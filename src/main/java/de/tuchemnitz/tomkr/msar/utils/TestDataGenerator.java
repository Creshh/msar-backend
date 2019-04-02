package de.tuchemnitz.tomkr.msar.utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDescriptor;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.github.javafaker.Faker;

import de.tuchemnitz.tomkr.msar.Config;
import de.tuchemnitz.tomkr.msar.core.DocumentHandler;
import de.tuchemnitz.tomkr.msar.core.SchemaHandler;
import de.tuchemnitz.tomkr.msar.core.registry.MetaTypeService;
import de.tuchemnitz.tomkr.msar.db.MetaTypeRepository;
import de.tuchemnitz.tomkr.msar.db.types.Asset;
import de.tuchemnitz.tomkr.msar.db.types.MetaType;
import de.tuchemnitz.tomkr.msar.elastic.IndexFunctions;
import de.tuchemnitz.tomkr.msar.storage.AssetService;

@Service
public class TestDataGenerator {

	@Autowired
	DocumentHandler docHandler;

	@Autowired
	IndexFunctions indexFunc;

	@Autowired
	SchemaHandler schemaHandler;
	
	@Autowired
	MetaTypeRepository metaRepo;
	
	@Autowired
	AssetService assetService;

	@Autowired
	Config config;

	private static final String API_CODE = "4qxzNF4J9p9Xm59X";

	private static final String BASE_PATH = "D:\\test_img";

	private static final String[] OBJECT_NAMES = new String[] {
			"chair", "table", "bottle", "car", "plant", "laptop", "person", "bicycle"
	};
	
	public boolean checkPermission(String apiCode) {
		return API_CODE.equals(apiCode);
	}

	public void generateData() {
		indexFunc.cleanIndex(config.getIndex());
//		metaRepo.deleteAll();
		List<MetaType> metaTypes = metaRepo.findAll();
		for(MetaType metaType : metaTypes) {
			if(!metaType.getType().equals(MetaTypeService.TYPE_META_SCHEMA)) {
				metaRepo.delete(metaType);
			}
		}

		schemaHandler.registerSchema("exif", Helpers.readResource("schema/exif.json"));
		schemaHandler.registerSchema("location", Helpers.readResource("schema/location.json"));
		schemaHandler.registerSchema("objects", Helpers.readResource("schema/objects.json"));

		File base = new File(BASE_PATH);
		Collection<File> files = FileUtils.listFiles(base, null, true);
		
		for(int i = 0; i< 10; i++) {
			for (File file : files) {
				handleImage(file);
			}
		}
	}

	private void handleImage(File image) {
		
		Asset asset = assetService.storeFile(image);				
		
		Map<String, Object> exif = readEXIF(image);
		Map<String, Object> loc = generateLocation(image);
		Map<String, Object> obj = generateObjects(image, (int) exif.get("xdim"), (int) exif.get("ydim"));

		docHandler.addDocument(JsonHelpers.mapToString(exif), String.valueOf(asset.getId()));
		docHandler.addDocument(JsonHelpers.mapToString(loc), String.valueOf(asset.getId()));
		docHandler.addDocument(JsonHelpers.mapToString(obj), String.valueOf(asset.getId()));
	}
	
	
	private Map<String, Object> generateObjects(File image, int width, int height) {
		Map<String, Object> result = new HashMap<>();
		
		result.put("reference", image.getAbsolutePath());
		result.put("type", "objects");
		result.put("source", "faker");
		
		List<String> objects = new ArrayList<>();
		List<int[]> coordinates = new ArrayList<>();
		
		Faker faker = new Faker();
		for(int i = 0; i < faker.number().numberBetween(0, 5); i++) {
			int[] coord = new int[4];
			coord[0] = faker.number().numberBetween(0, width - (width/4));
			coord[1] = faker.number().numberBetween(0, height - (height/4));
			coord[2] = faker.number().numberBetween(coord[0], width);
			coord[3] = faker.number().numberBetween(coord[1], height);
			
			coordinates.add(coord);
			objects.add(OBJECT_NAMES[faker.number().numberBetween(0, OBJECT_NAMES.length)]);
		}
		
		
		
		result.put("objects", objects);
		result.put("objects_coordinates", coordinates);
		
		return result;
	}

	private Map<String, Object> generateLocation(File image) {
		Map<String, Object> result = new HashMap<>();

		result.put("reference", image.getAbsolutePath());
		result.put("type", "location");
		result.put("source", "faker");

		Faker faker = new Faker();
		result.put("country", faker.address().country());
		result.put("city", faker.address().city());
		result.put("street", faker.address().streetName());
		result.put("number", faker.address().buildingNumber());
		try {
			DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.GERMAN);
			result.put("latitude", df.parseObject(faker.address().latitude()));
			result.put("longitude", df.parseObject(faker.address().longitude()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	private Map<String, Object> readEXIF(File image) {
		Map<String, Object> result = new HashMap<>();

		result.put("reference", image.getAbsolutePath());
		result.put("type", "exif");
		result.put("source", "exif");

		Metadata metadata = null;
		try {
			metadata = ImageMetadataReader.readMetadata(image);
		} catch (IOException | ImageProcessingException e) {
			e.printStackTrace();
		}

		if (metadata == null) {
			return null;
		}

//		for (Directory directory : metadata.getDirectories()) {
//		    for (Tag tag : directory.getTags()) {
//		        System.out.println(tag);
//		    }
//		}

		ExifIFD0Directory base = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

//		Collection<ExifDirectoryBase> base = metadata.getDirectoriesOfType(ExifDirectoryBase.class);
//		System.out.println("---------------------------------------------------");
//		for (Directory directory : base) {
//			for(Tag tag : directory.getTags()) {
//				System.out.println(tag);
//			}
//		}

		JpegDirectory jpeg = metadata.getFirstDirectoryOfType(JpegDirectory.class);

		ExifSubIFDDirectory exif = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
		ExifSubIFDDescriptor desc = new ExifSubIFDDescriptor(exif);

		result.put("datetime", base.getString(ExifIFD0Directory.TAG_DATETIME));
		result.put("manufacturer", base.getString(ExifIFD0Directory.TAG_MAKE));
		result.put("model", base.getString(ExifIFD0Directory.TAG_MODEL));

		try {
			int w = jpeg.getImageWidth();
			int h = jpeg.getImageHeight();
			result.put("xdim", w);
			result.put("ydim", h);
			
			result.put("orientation", w < h ? "portrait" : "landscape");
		} catch (MetadataException e) {
			e.printStackTrace();
		}
		result.put("compression", jpeg.getString(JpegDirectory.TAG_COMPRESSION_TYPE));

		result.put("datetimeoriginal", exif.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
		result.put("lens", exif.getString(ExifSubIFDDirectory.TAG_LENS));
		result.put("fnumber", exif.getString(ExifSubIFDDirectory.TAG_FNUMBER));
		result.put("exposuretime", exif.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
		result.put("isospeed", exif.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
		result.put("focallength", exif.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));

		
		result.put("flash", desc.getFlashDescription());
		result.put("meteringmode", desc.getMeteringModeDescription());

		result.entrySet().removeIf(entry -> (entry.getValue() == null || entry.getValue().equals("")));

		return result;
	}
}

package de.tuchemnitz.tomkr.msar.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.tuchemnitz.tomkr.msar.Config;
import de.tuchemnitz.tomkr.msar.db.AssetRepository;
import de.tuchemnitz.tomkr.msar.db.types.Asset;
import net.coobird.thumbnailator.Thumbnails;

@Service
public class AssetService {

	private static Logger LOG = LoggerFactory.getLogger(AssetService.class);

	@Autowired
	AssetRepository assetRepo;
	
	@Autowired
	Config config;

	@Autowired
	FileStorageService storageService;

	public Asset storeFile(File file) {
		// Normalize file name
		String fileName = file.getName();
		
		// Check if the file's name contains invalid characters
		if (fileName.contains("..") || file.isDirectory()) {
			LOG.error("Sorry! Filename contains invalid path sequence " + fileName + " or file is directory");
			return null;
		}
		LOG.debug(fileName + " - " + fileName.split("\\.")[1]);
		String type = fileName.split("\\.")[1];
		Asset asset = new Asset(fileName, type);

		assetRepo.save(asset);
		
		boolean success = storageService.storeFile(String.format("%d.%s", asset.getId(), type), file);
		
		if(!success) {
			LOG.error("Couldn't store file!");
			assetRepo.delete(asset);
			return null;
		}
		
		try {
			createThumb(asset, new FileInputStream(file));
		} catch (FileNotFoundException e) {
			LOG.error("Error reading file for creating thumbnail");
		}
		
		return asset;
	}
	
	
	public Asset storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = file.getOriginalFilename();
		
		// Check if the file's name contains invalid characters
		if (fileName.contains("..")) {
			LOG.error("Sorry! Filename contains invalid path sequence " + fileName);
			return null;
		}
		LOG.debug(file.getOriginalFilename() + " - " + file.getContentType());
		String type = fileName.split("\\.")[1];
		Asset asset = new Asset(fileName, type);

		assetRepo.save(asset);
		
		boolean success = storageService.storeFile(String.format("%d.%s", asset.getId(), type), file);
		
		if(!success) {
			LOG.error("Couldn't store file!");
			assetRepo.delete(asset);
			return null;
		}
		
		try {
			createThumb(asset, file.getInputStream());
		} catch (IOException e) {
			LOG.error("Error reading file for creating thumbnail");
		}
		
		return asset;
	}
	
	private void createThumb(Asset asset, InputStream inputStream) {
		String fileFormat = "%s/%s_t.%s";
		
		try {
			Thumbnails.of(inputStream).size(400, 400).toFile(String.format(fileFormat, config.getStorageBase(), asset.getId(), asset.getDataType()));
		} catch (IOException e) {
			LOG.error("Error creating and storing thumbnail!", e);
		}
	}

	public Resource loadFileAsResource(long id, boolean thumb) {
		Asset asset = assetRepo.findById(id).get();
		if(asset == null) {
			LOG.error(String.format("Identifier %s not found!", id));
			return null;
		}
		String fileFormat = thumb ? "%s_t.%s" : "%s.%s";
		return storageService.loadFileAsResource(String.format(fileFormat, asset.getId(), asset.getDataType()));
	}
}

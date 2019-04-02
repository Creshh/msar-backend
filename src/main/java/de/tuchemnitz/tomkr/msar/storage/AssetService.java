package de.tuchemnitz.tomkr.msar.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.tuchemnitz.tomkr.msar.db.AssetRepository;
import de.tuchemnitz.tomkr.msar.db.types.Asset;

@Service
public class AssetService {

	private static Logger LOG = LoggerFactory.getLogger(AssetService.class);

	@Autowired
	AssetRepository assetRepo;

	@Autowired
	FileStorageService storageService;

	public Asset storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = file.getOriginalFilename();
		
		// Check if the file's name contains invalid characters
		if (fileName.contains("..")) {
			LOG.error("Sorry! Filename contains invalid path sequence " + fileName);
			return null;
		}
		LOG.debug(file.getOriginalFilename() + " - " + file.getContentType());
		String id = fileName.split("\\.")[0]; // dot is contained in filename but won't split
		String type = fileName.split("\\.")[1];
		Asset asset = new Asset(id, fileName, type);

		// handle exisiting ids / overwrite
		
		storageService.storeFile(String.format("%s.%s", id, type), file);
		
		assetRepo.save(asset);
		return asset;
	}

	public Resource loadFileAsResource(String id) {
		
		Asset asset = assetRepo.findByIdentifier(id);
		if(asset == null) {
			LOG.error(String.format("Identifier %s not found!", id));
			return null;
		}
		
		return storageService.loadFileAsResource(String.format("%s.%s", asset.getIdentifier(), asset.getDataType()));
	}

}

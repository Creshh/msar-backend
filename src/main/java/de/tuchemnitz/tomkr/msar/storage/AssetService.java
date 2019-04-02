package de.tuchemnitz.tomkr.msar.storage;

import java.io.File;

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
		
		return asset;
	}

	public Resource loadFileAsResource(long id) {
		Asset asset = assetRepo.findById(id).get();
		if(asset == null) {
			LOG.error(String.format("Identifier %s not found!", id));
			return null;
		}
		
		return storageService.loadFileAsResource(String.format("%s.%s", asset.getId(), asset.getDataType()));
	}

}

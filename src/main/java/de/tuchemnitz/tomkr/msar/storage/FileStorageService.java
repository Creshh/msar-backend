package de.tuchemnitz.tomkr.msar.storage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import de.tuchemnitz.tomkr.msar.Config;

@Service
public class FileStorageService {

	private static Logger LOG = LoggerFactory.getLogger(FileStorageService.class);

	public class FileStorageException extends RuntimeException {
		private static final long serialVersionUID = 20190401115900L;

		public FileStorageException(String message) {
	        super(message);
	    }

	    public FileStorageException(String message, Throwable cause) {
	        super(message, cause);
	    }
	}
	
	@Autowired
    private Config config;
	private Path fileStorageLocation;

	@PostConstruct
	public void init() {	
    	fileStorageLocation = Paths.get(config.getStorageBase()).toAbsolutePath().normalize();
    	
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (Exception ex) {
            LOG.error("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public boolean storeFile(String fileName, File file) {
        try {
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.toPath(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            LOG.error("Could not store file " + fileName + ". Please try again!", ex);
            return false;
        }
        return true;
    }
    
    public boolean removeFile(String fileName) {
        try {
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.deleteIfExists(targetLocation);
        } catch (IOException ex) {
            LOG.error("Could not delete file " + fileName + ". Please try again!", ex);
            return false;
        }
        return true;
    }
	
    public boolean storeFile(String fileName, MultipartFile file) {
        try {
            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            LOG.error("Could not store file " + fileName + ". Please try again!", ex);
            return false;
        }
        return true;
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found " + fileName, ex);
        }
    }
}
package de.tuchemnitz.tomkr.msar.api;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.tuchemnitz.tomkr.msar.api.data.UploadFileResponse;
import de.tuchemnitz.tomkr.msar.db.types.Asset;
import de.tuchemnitz.tomkr.msar.storage.AssetService;

@RestController
@RequestMapping("api/assets")
public class AssetApiController {

	private static Logger LOG = LoggerFactory.getLogger(AssetApiController.class);

    @Autowired
    private AssetService assetService;
	
//    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping("/upload")
    public UploadFileResponse upload(@RequestParam("file") MultipartFile file) {
        Asset asset = assetService.storeFile(file);

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("assets/get/")
                .path(String.valueOf(asset.getId()))
                .toUriString();

        return new UploadFileResponse(asset.getId(), fileUri,
                file.getContentType(), file.getSize());
    }
    
    @GetMapping("/get/{id}")
    public ResponseEntity<Resource> get(@PathVariable long id, @RequestParam(name = "thumb", defaultValue = "false") boolean thumb, @RequestParam(name = "dl", defaultValue = "false") boolean dl, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = assetService.loadFileAsResource(id, thumb);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            LOG.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        
        BodyBuilder builder = ResponseEntity.ok();
        builder.contentType(MediaType.parseMediaType(contentType));
        if(dl) {
        	builder.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\""); // download
        } else {
        	builder.header(HttpHeaders.CONTENT_DISPOSITION, "inline"); // inline
        }
        return builder.body(resource);
    }
    
    @GetMapping("/remove/{id}")
    public boolean remove(@PathVariable long id, HttpServletRequest request) {
        boolean success = assetService.removeFile(id);

        return success;
    }
}

package de.tuchemnitz.tomkr.msar.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import de.tuchemnitz.tomkr.msar.Config;
import de.tuchemnitz.tomkr.msar.db.types.Asset;
import de.tuchemnitz.tomkr.msar.storage.AssetService;

@RestController
@RequestMapping("assets")
public class AssetApiController {

	private static Logger LOG = LoggerFactory.getLogger(AssetApiController.class);

	@Autowired
	private Config config;
	
    @Autowired
    private AssetService assetService;
	
	@GetMapping("/get/{reference}")
	public void getImageAsByteArray(@PathVariable("reference") String reference, HttpServletResponse response)
			throws IOException {
		InputStream in = FileUtils.openInputStream(new File(config.getStorageBase() + "/" + reference));
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		IOUtils.copy(in, response.getOutputStream());
	}
	
    
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        Asset asset = assetService.storeFile(file);

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/getAsset/")
                .path(String.valueOf(asset.getIdentifier()))
                .toUriString();

        return new UploadFileResponse(asset.getIdentifier(), fileUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/getAsset/{id}")
    public ResponseEntity<Resource> getAsset(@PathVariable String id, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = assetService.loadFileAsResource(id);

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

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

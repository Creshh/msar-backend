package de.tuchemnitz.tomkr.msar.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("assets")
public class AssetApiController {

	@GetMapping("/get/{reference}")
	public void getImageAsByteArray(@PathVariable("reference") String reference, HttpServletResponse response)
			throws IOException {
		InputStream in = FileUtils.openInputStream(new File("D:/test_img/" + reference));
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		IOUtils.copy(in, response.getOutputStream());
	}
}

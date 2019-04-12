package de.tuchemnitz.tomkr.msar.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.tuchemnitz.tomkr.msar.core.DocumentHandler;
import de.tuchemnitz.tomkr.msar.core.SchemaHandler;
import de.tuchemnitz.tomkr.msar.utils.TestDataGenerator;



@RestController
@RequestMapping("api/doc")
public class DocumentApiController {
	private static Logger LOG = LoggerFactory.getLogger(DocumentApiController.class);


	@Autowired
	SchemaHandler schemaHandler;
	
	@Autowired
	DocumentHandler documentHandler;

	@Autowired
	TestDataGenerator testDataGenerator;
	
	@GetMapping("/generateTestData")
	public boolean generateTestData(String apiCode) {
		LOG.debug("############################ Generate Test Data ############################");
		
		if(!testDataGenerator.checkPermission(apiCode)) {
			LOG.debug("No permission");
			return false;
		}
		
		testDataGenerator.generateData();
		
		LOG.debug("################################# Finished #################################");
		return true;
	}	
	
	@PostMapping("type/add")
	public boolean addType(@RequestParam String type, @RequestBody String schema) {
		LOG.debug(String.format("[/api/addType]: [%s] \n------------------------\n%s\n------------------------", type, schema));
		return schemaHandler.registerSchema(type, schema);
	}

	@PostMapping("/addDocument")
	public boolean addDocument(@RequestBody String document, String reference) {
		LOG.debug(String.format("[/api/addDocument] for [%s]: \n%s\n------------------------", reference,  document));
		return documentHandler.addDocument(document, reference);
	}
	
	@PostMapping("/add")
	public boolean add(@RequestParam("file") MultipartFile file, String reference) {
		LOG.debug(String.format("[/api/addDocument]: \n------------------------"));
		
		if(MediaType.APPLICATION_JSON != MediaType.parseMediaType(file.getContentType())) {
			LOG.error("No json format uploaded");
			return false;
		}
		
		String document; 
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
			document = IOUtils.toString(stream, "UTF-8");
		} catch (IOException e) {
			LOG.error("Error while reading file");
			return false;
		}
		
		return documentHandler.addDocument(document, reference);
	}
}

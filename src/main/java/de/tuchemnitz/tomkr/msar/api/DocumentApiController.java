package de.tuchemnitz.tomkr.msar.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import de.tuchemnitz.tomkr.msar.core.DocumentHandler;
import de.tuchemnitz.tomkr.msar.core.SchemaHandler;
import de.tuchemnitz.tomkr.msar.core.registry.MetaTypeService;
import de.tuchemnitz.tomkr.msar.utils.Result;
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
	MetaTypeService metaTypeService;

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
	
	@PostMapping("/addDocument")
	public ResponseEntity<String> addDocument(@RequestBody String document, String reference) {
		LOG.debug(String.format("[/api/addDocument] for [%s]: \n%s\n------------------------", reference,  document));
		// Result result =  documentHandler.addDocument(document, reference);
		documentHandler.addDocument(document, reference);
		return new ResponseEntity<String>("", HttpStatus.OK); // TODO
	}
	
	@PostMapping("/add")
	public ResponseEntity<String> add(@RequestParam("file") MultipartFile file, String reference) {
		LOG.debug(String.format("[/api/addDocument]: \n------------------------")); 
		
		if(!MediaType.APPLICATION_JSON.equals(MediaType.parseMediaType(file.getContentType()))) {
			LOG.error("No json format uploaded");
			return new ResponseEntity<String>("No json format uploaded", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}
		
		String document; 
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
			document = IOUtils.toString(stream, "UTF-8");
		} catch (IOException e) {
			LOG.error("Error while reading file");
			return new ResponseEntity<String>("Error reading file", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		Result result = documentHandler.addDocument(document, reference);
		if(result.isSuccess()) {
			return new ResponseEntity<String>("Success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(result.getMsg(), HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@GetMapping("/get")
	public Map<String, Object> getDocuments(String reference) {
		return documentHandler.getDocuments(reference); 
	}
}

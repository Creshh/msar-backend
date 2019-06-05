package de.tuchemnitz.tomkr.msar.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
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

import de.tuchemnitz.tomkr.msar.core.SchemaHandler;
import de.tuchemnitz.tomkr.msar.core.registry.MetaTypeService;
import de.tuchemnitz.tomkr.msar.db.types.Field;
import de.tuchemnitz.tomkr.msar.utils.Result;



@RestController
@RequestMapping("api/type")
public class TypeApiController {
	private static Logger LOG = LoggerFactory.getLogger(TypeApiController.class);


	@Autowired
	SchemaHandler schemaHandler;
	
	@Autowired
	MetaTypeService metaTypeService;
	
	@PostMapping("/addString")
	public boolean addType(@RequestBody String schema) {
		LOG.debug(String.format("[/api/type/add]: [%s] \n------------------------\n%s\n------------------------", schema));
		return schemaHandler.registerSchema(schema).isSuccess();
	}

	@GetMapping("/get")
	public Map<String, Object> getTypes() {
		return metaTypeService.getAllTypes();
	}
	
	@PostMapping("/add")
	public ResponseEntity<String> add(@RequestParam("file") MultipartFile file) {
		LOG.debug(String.format("[/api/addDocument]: \n------------------------")); 
		
		if(!MediaType.APPLICATION_JSON.equals(MediaType.parseMediaType(file.getContentType()))) {
			LOG.error("No json format uploaded");
			return new ResponseEntity<String>("No json format uploaded", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		}
		
		String schema; 
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(file.getBytes());
			schema = IOUtils.toString(stream, "UTF-8");
		} catch (IOException e) {
			LOG.error("Error while reading file");
			return new ResponseEntity<String>("Error reading file", HttpStatus.UNPROCESSABLE_ENTITY);
		}
		
		Result result = schemaHandler.registerSchema(schema);
		if(result.isSuccess()) {
			return new ResponseEntity<String>("Success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>(result.getMsg(), HttpStatus.NOT_ACCEPTABLE);
		}
	}
	
	@GetMapping("/getFields")
	public List<Field> getFields() {
		return metaTypeService.getAllFields();
	}
}

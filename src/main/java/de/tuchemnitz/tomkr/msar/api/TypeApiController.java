package de.tuchemnitz.tomkr.msar.api;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.tuchemnitz.tomkr.msar.core.SchemaHandler;
import de.tuchemnitz.tomkr.msar.core.registry.MetaTypeService;



@RestController
@RequestMapping("api/type")
public class TypeApiController {
	private static Logger LOG = LoggerFactory.getLogger(TypeApiController.class);


	@Autowired
	SchemaHandler schemaHandler;
	
	@Autowired
	MetaTypeService metaTypeService;
	
	@PostMapping("/add")
	public boolean addType(@RequestParam String type, @RequestBody String schema) {
		LOG.debug(String.format("[/api/type/add]: [%s] \n------------------------\n%s\n------------------------", type, schema));
		return schemaHandler.registerSchema(type, schema);
	}

	@GetMapping("/get")
	public Map<String, Object> getTypes() {
		return metaTypeService.getAllTypes();
	}
}

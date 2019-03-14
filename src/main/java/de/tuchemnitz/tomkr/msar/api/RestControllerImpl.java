package de.tuchemnitz.tomkr.msar.api;

import java.util.ArrayList;
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

import de.tuchemnitz.tomkr.msar.api.data.SuggestCategory;
import de.tuchemnitz.tomkr.msar.core.DocumentHandler;
import de.tuchemnitz.tomkr.msar.core.SchemaHandler;
import de.tuchemnitz.tomkr.msar.elastic.QueryFunctions;



@org.springframework.web.bind.annotation.RestController
@RequestMapping("api")
public class RestControllerImpl {
	private static Logger LOG = LoggerFactory.getLogger(RestControllerImpl.class);


	@Autowired
	QueryFunctions queryFunctions;
	
	@Autowired
	SchemaHandler schemaHandler;
	
	@Autowired
	DocumentHandler documentHandler;

	@RequestMapping("/")
	public String index() {
		LOG.debug("API Index");
		return "API Index";
	}


	
	@GetMapping("/suggest")
	public Map<String, SuggestCategory> suggest(String prefix) {
		LOG.debug(String.format("[/v1/suggest]: [%s]", prefix));
		return queryFunctions.getSuggestions(prefix);
	}


	@GetMapping("/search/query")
	public List<Map<String, Object>> search(String query) {
		return queryFunctions.searchByValue(query);
	}

	@GetMapping("/search/field")
	public List<Map<String,Object>> search(String value, String field) {
		return queryFunctions.matchByValue(value, field);
	}

	@GetMapping("/search/range")
	public List<Map<String,Object>> search(double lower, double upper, String field) {
//		if(!(lower instanceof Number && upper instanceof Number)) {
//			LOG.error("Wrong datatype, numbers expected");
//			return null;
//		}
		return queryFunctions.matchByRange(lower, upper, field);
	}


	@PostMapping("/addType")
	public boolean addType(@RequestParam String type, @RequestBody String schema) {
		LOG.debug(String.format("[/v1/addType]: [%s] \n------------------------\n%s\n------------------------", type, schema));
		return schemaHandler.registerSchema(type, schema);
	}


	@PostMapping("/addDocument")
	public boolean addDocument(@RequestBody String document) {
		LOG.debug(String.format("[/v1/addDocument]: \n%s\n------------------------", document));
		return documentHandler.addDocument(document);
	}
}

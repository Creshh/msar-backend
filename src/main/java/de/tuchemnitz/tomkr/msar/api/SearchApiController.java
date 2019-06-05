package de.tuchemnitz.tomkr.msar.api;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.tuchemnitz.tomkr.msar.api.data.SuggestCategory;
import de.tuchemnitz.tomkr.msar.elastic.QueryFunctions;


@RestController
@RequestMapping("api/search")
public class SearchApiController {
	private static Logger LOG = LoggerFactory.getLogger(SearchApiController.class);


	@Autowired
	QueryFunctions queryFunctions;
	
	@GetMapping("/suggest")
	public Map<String, SuggestCategory> suggest(String prefix) {
//		LOG.debug(String.format("[/api/suggest]: [%s]", prefix));
		return queryFunctions.getSuggestions(prefix);
	}


	@GetMapping("/query")
	public List<Map<String, Object>> search(String query) {
		return queryFunctions.searchByValue(query);
	}

	@GetMapping("/field")
	public List<Map<String,Object>> search(String value, String field) {
		return queryFunctions.matchByValue(value, field);
	}

	@GetMapping("/range")
	public List<Map<String,Object>> search(double lower, double upper, String field) {
//		if(!(lower instanceof Number && upper instanceof Number)) {
//			LOG.error("Wrong datatype, numbers expected");
//			return null;
//		}
		return queryFunctions.matchByRange(lower, upper, field);
	}
	/**
	 * Request in json format:
	 * field: {
	 * 	must: true
	 * 	lower: value
	 * 	upper: value
	 * }
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/multiple")
	public List<Map<String,Object>> searchMultiple(String request) {
		return queryFunctions.searchMultiple(request);
	}
		
}

package de.tuchemnitz.tomkr.msar.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.tuchemnitz.tomkr.msar.api.data.SuggestCategory;
import de.tuchemnitz.tomkr.msar.elastic.QueryFunctions;


/**
 * API Controller for search
 * Provides API endpoints for suggestions, search and queries.
 * 
 * @author Tom Kretzschmar
 *
 */
@RestController
@RequestMapping("api/search")
public class SearchApiController {


	@Autowired
	private QueryFunctions queryFunctions;
	
	/**
	 * Get map of suggestions for given prefix mapped to the corresponding metadata field name.
	 * 
	 * @param prefix The prefix, the suggestions should be generated for
	 * @return Map of metadata field to value list mapping
	 */
	@GetMapping("/suggest")
	public Map<String, SuggestCategory> suggest(String prefix) {
		return queryFunctions.getSuggestions(prefix);
	}


	/**
	 * Get documents based on a query string query.
	 * See https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl-query-string-query.html
	 * 
	 * @param query The query string.
	 * @return The resulting documents.
	 */
	@GetMapping("/query")
	public List<Map<String, Object>> search(String query) {
		return queryFunctions.searchByValue(query);
	}

	/**
	 * Get documents based on a term and a given field.
	 * 
	 * @param value The value to search for.
	 * @param field The field which is included in the search.
	 * @return The resulting documents.
	 */
	@GetMapping("/field")
	public List<Map<String,Object>> search(String value, String field) {
		return queryFunctions.matchByValue(value, field);
	}

	/**
	 * Get documents based on a range and a given field.
	 * 
	 * @param lower The lower limit of the range (included)
	 * @param upper The upper limit of the range (excluded)
	 * @param field The field which is included in the search.
	 * @return The resulting documents.
	 */
	@GetMapping("/range")
	public List<Map<String,Object>> search(double lower, double upper, String field) {
		return queryFunctions.matchByRange(lower, upper, field);
	}
	
	/**
	 * Search based on multiple, combined search requests. These requests can be normal term queries or range queries.<br/>
	 * The key is arbitrary, add defines the negation for the entry, lower is the value or lower limit of the range, upper is the upper limit of the range (if given).
	 * 
	 * @param request The request in json format:
	 * <pre>{
	 *    key: {
	 *        field: name
	 * 	      add: true
	 * 	      lower: value
	 * 	      upper: value
	 *    },
	 *    ...
	 *}</pre>
	 *
	 * @return The resulting documents.
	 */
	@PostMapping("/multiple")
	public List<Map<String,Object>> searchMultiple(@RequestBody String request) {
		return queryFunctions.searchMultiple(request);
	}
		
}

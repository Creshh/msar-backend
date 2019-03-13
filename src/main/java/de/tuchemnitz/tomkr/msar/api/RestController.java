package de.tuchemnitz.tomkr.msar.api;

import java.util.List;
import java.util.Map;

public interface RestController {

	List<String> suggest(String prefix);
	
	List<Map<String, Object>> search(String value);
	
	List<Map<String, Object>> search(String value, String field);
	
	List<Map<String, Object>> search(double lower, double upper, String field);
	
	boolean addType(String type, String schema);
	
	boolean addDocument(String document);

}

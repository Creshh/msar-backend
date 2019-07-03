package de.tuchemnitz.tomkr.msar.core.registry;

import java.util.Map;

import de.tuchemnitz.tomkr.msar.utils.Helpers;


/**
 * Utility class for mapping json schema type definitions to elasticsearch types.
 * 
 * @author Tom Kretzschmar
 * 
 */
public class DataTypeMapper {
	
	/**
	 * The type mapping itself.
	 */
	private Map<String, String> typeMap;
	
	/**
	 * Create a type mapping using the given property file.
	 * 
	 * @param typeMappingRes The property file containing the type mappings line by line.
	 */
	public DataTypeMapper(String typeMappingRes) {
		typeMap = Helpers.getPropertiesAsMap(typeMappingRes);
	}
	
	/**
	 * Map a schema type to an elasticsearch data type.
	 * 
	 * @param sourceType The schema type which should be mapped.
	 * @return The resulting elasticsearch type.
	 */
	public String map(String sourceType) {
		return typeMap.get(sourceType);
	}
}
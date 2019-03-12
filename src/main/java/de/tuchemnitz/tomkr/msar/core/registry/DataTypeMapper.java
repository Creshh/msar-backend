package de.tuchemnitz.tomkr.msar.core.registry;

import java.util.Map;

import de.tuchemnitz.tomkr.msar.utils.Helpers;


/**
 * 
 * @author Kretzschmar
 * 
 */
public class DataTypeMapper {
	
	private Map<String, String> typeMap;
	
	public DataTypeMapper(String typeMappingRes) {
		typeMap = Helpers.getPropertiesAsMap(typeMappingRes);
	}
	
	public String map(String sourceType) {
		return typeMap.get(sourceType);
	}
}
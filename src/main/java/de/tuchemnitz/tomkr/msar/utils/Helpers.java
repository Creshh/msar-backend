package de.tuchemnitz.tomkr.msar.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Helpers {

	private static Logger LOG = LoggerFactory.getLogger(Helpers.class);

	
	public static Properties loadProperties(String resourceName){
		Properties prop = new Properties();
		try {
			prop.load(Helpers.class.getClassLoader().getResourceAsStream(resourceName));
		} catch (Exception e) { 
			LOG.error(String.format("Error while loading properties [%s]", resourceName));
		}
		return prop;
	}
	
	public static Map<String, String> getPropertiesAsMap(String resourceName){
		Properties prop = loadProperties(resourceName);
		Map<String, String> result = new HashMap<>();
		
		for (String key : prop.stringPropertyNames()) {
		    result.put(key, prop.getProperty(key));
		}
		
		return result;
	}
}
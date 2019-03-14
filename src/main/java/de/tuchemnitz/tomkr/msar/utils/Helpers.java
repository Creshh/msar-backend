package de.tuchemnitz.tomkr.msar.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

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
	
	public static String readFile(File file) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			return IOUtils.toString(is, StandardCharsets.UTF_8);
		} catch (IOException e) {
			LOG.error("Error reading from InputStream", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.error("Error closing InputStream", e);
				}
			}
		}
		return null;
	}
	
	public static String readResource(String resource) {
		try {
			File file = ResourceUtils.getFile(String.format("classpath:%s", resource));
			return readFile(file);
		} catch (FileNotFoundException e) {
			LOG.error(String.format("Error reading resource %s", resource), e);
		}
		return null;
	}
}
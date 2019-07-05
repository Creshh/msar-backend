package de.tuchemnitz.tomkr.msar.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class providing static helper methods for various tasks.
 * 
 * @author Tom Kretzschmar
 *
 */
public class Helpers {

	private static Logger LOG = LoggerFactory.getLogger(Helpers.class);

	
	/**
	 * Load properties as {@link Properties} object from given resource file.
	 * 
	 * @param resourceName The resource file name. 
	 * @return A Properties object representing the content of the resource file.
	 */
	public static Properties loadProperties(String resourceName){
		Properties prop = new Properties();
		try {
			prop.load(Helpers.class.getClassLoader().getResourceAsStream(resourceName));
		} catch (Exception e) { 
			LOG.error(String.format("Error while loading properties [%s]", resourceName));
		}
		return prop;
	}
	
	/**
	 * Load properties as {@link Map} from given resource file.
	 * 
	 * @param resourceName The resource file name.
	 * @return A Map representing the content of the resource file.
	 */
	public static Map<String, String> getPropertiesAsMap(String resourceName){
		Properties prop = loadProperties(resourceName);
		Map<String, String> result = new HashMap<>();
		
		for (String key : prop.stringPropertyNames()) {
		    result.put(key, prop.getProperty(key));
		}
		
		return result;
	}

	/**
	 * Read file to string.
	 * 
	 * @param file The file object which should be read.
	 * @return The string content of the file.
	 */
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

	/**
	 * Read resource file to string.
	 * @param resource The resource file name.
	 * @return The string content of the resource.
	 */
	public static String readResource(String resource) {
		InputStream in = Helpers.class.getResourceAsStream("/" + resource); 
		if(in != null){
			StringWriter writer = new StringWriter();
			try {
				IOUtils.copy(in, writer, StandardCharsets.UTF_8);
				return writer.toString();
			} catch (IOException e) {
				LOG.error(String.format("Error reading resource %s", resource), e);
			}
		} else {
			LOG.error(String.format("Error reading resource %s; not found", resource));
		}
		return null;
	}
}
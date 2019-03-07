package de.tuchemnitz.tomkr.msar.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JsonHelper {

	private static Logger LOG = LoggerFactory.getLogger(JsonHelper.class);

	public static JSONObject loadJSON(String json) {
		return new JSONObject(new JSONTokener(json));
	}
	
	public static JSONObject loadJSONFromFile(String filePath) {
		InputStream is = null;
		String json = null;
		try {
			is = new FileInputStream(new File("D:\\ws\\eclipse_ws\\metaapp\\src\\main\\resources\\schema\\metaData.json"));
			json = IOUtils.toString(is, StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return loadJSON(json);
	}
	
	// validate against schema beforehand; add "tags" to schema, where it is defined which fields will be queried as tags -> put these fields in a registry.  
	@SuppressWarnings("unchecked")
	public Map<String, Object> readJsonToMap(String filePath){
		HashMap<String, Object> result = null;
		try {
			result = new ObjectMapper().readValue(new File(filePath), HashMap.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}

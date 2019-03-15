package de.tuchemnitz.tomkr.msar.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelpers {

	private static Logger LOG = LoggerFactory.getLogger(JsonHelpers.class);

	public static JSONObject loadJSON(String json) {
		if(json == null) {
			LOG.error("Can't decode null string to json");
		}
		return new JSONObject(new JSONTokener(json));
	}

	public static JSONObject loadJSONFromFilePath(String filePath) {
		return loadJSON(Helpers.readFile(new File(filePath)));
	}

	public static JSONObject loadJSONFromFile(File file) {
		return loadJSON(Helpers.readFile(file));
	}

	public static JSONObject loadJSONFromResource(String resource) {
		return loadJSON(Helpers.readResource(resource));
	}

	public static Map<String, Object> readJsonToMapFromFile(File file) {
		String json = Helpers.readFile(file); 
		return readJsonToMap(json);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> readJsonToMap(String json) {
		HashMap<String, Object> result = null;
		try {
			result = new ObjectMapper().readValue(json, HashMap.class);
		} catch (IOException e) {
			LOG.error(String.format("Error reading json"), e);
		}
		return result;
	}

	public static String mapToString(Map<String, Object> result) {
		try {
			return new ObjectMapper().writeValueAsString(result);
		} catch (JsonProcessingException e) {
			LOG.error(String.format("Error writing json"), e);
		}
		return null;
	}
}

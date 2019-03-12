package de.tuchemnitz.tomkr.msar.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelpers {

	private static Logger LOG = LoggerFactory.getLogger(JsonHelpers.class);

	public static JSONObject loadJSON(String json) {
		return new JSONObject(new JSONTokener(json));
	}

	public static JSONObject loadJSONFromFilePath(String filePath) {
		return loadJSONFromFile(new File(filePath));
	}

	public static JSONObject loadJSONFromFile(File file) {
		InputStream is = null;
		String json = null;
		try {
			is = new FileInputStream(file);
			json = IOUtils.toString(is, StandardCharsets.UTF_8);
			return loadJSON(json);
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

	public static JSONObject loadJSONFromResource(String resource) {
		try {
			File file = ResourceUtils.getFile(String.format("classpath:%s", resource));
			return loadJSONFromFile(file);
		} catch (FileNotFoundException e) {
			LOG.error(String.format("Error reading resource %s", resource), e);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> readJsonToMapFromFile(String filePath) {
		HashMap<String, Object> result = null;
		try {
			result = new ObjectMapper().readValue(new File(filePath), HashMap.class);
		} catch (IOException e) {
			LOG.error(String.format("Error reading file %s", filePath), e);
		}
		return result;
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
}

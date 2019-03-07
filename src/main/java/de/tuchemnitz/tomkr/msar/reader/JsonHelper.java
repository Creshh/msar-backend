package de.tuchemnitz.tomkr.msar.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

@Service
public class JsonHelper {

	private static Logger LOG = LoggerFactory.getLogger(JsonHelper.class);

	public JsonNode readJSONFromResource(String resourcePath) {

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;
		try {
			root = mapper.readTree(this.getClass().getResourceAsStream(resourcePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
	}

	public JsonNode readJSON(String filePath) {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;
		try {
			root = mapper.readTree(new File(filePath)); // breaks with ß or similar -
														// com.fasterxml.jackson.core.JsonParseException: Invalid UTF-8
														// middle byte 0x65
		} catch (IOException e) {
			e.printStackTrace();
		}
		return root;
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
	
	public void validate() {
		try {
			JsonNode locationSchema = readJSON(
					"D:\\ws\\eclipse_ws\\metaapp\\src\\main\\resources\\jsonSchema\\location.json"); // D:\\ws\\eclipse_ws\\metaapp\\src\\main\\java\\de\\tuchemnitz\\tomkr\\meta\\test1.json

			final JsonNode locationJSON = readJSON(
					"D:\\ws\\eclipse_ws\\metaapp\\src\\test\\resources\\exampleJson\\locationExample2.json");

			final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

			final JsonSchema schema = factory.getJsonSchema(locationSchema);

			ProcessingReport report;

			report = schema.validate(locationJSON);

			if (report.isSuccess()) {
				LOG.info("VALIDATION SUCCESS");
			} else {
				LOG.error("VALIDATION ERROR");
			}
			LOG.info(report.toString());

		} catch (ProcessingException e) {
			e.printStackTrace();
		}
	}

	public void validateNew() {
		InputStream isSchema = null;
		InputStream isFile = null;
		try {
			isSchema = new FileInputStream(new File("D:\\ws\\eclipse_ws\\metaapp\\src\\main\\resources\\schema\\metaData.json"));
			isFile = new FileInputStream(new File("D:\\ws\\eclipse_ws\\metaapp\\src\\test\\resources\\jsonExamples\\locationExample2.json"));

			
			
			String stringSchema = IOUtils.toString(isSchema, StandardCharsets.UTF_8);
			String stringFile = IOUtils.toString(isFile, StandardCharsets.UTF_8);
			
			
			JSONObject rawSchema = new JSONObject(new JSONTokener(stringSchema));
			JSONObject rawFile = new JSONObject(new JSONTokener(stringFile));

			Schema schema = SchemaLoader.load(rawSchema);
			schema.validate(rawFile); // throws a ValidationException if this object is invalid
			
			
		} catch (ValidationException e) {
			LOG.error("ERROR VALIDATING JSON FILE" + e.getMessage());
			
			  e.getCausingExceptions().stream()
		      .map(ValidationException::getMessage)
		      .forEach(System.out::println);
			
			  System.out.println("######################");
			  
			  System.out.println(e.toJSON().toString(4));
			  
			  System.out.println("######################");
			  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (isSchema != null) {
				try {
					isSchema.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isFile != null) {
				try {
					isFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

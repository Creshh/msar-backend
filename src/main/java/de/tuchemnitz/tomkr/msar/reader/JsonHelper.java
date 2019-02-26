package de.tuchemnitz.tomkr.msar.reader;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.examples.Utils;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import de.tuchemnitz.tomkr.msar.model.MetaData;

@Service
public class JsonHelper {

	public MetaData readJson(String filePath) {

		return null;
	}

	public void validate() {
		JsonNode locationSchema;
		try {
			locationSchema = Utils.loadResource("/src/main/resources/jsonSchema/location.json");

			final JsonNode locationJSON = Utils.loadResource("/src/test/resources/exampleJson/locationExample.json");

			final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();

			final JsonSchema schema = factory.getJsonSchema(locationSchema);

			ProcessingReport report;

			report = schema.validate(locationJSON);
			
			System.out.println(report);
		} catch (IOException | ProcessingException e) {
			e.printStackTrace();
		}
	}
}

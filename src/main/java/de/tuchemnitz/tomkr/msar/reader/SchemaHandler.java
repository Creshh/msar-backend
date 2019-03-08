package de.tuchemnitz.tomkr.msar.reader;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.search.IndexService;
import de.tuchemnitz.tomkr.msar.search.TypeRegistry;
import de.tuchemnitz.tomkr.msar.search.TypeRegistry.SchemaFields;

@Service
public class SchemaHandler {

	private static Logger LOG = LoggerFactory.getLogger(SchemaHandler.class);

	@Autowired
	private TypeRegistry typeRegistry;

	@Autowired
	private IndexService indexService;

	public void validateNew() {
		try {
			JSONObject rawSchema = JsonHelper
					.loadJSONFromFilePath("D:\\ws\\eclipse_ws\\metaapp\\src\\main\\resources\\schema\\metaData.json");
			JSONObject rawFile = JsonHelper.loadJSONFromFilePath(
					"D:\\ws\\eclipse_ws\\metaapp\\src\\test\\resources\\jsonExamples\\locationExample2.json");

			Schema schema = SchemaLoader.load(rawSchema);
			schema.validate(rawFile);
		} catch (ValidationException e) {
			LOG.error("ERROR VALIDATING JSON FILE" + e.getMessage());

			e.getCausingExceptions().stream().map(ValidationException::getMessage).forEach(System.out::println);

			System.out.println("######################");

			System.out.println(e.toJSON().toString(4));

			System.out.println("######################");
		}
	}

	public boolean validate(Schema schema, JSONObject json) {
		try {
			schema.validate(json);
		} catch (ValidationException e) {
			LOG.error("ERROR VALIDATING JSON FILE" + e.getMessage());

			e.getCausingExceptions().stream().map(ValidationException::getMessage).forEach(System.out::println);

			System.out.println("######################");

			System.out.println(e.toJSON().toString(4));

			System.out.println("######################");
			return false;
		}
		return true;
	}

	/**
	 * 
	 * Maybe get type from title
	 * 
	 * @param type
	 * @param schemaJSON
	 * @return
	 */
	public boolean registerSchema(String type, String schemaJSON) {
		if (typeRegistry.contains(type)) {
			LOG.error(String.format("Type [%s] already registered - abort registration.", type));
		}

		JSONObject schemaRoot = JsonHelper.loadJSON(schemaJSON);

		for (String field : SchemaFields.getAllRequired()) {
			if (!schemaRoot.has(field)) {
				LOG.error(String.format("Field \"%s\" has to be specified in schema!", field));
				return false;
			}
		}

		boolean valid = validate(typeRegistry.getMetaSchema(), schemaRoot);

		if (!valid) {
			LOG.error("Given schema is not valid!");
			return false;
		}

		Schema schema = SchemaLoader.load(schemaRoot);
		typeRegistry.addSchema(type, schema);

		try {
			typeRegistry.startDocument();
			JSONObject properties = schemaRoot.getJSONObject(SchemaFields.PROPERTIES);
			for (String key : properties.keySet()) {
				JSONObject field = properties.getJSONObject(key);
				
				// extract and map type definition from schema to elastic types
				String dataType = typeRegistry.getTypeMapping(field.getString(SchemaFields.TYPE));
				if(dataType == null) {
					LOG.error(String.format("No TypeMapping for type [%s] found!", field.getString(type)));
					return false;
				}
				
				// add Field to type registry for tagging and to Builder for generation of mapping
				typeRegistry.addField(TypeRegistry.TYPE, key, dataType, (field.has(SchemaFields.TAG) ? field.getBoolean(SchemaFields.TAG) : false));
			}

			// apply mapping update
			XContentBuilder builder = typeRegistry.endDocument();
			indexService.createMapping(builder, TypeRegistry.INDEX, TypeRegistry.TYPE);
		} catch (IOException e) {
			LOG.error("Error while creating mapping!", e);
			return false;
		}

		return true;
	}
}

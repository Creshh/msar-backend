package de.tuchemnitz.tomkr.msar.core;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.Config;
import de.tuchemnitz.tomkr.msar.core.registry.DataTypeMapper;
import de.tuchemnitz.tomkr.msar.core.registry.FieldRegistry;
import de.tuchemnitz.tomkr.msar.core.registry.TypeRegistry;
import de.tuchemnitz.tomkr.msar.elastic.IndexFunctions;
import de.tuchemnitz.tomkr.msar.utils.JsonHelpers;

/**
 * 
 * @author Kretzschmar
 *
 */
@Service
public class SchemaHandler {

	private static Logger LOG = LoggerFactory.getLogger(SchemaHandler.class);

	@Autowired
	private Config config;
	
	@Autowired
	private DataTypeMapper dataTypeMapper;
	
	@Autowired
	private TypeRegistry typeRegistry;
	
	@Autowired
	private FieldRegistry fieldRegistry;

	@Autowired
	private IndexFunctions indexService;

	@Autowired
	private Validator validator;
	
//  Maybe get type from title
//	private static final String TITLE = "title";
	private static final String TYPE = "type";
	private static final String PROPERTIES = "properties";
	private static final String TAG = "tag";
	
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

		JSONObject schemaRoot = JsonHelpers.loadJSON(schemaJSON);
		boolean valid = validator.checkDocument(typeRegistry.getMetaSchema(), schemaRoot);

		if (!valid) {
			LOG.error("Given schema is not valid!");
			return false;
		}

		Schema schema = SchemaLoader.load(schemaRoot);
		typeRegistry.addSchema(type, schema);

		try {
			fieldRegistry.startDocument();
			JSONObject properties = schemaRoot.getJSONObject(PROPERTIES);
			for (String key : properties.keySet()) {
				JSONObject field = properties.getJSONObject(key);
				
				// extract and map type definition from schema to elastic types
				String dataType = dataTypeMapper.map(field.getString(TYPE));
				if(dataType == null) {
					LOG.error(String.format("No TypeMapping for type [%s] found!", field.getString(TYPE)));
					return false;
				}
				
				// add Field to type registry for tagging and to Builder for generation of mapping
				fieldRegistry.addField(config.getType(), key, dataType, (field.has(TAG) ? field.getBoolean(TAG) : false));
			}

			// apply mapping update
			XContentBuilder builder = fieldRegistry.endDocument();
			indexService.createMapping(builder, config.getIndex(), config.getType());
		} catch (IOException e) {
			LOG.error("Error while creating mapping!", e);
			return false;
		}

		return true;
	}
}

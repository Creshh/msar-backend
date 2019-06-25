package de.tuchemnitz.tomkr.msar.core;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.Config;
import de.tuchemnitz.tomkr.msar.core.registry.DataTypeMapper;
import de.tuchemnitz.tomkr.msar.core.registry.MappingBuilder;
import de.tuchemnitz.tomkr.msar.core.registry.MetaTypeService;
import de.tuchemnitz.tomkr.msar.db.types.Field;
import de.tuchemnitz.tomkr.msar.db.types.MetaType;
import de.tuchemnitz.tomkr.msar.utils.JsonHelpers;
import de.tuchemnitz.tomkr.msar.utils.Result;

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
	private MetaTypeService metaTypeService;
	
	@Autowired
	private MappingBuilder mappingBuilder;

	@Autowired
	private Validator validator;
	
	
	private static final String TITLE = "title";
	private static final String TYPE = "type";
	private static final String PROPERTIES = "properties";
	private static final String SUGGEST = "suggest";
	private static final String FIELD_TYPE = "searchType";
	private static final String ARRAY = "array";
	private static final String ITEMS = "items";

	
	
	/**
	 * 
	 * 
	 * @param schemaJSON
	 * @return
	 */
	public Result registerSchema(String schemaJSON) {
		JSONObject schemaRoot = JsonHelpers.loadJSON(schemaJSON);
		String type = null;
		try{
			type = schemaRoot.getString(TITLE);
		} catch (JSONException e) {
			LOG.error("No title defined in Schema!");
			return new Result(false, "No title defined in Schema!");
		}
		
		if (metaTypeService.contains(type)) {
			LOG.error(String.format("Type [%s] already registered - abort registration.", type));
			return new Result(false, String.format("Type [%s] already registered - abort registration.", type));
		}

		
		Result validation = validator.checkDocument(metaTypeService.getMetaSchema(), schemaRoot);

		if (!validation.isSuccess()) {
			LOG.error("Given schema is not valid!");
			return new Result(false, "Given schema is not valid! [" + validation.getMsg() + "]");
		}

		MetaType metaType = new MetaType();
		metaType.setType(type);
		metaType.setSchema(schemaJSON);

		try {
			mappingBuilder.startDocument();
			JSONObject properties = schemaRoot.getJSONObject(PROPERTIES);
			for (String key : properties.keySet()) {
				JSONObject field = properties.getJSONObject(key);
				
				String sourceType = getSourceType(field);
				String dataType = dataTypeMapper.map(sourceType);
				if(dataType == null) {
					LOG.error(String.format("No TypeMapping for type [%s] found!", field.getString(TYPE)));
					return new Result(false, String.format("No TypeMapping for type [%s] found!", field.getString(TYPE)));
				}
				
				boolean suggest = field.has(SUGGEST) ? field.getBoolean(SUGGEST) : false;
				String fieldType = field.has(FIELD_TYPE) ? field.getString(FIELD_TYPE) : null;
				mappingBuilder.addField(key, dataType, suggest);
				if(fieldType != null) {
					metaType.getFields().add(new Field(key, metaType, suggest, fieldType));
				}
			}

			 mappingBuilder.endDocument();
			 mappingBuilder.applyMapping(config.getIndex(), config.getType());
		} catch (IOException e) {
			LOG.error("Error while creating mapping!", e);
			return new Result(false, "Error while creating mapping!");
		}
		
		metaTypeService.addType(metaType);
		
		return new Result(true, "Success");
	}
	
	private String getSourceType(JSONObject field) {
		String sourceType = field.getString(TYPE);
		if(sourceType.equals(ARRAY)) {
			JSONObject subField = field.getJSONObject(ITEMS);
			sourceType = getSourceType(subField);
		}
		return sourceType;
	}
}

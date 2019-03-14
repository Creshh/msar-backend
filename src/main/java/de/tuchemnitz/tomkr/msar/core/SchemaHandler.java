package de.tuchemnitz.tomkr.msar.core;

import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.Config;
import de.tuchemnitz.tomkr.msar.core.registry.DataTypeMapper;
import de.tuchemnitz.tomkr.msar.core.registry.MappingBuilder;
import de.tuchemnitz.tomkr.msar.core.registry.MetaTypeService;
import de.tuchemnitz.tomkr.msar.core.registry.types.Field;
import de.tuchemnitz.tomkr.msar.core.registry.types.MetaType;
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
	private MetaTypeService metaTypeService;
	
	@Autowired
	private MappingBuilder mappingBuilder;

	@Autowired
	private Validator validator;
	
	private static final String TYPE = "type";
	private static final String PROPERTIES = "properties";
	private static final String TAG = "tag";
	
	/**
	 * 
	 * 
	 * @param type
	 * @param schemaJSON
	 * @return
	 */
	public boolean registerSchema(String type, String schemaJSON) {
		if (metaTypeService.contains(type)) {
			LOG.error(String.format("Type [%s] already registered - abort registration.", type));
			return false;
		}

		JSONObject schemaRoot = JsonHelpers.loadJSON(schemaJSON);
		boolean valid = validator.checkDocument(metaTypeService.getMetaSchema(), schemaRoot);

		if (!valid) {
			LOG.error("Given schema is not valid!");
			return false;
		}

		MetaType metaType = new MetaType();
		metaType.setType(type);
		metaType.setSchema(schemaJSON);

		try {
			mappingBuilder.startDocument();
			JSONObject properties = schemaRoot.getJSONObject(PROPERTIES);
			for (String key : properties.keySet()) {
				JSONObject field = properties.getJSONObject(key);
				
				String dataType = dataTypeMapper.map(field.getString(TYPE));
				if(dataType == null) {
					LOG.error(String.format("No TypeMapping for type [%s] found!", field.getString(TYPE)));
					return false;
				}
				
				boolean searchable = field.has(TAG) ? field.getBoolean(TAG) : false;
				mappingBuilder.addField(key, dataType, searchable);
				if(searchable) {
					metaType.getFields().add(new Field(key, metaType));
				}
			}

			 mappingBuilder.endDocument();
			 mappingBuilder.applyMapping(config.getIndex(), config.getType());
		} catch (IOException e) {
			LOG.error("Error while creating mapping!", e);
			return false;
		}
		
		metaTypeService.addType(metaType);
		
		return true;
	}
}

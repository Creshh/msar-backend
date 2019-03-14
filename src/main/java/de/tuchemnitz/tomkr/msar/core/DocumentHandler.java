package de.tuchemnitz.tomkr.msar.core;

import java.util.Map;

import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.core.registry.MetaTypeService;
import de.tuchemnitz.tomkr.msar.elastic.DocumentFunctions;
import de.tuchemnitz.tomkr.msar.utils.JsonHelpers;

/**
 * 
 * @author Kretzschmar
 *
 */
@Service
public class DocumentHandler {

	private static Logger LOG = LoggerFactory.getLogger(DocumentHandler.class);

	@Autowired
	private MetaTypeService typeRegistry;

	@Autowired
	private DocumentFunctions docFunctions;
	
	@Autowired
	private Validator validator;

	public boolean addDocument(String json) {
		// read
		JSONObject docObj = JsonHelpers.loadJSON(json);
		Map<String, Object> doc = JsonHelpers.readJsonToMap(json);
		// validate
		String type = (String) doc.get("type");
		if (type == null) {
			LOG.error("No type defined!");
			return false;
		}
		Schema schema = typeRegistry.getSchema(type);
		if (schema == null) {
			LOG.error(String.format("No schema for type [%s] found, register schema first!", type));
			return false;
		}

		boolean valid = validator.checkDocument(schema, docObj);
		if (!valid) {
			LOG.error("Schema not valid, check errors!");
			return false;
		}

		// index
		docFunctions.indexDocument(doc);

		return true;
	}
	
	public boolean deleteDocument(String type, String reference) {
//		docFunctions.deleteDocument();
		return true;
	}
}

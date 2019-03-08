package de.tuchemnitz.tomkr.msar.reader;

import java.util.Map;

import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.search.DocumentService;
import de.tuchemnitz.tomkr.msar.search.TypeRegistry;

@Service
public class DocumentHandler {

	private static Logger LOG = LoggerFactory.getLogger(DocumentHandler.class);

	@Autowired
	private TypeRegistry typeRegistry;

	@Autowired
	private SchemaHandler schemaHandler;
	
	@Autowired
	private DocumentService documentService;
	
	public boolean addDocument(String json) {
		// read
		JSONObject docObj = JsonHelper.loadJSON(json);
		Map<String, Object> doc = JsonHelper.readJsonToMap(json);
		// validate
		String type = (String) doc.get("type");
		if(type == null) {
			LOG.error("No type defined!");
			return false;
		}
		Schema schema = typeRegistry.getSchema(type);
		if(schema == null) {
			LOG.error(String.format("No schema for type [%s] found, register schema first!", type));
			return false;
		}
		
		boolean valid = schemaHandler.validate(schema, docObj);
		if(!valid) {
			LOG.error("Schema not valid, check errors!");
			return false;
		}

		// index
		documentService.indexDocument(doc);
		
		return true;
	}
}

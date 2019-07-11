package de.tuchemnitz.tomkr.msar.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.everit.json.schema.Schema;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.core.registry.MetaTypeService;
import de.tuchemnitz.tomkr.msar.elastic.DocumentFunctions;
import de.tuchemnitz.tomkr.msar.elastic.QueryFunctions;
import de.tuchemnitz.tomkr.msar.utils.JsonHelpers;
import de.tuchemnitz.tomkr.msar.utils.Result;

/**
 * 
 * 
 * @author Tom Kretzschmar
 *
 */
@Service
public class DocumentHandler {

	private static Logger LOG = LoggerFactory.getLogger(DocumentHandler.class);

	@Autowired
	QueryFunctions queryFunctions;

	@Autowired
	private MetaTypeService typeRegistry;

	@Autowired
	private DocumentFunctions docFunctions;

	@Autowired
	private Validator validator;

	public Result addDocument(String json) {
		return addDocument(json, null);
	}

	public Result addDocument(String json, String reference) {
		// read
		JSONObject docObj = JsonHelpers.loadJSON(json);
		Map<String, Object> doc = JsonHelpers.readJsonToMap(json);
		// validate
		String type = (String) doc.get("type");
		if (type == null) {
			LOG.error("No type defined!");
			return new Result(false, "No type defined!");
		}
		Schema schema = typeRegistry.getSchema(type);
		if (schema == null) {
			LOG.error(String.format("No schema for type [%s] found, register schema first!", type));
			return new Result(false, String.format("No schema for type [%s] found, register schema first!", type));
		}

		Result validation = validator.checkDocument(schema, docObj);
		if (!validation.isSuccess()) {
			LOG.error("Validation failed!");
			return new Result(false, "Validation failed: " + validation.getMsg()); // TODO: add "additional" to result and return it. "Show more" in message box; hide message box only when closed manually
		}

		// check duplicate
		List<Map<String, Object>> docs = queryFunctions.getDocuments(reference, type);
		for(Map<String, Object> indexedDoc : docs) {
			if(indexedDoc.get("source").equals(doc.get("source"))) {
				return new Result(false, "Document of same type with same source already indexed - upload skipped!"); // maybe extract to different function "check duplicate" and ask user if it should be overwritten
			}
		}
		
		// reset reference
		if (reference != null) {
			doc.put("reference", reference);
		}

		// index
		docFunctions.indexDocument(doc);

		return new Result(true, null);
	}

	public boolean removeDocument(long reference) {
		List<String> docs = queryFunctions.getDocumentIds(String.valueOf(reference));
		docFunctions.removeDocuments(docs);
		return true;
	}

	public Map<String, Object> getDocuments(String reference) {
		Map<String, Object> result = new HashMap<>();
		List<String> types = typeRegistry.getAllTypeNames();
		for (String type : types) {
			result.put(type, queryFunctions.getDocuments(reference, type));
		}
		return result;
	}
}

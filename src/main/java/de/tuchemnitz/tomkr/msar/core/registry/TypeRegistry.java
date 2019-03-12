package de.tuchemnitz.tomkr.msar.core.registry;

import java.util.HashMap;
import java.util.Map;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.utils.JsonHelpers;

/**
 * maybe split data and register functions / should always be singleton bean;
 * maybe handle concurrent access of data fields
 * 
 * 
 * @author Kretzschmar
 *
 */
@Service
public class TypeRegistry {

	private static Logger LOG = LoggerFactory.getLogger(TypeRegistry.class);

	private static final String META_SCHEMA = "meta-schema-v7.json";

	private Map<String, Schema> schemaRegistry;

	private Schema metaSchema;

	public TypeRegistry() {
		schemaRegistry = new HashMap<>();
		JSONObject json = JsonHelpers.loadJSONFromResource(META_SCHEMA);
		if (json != null) {
			metaSchema = SchemaLoader.load(json);
		} else {
			LOG.error(String.format("MetaSchema [%s] could not be found!", META_SCHEMA));
		}
	}

	public void addSchema(String type, Schema schema) {
		schemaRegistry.put(type, schema);
	}

	public Schema getSchema(String type) {
		return schemaRegistry.get(type);
	}

	public boolean contains(String type) {
		return schemaRegistry.containsKey(type);
	}

	public Schema getMetaSchema() {
		return metaSchema;
	}
}

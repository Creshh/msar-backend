package de.tuchemnitz.tomkr.msar.core.registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.utils.JsonHelpers;

/**
 * maybe split data and register functions / should always be singleton bean;
 * maybe handle concurrent access of data fields
 * 
 * 
 * 
 * 
 * 
 * 
 * >>>>>> Split Schema and Field Registry in Separate parts; separate Constants also
 *  
 *  
 *  
 *  
 *  
 *  
 *  
 *  
 *  
 * put (all) constants in properties files, especially type mapping
 * 
 * @author Kretzschmar
 *
 */
@Service
public class TypeRegistry {

	private static Logger LOG = LoggerFactory.getLogger(TypeRegistry.class);

	private static final String META_SCHEMA = "meta-schema-v7.json";
	public static final String INDEX = "msar";
	public static final String TYPE = "meta";

	public static class SchemaFields {
		public static final String TITLE = "title";
		public static final String TYPE = "type";
		public static final String PROPERTIES = "properties";

		public static final String TAG = "tag";

		public static String[] getAllRequired() {
			return new String[] { PROPERTIES, TITLE, TYPE };
		}
	}

	public static class ElasticFields {
		public static final String PROPERTIES = "properties";
		public static final String FIELDS = "fields";
		public static final String TYPE = "type";
		public static final String COMPLETION = "completion";
	}

	private List<String> fieldRegistry;
	private Map<String, Schema> schemaRegistry;
	private XContentBuilder mappingBuilder;

	private Schema metaSchema;

	public TypeRegistry() {
		schemaRegistry = new HashMap<>();
		fieldRegistry = new ArrayList<>();
		JSONObject json = JsonHelpers.loadJSONFromResource(META_SCHEMA);
		if (json != null) {
			metaSchema = SchemaLoader.load(json);
		} else {
			LOG.error(String.format("MetaSchema [%s] could not be found!", META_SCHEMA));
		}
	}

	public void startDocument() throws IOException {
		mappingBuilder = XContentFactory.jsonBuilder();
		mappingBuilder.startObject();
		mappingBuilder.startObject(ElasticFields.PROPERTIES);
	}

	public void addField(String type, String field, String dataType, boolean searcheable) throws IOException {

		if (searcheable) {
			fieldRegistry.add(field);
		}

		mappingBuilder.startObject(field);
		mappingBuilder.field(ElasticFields.TYPE, dataType);
		if (searcheable) {
			mappingBuilder.startObject(ElasticFields.FIELDS);
			mappingBuilder.startObject(ElasticFields.COMPLETION);
			mappingBuilder.field(ElasticFields.TYPE, ElasticFields.COMPLETION);
			mappingBuilder.endObject();
			mappingBuilder.endObject();
		}
		mappingBuilder.endObject();
	}

	public XContentBuilder endDocument() throws IOException {
		mappingBuilder.endObject().endObject();
		return mappingBuilder;
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

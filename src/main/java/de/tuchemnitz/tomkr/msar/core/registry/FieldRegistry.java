package de.tuchemnitz.tomkr.msar.core.registry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Service;

/**
 * maybe split data and register functions / should always be singleton bean;
 * maybe handle concurrent access of data fields
 *  
 * @author Kretzschmar
 *
 */
@Service
public class FieldRegistry {

	private static final String PROPERTIES = "properties";
	private static final String FIELDS = "fields";
	private static final String TYPE = "type";
	private static final String COMPLETION = "completion";

	private Map<String,String> fieldRegistry;
	private XContentBuilder mappingBuilder;
	
	public FieldRegistry() {
		fieldRegistry = new HashMap<>();
	}

	public void startDocument() throws IOException {
		mappingBuilder = XContentFactory.jsonBuilder();
		mappingBuilder.startObject();
		mappingBuilder.startObject(PROPERTIES);
	}

	public void addField(String metaType, String field, String dataType, boolean searcheable) throws IOException {
		if (searcheable) {
			fieldRegistry.put(metaType, field);
		}

		mappingBuilder.startObject(field);
		mappingBuilder.field(TYPE, dataType);
		if (searcheable) {
			mappingBuilder.startObject(FIELDS);
			mappingBuilder.startObject(COMPLETION);
			mappingBuilder.field(TYPE, COMPLETION);
			mappingBuilder.endObject();
			mappingBuilder.endObject();
		}
		mappingBuilder.endObject();
	}

	public XContentBuilder endDocument() throws IOException {
		mappingBuilder.endObject().endObject();
		return mappingBuilder;
	}
	
	public List<String> getAll(){
		return new ArrayList<String>(fieldRegistry.values());
	}
}

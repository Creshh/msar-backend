package de.tuchemnitz.tomkr.msar.core.registry;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.elastic.IndexFunctions;

/**
 *  
 * @author Tom Kretzschmar
 *
 */
@Service
public class MappingBuilder {

	@Autowired
	private IndexFunctions indexService;
	
	private static final String PROPERTIES = "properties";
	private static final String FIELDS = "fields";
	private static final String TYPE = "type";
	private static final String COMPLETION = "completion";

	private XContentBuilder mappingBuilder;
	
	
	public void startDocument() throws IOException {
		mappingBuilder = XContentFactory.jsonBuilder();
		mappingBuilder.startObject();
		mappingBuilder.startObject(PROPERTIES);
	}

	public void addField(String field, String dataType, boolean searcheable) throws IOException {
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

	public void endDocument() throws IOException {
		mappingBuilder.endObject().endObject();
	}
	
	public void applyMapping(String index, String type) {
		indexService.createMapping(mappingBuilder, index, type);
	}
}

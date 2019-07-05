package de.tuchemnitz.tomkr.msar.core.registry;

import java.io.IOException;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.elastic.IndexFunctions;

/**
 * Builder class wrapping the elasticsearch {@link XContentBuilder} for generating and applying new mappings to the elasticsearch instance.  
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

	/**
	 * The elasticsearch builder instance.
	 */
	private XContentBuilder mappingBuilder;
	
	/**
	 * Start new elasticsearch mapping type.
	 * 
	 * @return The current MappingBuilder instance for further calls.
	 * @throws IOException
	 */
	public MappingBuilder startDocument() throws IOException {
		mappingBuilder = XContentFactory.jsonBuilder();
		mappingBuilder.startObject();
		mappingBuilder.startObject(PROPERTIES);
		return this;
	}

	/**
	 * Add new data field to the mapping.
	 * 
	 * @param field The field name.
	 * @param dataType The datatype of the field.
	 * @param searcheable Flag, if the field should be enabled for suggestion queries.
	 * @return The current MappingBuilder instance for further calls.
	 * @throws IOException
	 */
	public MappingBuilder addField(String field, String dataType, boolean searcheable) throws IOException {
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
		return this;
	}

	/**
	 * End the elasticsearch mapping type.
	 * @return The current MappingBuilder instance for further calls.
	 * @throws IOException
	 */
	public MappingBuilder endDocument() throws IOException {
		mappingBuilder.endObject().endObject();
		return this;
	}
	
	/**
	 * Apply the generated mapping type and register it in the elasticsearch instance.
	 * @param index The index, where the mapping should be registered in.
	 * @param type The type, where the mapping should be registered in.
	 */
	public void applyMapping(String index, String type) {
		indexService.createMapping(mappingBuilder, index, type);
	}
}

package de.tuchemnitz.tomkr.msar.core.registry;

import java.util.ArrayList;
import java.util.List;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.db.FieldRepository;
import de.tuchemnitz.tomkr.msar.db.MetaTypeRepository;
import de.tuchemnitz.tomkr.msar.db.types.MetaType;
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
public class MetaTypeService {

	public static final String TYPE_META_SCHEMA = "meta_schema";

	@Autowired
	private MetaTypeRepository metaTypeRepo;
	
	@Autowired
	private FieldRepository fieldRepo;

	public Schema getSchema(String type) {
		MetaType metaType = metaTypeRepo.findByName(type);
		return SchemaLoader.load(JsonHelpers.loadJSON(metaType.getSchema()));
	}
	
	public Schema getMetaSchema() {
		return getSchema(TYPE_META_SCHEMA);
	}
	
	public boolean contains(String type) {
		return (metaTypeRepo.findByName(type) != null);
	}

	public List<String> getAllFields() {
		List<String> result = new ArrayList<>();
		fieldRepo.findAll().forEach(e -> result.add(e.getName()));
		return result;
	}

	public void addType(MetaType metaType) {
		metaTypeRepo.save(metaType);
	}

	public void addMetaSchema(String json) {
		if(!contains(TYPE_META_SCHEMA)) {
			MetaType metaType = new MetaType();
			metaType.setType(TYPE_META_SCHEMA);
			metaType.setSchema(json);
			
			addType(metaType);
		}
	}
}

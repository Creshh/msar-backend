package de.tuchemnitz.tomkr.meta.input;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import de.tuchemnitz.tomkr.meta.model.MetaObject;

@Service
public class JSONAdapter {

	public MetaObject readJSON(String filePath){ 
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = null;
		try {
			root = mapper.readTree(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(root != null) {
			return traverseJSONTree(root);
		} else {
			return null;
		}
	}
	
	private MetaObject traverseJSONTree(JsonNode element){
		MetaObject current = new MetaObject();		
		
		// String, Number, Boolean and null values 
		if(element.isValueNode()) {
			current.setValue(getValue(element));
		} else if (element.isArray()) {
			int count = 0;
			if(element.size() > 0 && element.get(0).isContainerNode()) {
				for(Iterator<JsonNode> it = ((ArrayNode) element).elements(); it.hasNext(); ) {
					String key = String.format("listElement%05d", count);
					JsonNode value = it.next();
					MetaObject subResult = traverseJSONTree(value);
					current.getMetaData().put(key, subResult);
					count ++;
				}
			} else if (element.size() > 0){
				List<Object> values = new ArrayList<>();
				for(Iterator<JsonNode> it = ((ArrayNode) element).elements(); it.hasNext(); ) {
					JsonNode value = it.next();
					Object obj = getValue(value);
					values.add(obj);
				}
				current.setValue(values);
			} else {
				//handle null
				current.setValue(null);
			}
		} else if (element.isObject()){
			for(Iterator<Entry<String, JsonNode>> it = element.fields(); it.hasNext();) {
				Entry<String, JsonNode> subElement = it.next();
				String key = subElement.getKey();
				JsonNode value = subElement.getValue();
				
				MetaObject subResult = traverseJSONTree(value);
				current.getMetaData().put(key, subResult);
			}
		} else {
			// todo: error handling
		}
		return current;
	}
	
	private Object getValue(JsonNode element) {
		Object value = null;
		if(element.isNumber()) {
			if(element.isInt()) {
				value = element.asInt();
			} else if(element.isLong()) {
				value = element.asLong();
			} else if(element.isFloatingPointNumber()) {
				value = element.asDouble();
			}
		} else if(element.isTextual()) {
			value = element.asText();
		} else if(element.isBoolean()) {
			value = element.asBoolean();
		} else if(element.isNull()) {
			// handle Null
		}
		return value;
	}
	
}

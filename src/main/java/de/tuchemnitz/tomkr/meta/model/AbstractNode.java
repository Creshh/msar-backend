package de.tuchemnitz.tomkr.meta.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "meta")
public abstract class AbstractNode {

	
	private Map<String, MetaObject> metaData;
	
	public AbstractNode() {
		metaData = new HashMap<String, MetaObject>();
	}
	
	
	public MetaObject getValue(String [] path) {
		if(!this.metaData.isEmpty()) {
			MetaObject subNode = this.metaData.get(path[0]);
			if(path.length == 1) {
				return subNode;
			} else {
				return subNode.getValue(Arrays.copyOfRange(path, 1, path.length));
			}
		}
		return null;
	}
	
	public MetaObject getOrCreateValuePath(String [] path) {
		MetaObject subNode = null;
		if(!this.metaData.isEmpty() && this.metaData.containsKey(path[0])) {
			subNode = this.metaData.get(path[0]);
		} else {
			subNode = new MetaObject();
			this.metaData.put(path[0], subNode);
		}
		if(path.length == 1) {
			return subNode;
		} else {
			return subNode.getOrCreateValuePath(Arrays.copyOfRange(path, 1, path.length));
		}
	}
	
	public void addValue(String [] path, Object value) {
		MetaObject subNode = getOrCreateValuePath(path);
		subNode.setValue(value);
	}


	public Map<String, MetaObject> getMetaData() {
		return metaData;
	}


	public void setMetaData(Map<String, MetaObject> metaData) {
		this.metaData = metaData;
	}
	
}

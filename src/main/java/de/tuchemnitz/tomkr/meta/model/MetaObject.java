package de.tuchemnitz.tomkr.meta.model;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "meta")
public class MetaObject extends AbstractNode{

		@Field(store = true, copyTo = "allFields")
		private Object value;
		
		
		public MetaObject() {
			super();
		}
		
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		
		// specify schema / keys which should be handled as tags; extract the value from these keys to a tag field in the asset; for filter mechanism, provide an overview about the 
		// available keys and let choose
}

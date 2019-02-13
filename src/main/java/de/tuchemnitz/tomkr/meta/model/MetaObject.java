package de.tuchemnitz.tomkr.meta.model;

import java.lang.reflect.Type;

public class MetaObject extends AbstractNode{

		private Type type;
		private Object value;
		
		
		public MetaObject() {
			super();
		}
		
		public Type getType() {
			return type;
		}
		public void setType(Type type) {
			this.type = type;
		}
		public Object getValue() {
			return value;
		}
		public void setValue(Object value) {
			this.value = value;
		}
		
		
}

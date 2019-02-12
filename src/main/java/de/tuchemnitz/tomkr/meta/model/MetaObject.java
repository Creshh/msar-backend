package de.tuchemnitz.tomkr.meta.model;

import lombok.Data;

@Data
public class MetaObject <K extends Comparable<K>, V>{
	
		private MetaObject<K, V> child;
		private MetaObject<K, V> parent;
		private boolean color;

		private K key;
		private V value;

}

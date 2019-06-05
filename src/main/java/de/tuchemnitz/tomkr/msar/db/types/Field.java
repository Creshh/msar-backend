package de.tuchemnitz.tomkr.msar.db.types;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Field {

	public static final String DATA_RANGE = "range";
	public static final String DATA_DATE = "date";
	public static final String DATA_EXACT = "exact";
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String name;
	
	private boolean suggest;
	
	private String datatype;

	@ManyToOne
	@JsonBackReference
	private MetaType type;
	
	public Field() {}
	
	public Field(String name, MetaType type, boolean suggest, String datatype) {
		this.name = name;
		this.type = type;
		this.suggest = suggest;
		this.datatype = datatype;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MetaType getType() {
		return type;
	}

	public void setType(MetaType type) {
		this.type = type;
	}

	public boolean isSuggest() {
		return suggest;
	}

	public void setSuggest(boolean suggest) {
		this.suggest = suggest;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
}

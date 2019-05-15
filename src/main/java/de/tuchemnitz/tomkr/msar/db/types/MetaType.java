package de.tuchemnitz.tomkr.msar.db.types;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class MetaType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String name;

	// Maybe use JPAConverter https://stackoverflow.com/questions/25738569/jpa-map-json-column-to-java-object
	@Column(name="jsonSchema",columnDefinition="LONGTEXT")
	private String jsonSchema;

	@OneToMany(mappedBy = "type", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Field> fields;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setType(String type) {
		this.name = type;
	}

	public String getSchema() {
		return jsonSchema;
	}

	public void setSchema(String schema) {
		this.jsonSchema = schema;
	}

	public List<Field> getFields() {
		if (fields == null) {
			fields = new ArrayList<>();
		}
		return fields;
	}
}

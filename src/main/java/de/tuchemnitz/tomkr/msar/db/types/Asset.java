package de.tuchemnitz.tomkr.msar.db.types;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Asset {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String originalFileName;
	private String dataType;

	public Asset() {}

	public Asset(String originalFileName, String dataType) {
		this.originalFileName = originalFileName;
		this.dataType = dataType;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public long getId() {
		return id;
	}
}

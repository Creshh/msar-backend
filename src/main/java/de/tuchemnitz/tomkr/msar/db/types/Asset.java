package de.tuchemnitz.tomkr.msar.db.types;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Asset {

	@Id
	private String identifier;
	
	private String originalFileName;
	private String dataType;

	public Asset() {}

	public Asset(String identifier, String originalFileName, String dataType) {
		this.identifier = identifier;
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

	public String getIdentifier() {
		return identifier;
	}
}

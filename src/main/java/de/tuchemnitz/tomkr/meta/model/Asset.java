package de.tuchemnitz.tomkr.meta.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "meta", type = "Asset")
public class Asset extends AbstractNode{

	@Id
	private String reference;
	@CreatedDate
	private long cts;
	@LastModifiedDate
	private long mts;
	

	public Asset(){
		super();
	}
	
	public Asset(String reference) {
		super();
		this.reference = reference;
	}


	public String getReference() {
		return reference;
	}


	public void setReference(String reference) {
		this.reference = reference;
	}


	public long getCts() {
		return cts;
	}


	public void setCts(long cts) {
		this.cts = cts;
	}


	public long getMts() {
		return mts;
	}


	public void setMts(long mts) {
		this.mts = mts;
	}
	
}

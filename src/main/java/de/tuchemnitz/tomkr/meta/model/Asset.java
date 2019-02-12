package de.tuchemnitz.tomkr.meta.model;

import java.util.List;

import lombok.Data;

@Data
public class Asset {

	private String reference;
	private long cts;
	private long mts;
	
	private List<MetaObject> metaData;

		
}

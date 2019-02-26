package de.tuchemnitz.tomkr.metaapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "metaapp", type = "metafile")
public class MetaFile {
	
	@Id
	private String fileName;
	private String location;
}
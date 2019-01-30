package de.tuchemnitz.tomkr.metaapp.es;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import graphql.annotations.GraphQLDescription;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@GraphQLName("metafile")
@Document(indexName = "metaapp", type = "metafile")
public class MetaFile {
	
	@GraphQLField @Id
	@GraphQLDescription("The fileName; used as identifier")
	private String fileName;

	@GraphQLField
	private String location;
}
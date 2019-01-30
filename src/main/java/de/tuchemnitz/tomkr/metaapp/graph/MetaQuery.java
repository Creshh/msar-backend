package de.tuchemnitz.tomkr.metaapp.graph;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import de.tuchemnitz.tomkr.metaapp.es.MetaFile;
import graphql.annotations.GraphQLDescription;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;

//https://github.com/Enigmatis/graphql-java-annotations

@GraphQLName("query")
public class MetaQuery{

    @Autowired
    private MetaFileCollection metaFileCollection;

    public MetaQuery(){}

    @GraphQLField
    @GraphQLDescription("get All files")
    public List<MetaFile> getAll() {
        return metaFileCollection.metaFiles;
    }


}
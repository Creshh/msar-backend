package de.tuchemnitz.tomkr.metaapp.graph;

import org.springframework.beans.factory.annotation.Autowired;

import de.tuchemnitz.tomkr.metaapp.es.MetaFile;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;

//https://github.com/Enigmatis/graphql-java-annotations
//https://nwillc.wordpress.com/2016/10/17/graphql-java-schema-annotations/

@GraphQLName("mutation")
public class MetaMutation{

    @Autowired
    private MetaFileCollection metaFileCollection;

    @GraphQLField
    public void create(String fileName, String location) {
       metaFileCollection.metaFiles.add(new MetaFile(fileName, location));
    }


}
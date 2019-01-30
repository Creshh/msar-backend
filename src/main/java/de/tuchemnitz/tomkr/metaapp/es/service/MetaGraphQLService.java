package de.tuchemnitz.tomkr.metaapp.es.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.metaapp.model.MetaFile;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotation.GraphQLApi;

// https://github.com/Enigmatis/graphql-java-annotations
// https://github.com/leangen/graphql-spqr-spring-boot-starter
// https://github.com/leangen/graphql-spqr

@Service
@GraphQLApi
public class MetaGraphQLService{

    @Autowired
    private MetaFileCollection metaFileCollection;

    @GraphQLQuery(name = "getAll", description = "get all MetaFiles")
    public List<MetaFile> getAll() {
        return metaFileCollection.metaFiles;
    }

}
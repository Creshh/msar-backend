package de.tuchemnitz.tomkr.metaapp.graph;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.metaapp.es.MetaFile;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotation.GraphQLApi;

//https://github.com/Enigmatis/graphql-java-annotations
//https://github.com/leangen/graphql-spqr-spring-boot-starter

@Service
@GraphQLApi
public class MetaGraphQLService{

    @Autowired
    private MetaFileCollection metaFileCollection;

    @GraphQLQuery
    public List<MetaFile> getAll() {
        return metaFileCollection.metaFiles;
    }

}
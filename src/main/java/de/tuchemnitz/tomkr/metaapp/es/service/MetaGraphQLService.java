package de.tuchemnitz.tomkr.metaapp.es.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.metaapp.model.MetaFile;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotation.GraphQLApi;

// https://github.com/Enigmatis/graphql-java-annotations
// https://github.com/leangen/graphql-spqr-spring-boot-starter
// https://github.com/leangen/graphql-spqr

@Service
@GraphQLApi
public class MetaGraphQLService{

    // @Autowired
    // private MetaFileCollection metaFileCollection;

    @Autowired
    MetaElasticService metaElasticService; 

    @GraphQLQuery(name = "getAll", description = "get all MetaFiles")
    public List<MetaFile> getAll() {
        // return metaFileCollection.metaFiles;
        List<MetaFile> result = new ArrayList<>();
        metaElasticService.findAll().forEach(result::add);
        return result;
    }

    @GraphQLMutation(name = "addMetaData", description = "Add new MetaData")
    public boolean addData(String fileName, @GraphQLArgument(name = "location", defaultValue = "") String location) {
        MetaFile f = new MetaFile(fileName, location);
        metaElasticService.save(f);
        return true;
    }

}
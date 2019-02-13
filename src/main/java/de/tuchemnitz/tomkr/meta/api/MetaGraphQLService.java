package de.tuchemnitz.tomkr.meta.api;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.meta.search.MetaRetrievalService;
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
    MetaRetrievalService metaRetrievalService; 

    @GraphQLQuery(name = "getAllMeta", description = "get all MetaFiles")
    public List<String> getAll() {
        // return metaFileCollection.metaFiles;
//        List<MetaFile> result = new ArrayList<>();
//        metaElasticService.findAll().forEach(result::add);
        return Arrays.asList("a", "b");
    }

//    @GraphQLMutation(name = "addMetaData", description = "Add new MetaData")
//    public boolean addData(String fileName, @GraphQLArgument(name = "location", defaultValue = "") String location) {
//        MetaFile f = new MetaFile(fileName, location);
//        metaElasticService.save(f);
//        return true;
//    }

}
package de.tuchemnitz.tomkr.metaapp.service.graphql;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.metaapp.model.MetaFile;
import de.tuchemnitz.tomkr.metaapp.service.es.MetaElasticService;
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
        List<MetaFile> result = new ArrayList<>();
        metaElasticService.findAll().forEach(result::add);
        return result;
    }
    
    @GraphQLQuery(name = "getByLocFuzzy", description = "get MetaFiles fuzzy by location")
    public List<MetaFile> getLocationFuzzy(String location) {
        List<MetaFile> result = new ArrayList<>();
		Page<MetaFile> page = metaElasticService.findByLocationFuzzy(location,"AUTO", PageRequest.of(0, 10));
		page.forEach(result::add);
        return result;
    }

    @GraphQLMutation(name = "addMetaData", description = "Add new MetaData")
    public boolean addData(String fileName, @GraphQLArgument(name = "location", defaultValue = "") String location) {
        MetaFile f = new MetaFile(fileName, location);
        metaElasticService.save(f);
        return true;
    }

}
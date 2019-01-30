package de.tuchemnitz.tomkr.metaapp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.tuchemnitz.tomkr.metaapp.graph.MetaSchema;
import graphql.ExecutionResult;
import graphql.GraphQL;



@RestController
public class RestControllerImpl {
	private static Logger LOG = LoggerFactory.getLogger(RestControllerImpl.class);



    private GraphQL graphQL;
	
    @Autowired
    RestControllerImpl(MetaSchema metaSchema) throws IOException {
		LOG.debug("RestController constructor");
        graphQL = GraphQL.newGraphQL(metaSchema.getSchema()).build();
    }

    @PostMapping(value = "/graphql")
    public ResponseEntity query(@RequestBody String query){
        ExecutionResult result = graphQL.execute(query);
        LOG.error(result.getErrors().toString());
        return ResponseEntity.ok(result.getData());
    }





	@RequestMapping("/")
	public String index() {
		LOG.debug("Index");
		return "Index";
	}
}

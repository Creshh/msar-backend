package de.tuchemnitz.tomkr.metaapp.graph;

import graphql.schema.GraphQLSchema;
import static graphql.schema.GraphQLSchema.newSchema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import graphql.annotations.GraphQLAnnotations;

@Component
public class MetaSchema{

    private static Logger LOG = LoggerFactory.getLogger(MetaSchema.class);

    private final GraphQLSchema schema;

    public MetaSchema() throws IllegalAccessException, NoSuchMethodException, InstantiationException {
        LOG.debug("MetaSchema constructor");
        schema = newSchema().query(GraphQLAnnotations.object(MetaQuery.class))
            .mutation(GraphQLAnnotations.object(MetaMutation.class))
            .build();
    }

    public GraphQLSchema getSchema() {
        LOG.debug("get Schema");
        return schema;
    }
}
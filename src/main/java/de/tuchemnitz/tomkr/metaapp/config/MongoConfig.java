package de.tuchemnitz.tomkr.metaapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "de.tuchemnitz.tomkr.metaapp.service.mongo")
public class MongoConfig {

}

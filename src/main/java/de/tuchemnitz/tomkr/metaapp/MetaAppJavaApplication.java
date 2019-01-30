package de.tuchemnitz.tomkr.metaapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class MetaAppJavaApplication {

	private static Logger LOG = LoggerFactory.getLogger(MetaAppJavaApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(MetaAppJavaApplication.class, args);
		LOG.debug("Started Application");
	}
}


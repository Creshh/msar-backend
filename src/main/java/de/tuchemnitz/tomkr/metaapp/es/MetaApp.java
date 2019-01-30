package de.tuchemnitz.tomkr.metaapp.es;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class MetaApp {

	private static Logger LOG = LoggerFactory.getLogger(MetaApp.class);
	
	public static void main(String[] args) {
		SpringApplication.run(MetaApp.class, args);
		LOG.debug("Started Application");
	}
}


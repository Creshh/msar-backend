package de.tuchemnitz.tomkr.msar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * 
 * @author Kretzschmar
 *
 */
@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class MetaRetrievalApplication {

	private static Logger LOG = LoggerFactory.getLogger(MetaRetrievalApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(MetaRetrievalApplication.class, args);
		LOG.debug("Started Application");
		
		// TODO: ensure Index etc.. make startup function
	}
}


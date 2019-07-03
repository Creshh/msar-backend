package de.tuchemnitz.tomkr.msar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main application starter class.
 * 
 * @author Tom Kretzschmar
 *
 */
@SpringBootApplication()
public class MetaRetrievalApplication {

	private static Logger LOG = LoggerFactory.getLogger(MetaRetrievalApplication.class);
	
	/**
	 * Starter function for the application.
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(MetaRetrievalApplication.class, args);
		LOG.debug("---------- MSAR started ----------");
	}
}


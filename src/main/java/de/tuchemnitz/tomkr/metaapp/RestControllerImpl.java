package de.tuchemnitz.tomkr.metaapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class RestControllerImpl {
	private static Logger LOG = LoggerFactory.getLogger(RestControllerImpl.class);



	@RequestMapping("/")
	public String index() {
		LOG.debug("Index");
		return "Index";
	}
}

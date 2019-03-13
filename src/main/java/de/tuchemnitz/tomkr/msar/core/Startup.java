package de.tuchemnitz.tomkr.msar.core;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.Config;
import de.tuchemnitz.tomkr.msar.elastic.IndexFunctions;

@Service
public class Startup {

	@Autowired
	IndexFunctions indexFunctions;
	
	@Autowired
	Config config;
	
	@PostConstruct
	public void ensureIndex() {
		indexFunctions.ensureIndex(config.getIndex());
	}
}

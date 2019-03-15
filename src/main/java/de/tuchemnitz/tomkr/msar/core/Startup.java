package de.tuchemnitz.tomkr.msar.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.Config;
import de.tuchemnitz.tomkr.msar.core.registry.MetaTypeService;
import de.tuchemnitz.tomkr.msar.elastic.IndexFunctions;
import de.tuchemnitz.tomkr.msar.utils.Helpers;

@Service
public class Startup {

	private static Logger LOG = LoggerFactory.getLogger(Startup.class);
	
	@Autowired
	IndexFunctions indexFunctions;
	
	@Autowired
	MetaTypeService typeService;
	
	@Autowired
	Config config;
	
	@EventListener(ApplicationReadyEvent.class)
	private void startup() {
		ensureIndex();
		loadMetaSchema();
	}
	
	private void ensureIndex() {
		indexFunctions.ensureIndex(config.getIndex());
	}
	
	private void loadMetaSchema() {
		String json = Helpers.readResource(config.getMetaSchemaRes());
		
		if (json != null) {
			typeService.addMetaSchema(json);
		} else {
			LOG.error(String.format("MetaSchema [%s] could not be found!", config.getMetaSchemaRes()));
		}
		
	}
}

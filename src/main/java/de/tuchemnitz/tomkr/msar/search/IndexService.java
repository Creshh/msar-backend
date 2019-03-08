package de.tuchemnitz.tomkr.msar.search;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.Config;

/**
 * 
 * @author Tom Kretzschmar
 *
 */
@Service
public class IndexService {

	private static Logger LOG = LoggerFactory.getLogger(IndexService.class);

	@Autowired
	Config config;

	public void deleteIndex(String index) {
		DeleteIndexResponse response = config.getClient().admin().indices().delete(new DeleteIndexRequest(index))
				.actionGet();
		LOG.debug(response.toString());
	}

	public void createMapping(XContentBuilder mappingBuilder, String index, String type) {
		PutMappingResponse response = config.getClient().admin().indices().preparePutMapping(index).setType(type)
				.setSource(mappingBuilder).execute().actionGet();
		LOG.debug(response.toString());
	}
}

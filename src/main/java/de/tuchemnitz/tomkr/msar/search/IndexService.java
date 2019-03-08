package de.tuchemnitz.tomkr.msar.search;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
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

	public boolean checkIndex(String index) {
		IndicesExistsResponse response = config.getClient().admin().indices().exists(new IndicesExistsRequest(index))
				.actionGet();
		return response.isExists();
	}

	public void ensureIndex(String index) {
		boolean exists = checkIndex(index);
		if (!exists) {
			config.getClient().admin().indices().create(new CreateIndexRequest(index)).actionGet();
		}
		LOG.debug(String.format("EnsureIndex [%s] -> %s", index, exists ? "exists" : "created"));
	}

	public void deleteIndex(String index) {
		boolean exists = checkIndex(index);
		if (exists) {
			config.getClient().admin().indices().delete(new DeleteIndexRequest(index)).actionGet();
		}
		LOG.debug(String.format("DeleteIndex [%s] -> %s", index, exists ? "deleted" : "doesn't exist"));
	}

	public void createMapping(XContentBuilder mappingBuilder, String index, String type) {
		ensureIndex(index);
		PutMappingResponse response = config.getClient().admin().indices().preparePutMapping(index).setType(type)
				.setSource(mappingBuilder).execute().actionGet();
		LOG.debug(response.toString());
	}
}

package de.tuchemnitz.tomkr.msar.elastic;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 
 * @author Tom Kretzschmar
 *
 */
@Repository
public class IndexFunctions {

	private static Logger LOG = LoggerFactory.getLogger(IndexFunctions.class);

	@Autowired
	Client client;

	public boolean checkIndex(String index) {
		IndicesExistsResponse response = client.admin().indices().exists(new IndicesExistsRequest(index)).actionGet();
		return response.isExists();
	}

	public void ensureIndex(String index) {
		boolean exists = checkIndex(index);
		if (!exists) {
			client.admin().indices().create(new CreateIndexRequest(index)).actionGet();
		}
		LOG.debug(String.format("EnsureIndex [%s] -> %s", index, exists ? "exists" : "created"));
	}

	public void deleteIndex(String index) {
		boolean exists = checkIndex(index);
		if (exists) {
			client.admin().indices().delete(new DeleteIndexRequest(index)).actionGet();
		}
		LOG.debug(String.format("DeleteIndex [%s] -> %s", index, exists ? "deleted" : "doesn't exist"));
	}

	public void createMapping(XContentBuilder mappingBuilder, String index, String type) {
		ensureIndex(index);
		PutMappingResponse response = client.admin().indices().preparePutMapping(index).setType(type)
				.setSource(mappingBuilder).execute().actionGet();
		LOG.debug(response.toString());
	}
}

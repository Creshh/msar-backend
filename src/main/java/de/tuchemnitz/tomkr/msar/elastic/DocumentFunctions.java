package de.tuchemnitz.tomkr.msar.elastic;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
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
public class DocumentFunctions {

	private static Logger LOG = LoggerFactory.getLogger(DocumentFunctions.class);

	private static final String INDEX = "msar";
	public static final String TYPE = "meta";

	@Autowired
	Client client;

	public void indexDocument(Map<String, Object> doc) {
		indexDocument(doc, INDEX);
	}

	public void indexDocument(Map<String, Object> doc, String index) {
		IndexResponse response = client.prepareIndex(index, TYPE).setSource(doc).execute().actionGet();
		LOG.debug(response.toString());
	}

	public boolean removeDocuments(List<String> docs) {
		BulkRequestBuilder builder = client.prepareBulk();
		for(String id : docs) {
			builder.add(new DeleteRequest(INDEX, TYPE, id));
		}
		BulkResponse response = null;
		try {
			response = builder.execute().get();
		} catch (InterruptedException | ExecutionException e) {
			LOG.error("Error deleting documents", e);
		}
//		LOG.debug(response.toString());
		return true;
	}
}

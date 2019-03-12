package de.tuchemnitz.tomkr.msar.elastic;

import java.util.Map;

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
}

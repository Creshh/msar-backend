package de.tuchemnitz.tomkr.msar.search;

import java.util.Map;

import org.elasticsearch.action.index.IndexResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.Config;

/**
 * Make Json Schema where fields which should be searched directly are annotated
 * with e.g. "tag". Schema has to be submitted beforehand -> update elastic
 * mapping at this point maybe different indices needed for different json types
 * maybe use MultiSearchAPI for concatenation of queries; or use setPostFilter
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search-msearch.html
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-search.html
 * 
 * @author Tom Kretzschmar
 *
 */
@Service
public class DocumentService {

	private static Logger LOG = LoggerFactory.getLogger(DocumentService.class);

	private static final String INDEX = "msar";
	public static final String TYPE = "meta";

	@Autowired
	Config config;


	public void indexDocument(Map<String, Object> doc) {
		indexDocument(doc, INDEX);
	}

	public void indexDocument(Map<String, Object> doc, String index) {
		IndexResponse response = config.getClient().prepareIndex(index, TYPE).setSource(doc).execute().actionGet();
		LOG.debug(response.toString());
	}
}

package de.tuchemnitz.tomkr.msar.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion.Entry.Option;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
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
public class ElasticService {

	private static Logger LOG = LoggerFactory.getLogger(ElasticService.class);

	private static final String INDEX = "msar";
	private static final String TYPE = "meta";
	private static final String FIELD_REFERENCE = "reference";
	private static final String SUGGEST_FORMAT = "%s_suggest";

	@Autowired
	Config config;

	public void deleteIndex() {
		DeleteIndexResponse deleteResponse = config.getClient().admin().indices().delete(new DeleteIndexRequest(INDEX))
				.actionGet();
		LOG.debug(deleteResponse.toString());
	}

	public void indexDocument(Map<String, Object> doc) {
		indexDocument(doc, INDEX);
	}

	public void indexDocument(Map<String, Object> doc, String index) {
		IndexResponse response = config.getClient().prepareIndex(index, TYPE).setSource(doc).execute().actionGet();
		LOG.debug(response.toString());
	}

	public void createMapping() {
		createMapping(INDEX);
	}

	public void createMapping(String index) {
		try {

			XContentBuilder mappingBuilder = XContentFactory.jsonBuilder().startObject().startObject(TYPE)
					.startObject("properties").startObject("city").field("type", "text").startObject("fields")
					.startObject("completion").field("type", "completion").endObject().endObject().endObject()
					.endObject().endObject().endObject();

			XContentBuilder mappingBuilder2 = XContentFactory.jsonBuilder().startObject().startObject(TYPE)
					.startObject("properties").startObject("objects").field("type", "text").startObject("fields")
					.startObject("completion").field("type", "completion").endObject().endObject().endObject()
					.endObject().endObject().endObject();

			config.getClient().admin().indices().preparePutMapping(index).setType(TYPE).setSource(mappingBuilder)
					.execute().actionGet();
			config.getClient().admin().indices().preparePutMapping(index).setType(TYPE).setSource(mappingBuilder2)
					.execute().actionGet();

		} catch (IOException e) {
			LOG.error("Error while modifying mapping", e);
		}
	}

	private List<Map<String, Object>> search(QueryBuilder queryBuilder, String... indices) {
		List<Map<String, Object>> results = new ArrayList<>();
		SearchResponse response = config.getClient().prepareSearch(indices != null ? indices : new String[] { INDEX })
				.setQuery(queryBuilder).get();
		for (SearchHit hit : response.getHits()) {

			Map<String, Object> result = hit.getSourceAsMap();
			LOG.debug("> result: " + result.get(FIELD_REFERENCE) + " [" + hit.getId() + "]");
			results.add(result);
		}
		return results;
	}

//	public List<Map<String, Object>> searchAll(String... indices) {
//		LOG.debug(String.format(">> Search all fields"));
//		return search(QueryBuilders.matchAllQuery(), indices);
//	}

	public List<Map<String, Object>> matchByValue(String value, String field, String... indices) {
		LOG.debug(String.format("Search of [%s] in field [%s]", value, field));
		return search(QueryBuilders.matchQuery(field, value), indices);
	}

	public List<Map<String, Object>> matchByRange(long lower, long upper, String field, String... indices) {
		LOG.debug(String.format("Search of [%d - %d] in field [%s]", lower, upper, field));
		return search(QueryBuilders.rangeQuery(field).from(lower, true).to(upper, true), indices);
	}

	public List<Map<String, Object>> searchByValue(String value, String... indices) {
		LOG.debug(String.format("Search of [%s] in all fields", value));
		return search(QueryBuilders.queryStringQuery(value), indices);
	}

	public Map<String, Map<String, Object>> getSuggestions(String value) {
		String[] fields = new String[] { "city.completion" }; // TODO: get all suggestion fields from field registry
		return getSuggestions(value, fields);
	}

	public Map<String, Map<String, Object>> getSuggestions(String value, String... fields) {
		Map<String, Map<String, Object>> result = new HashMap<>();

		SuggestBuilder builder = new SuggestBuilder();
		for (String field : fields) {
			CompletionSuggestionBuilder completionSuggestBuilder = SuggestBuilders.completionSuggestion(field);
			completionSuggestBuilder.skipDuplicates(true); // when skipDuclicates = false, suggestions will be
															// duplicated for example in case of arrays with multiple
															// entries
			completionSuggestBuilder.prefix(value, Fuzziness.AUTO);
			builder.addSuggestion(String.format(SUGGEST_FORMAT, field), completionSuggestBuilder);
		}

		SearchResponse response = config.getClient().prepareSearch(INDEX).setQuery(QueryBuilders.matchAllQuery())
				.suggest(builder).execute().actionGet();
		Suggest suggest = response.getSuggest();

		for (String field : fields) {
			CompletionSuggestion fieldSuggestion = suggest.getSuggestion(String.format(SUGGEST_FORMAT, field));
			LOG.debug(String.format("Suggestion queried for [%s] in field [%s]", value, field));
			for (CompletionSuggestion.Entry entry : fieldSuggestion.getEntries()) {
				for (Option option : entry.getOptions()) {
					String suggestion = option.getText().string();
					Map<String, Object> doc = option.getHit().getSourceAsMap();
					LOG.debug("> suggestion: " + suggestion + " for Doc: " + doc.get(FIELD_REFERENCE) + " ["
							+ option.getHit().getId() + "]");

					result.put(suggestion, doc);
				}
			}
		}
		return result;
	}

}

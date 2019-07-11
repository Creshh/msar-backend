package de.tuchemnitz.tomkr.msar.elastic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
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
import org.springframework.stereotype.Repository;

import de.tuchemnitz.tomkr.msar.Config;
import de.tuchemnitz.tomkr.msar.api.data.SuggestCategory;
import de.tuchemnitz.tomkr.msar.api.data.SuggestItem;
import de.tuchemnitz.tomkr.msar.core.registry.MetaTypeService;
import de.tuchemnitz.tomkr.msar.utils.JsonHelpers;

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
@Repository
public class QueryFunctions {

	private static Logger LOG = LoggerFactory.getLogger(QueryFunctions.class);

	private static final String FIELD_REFERENCE = "reference";
	private static final String FIELD_TYPE = "type";
	private static final String SUGGEST_FORMAT = "%s_suggest";

	@Autowired
	MetaTypeService typeService;
	
	@Autowired
	Config config;
	
	@Autowired
	Client client;

	// workaround: do first query, get result ( if one ) and get references
	// do multi query and add this references to all queries. -> will be "OR" query
	// check if each query has at least one result for each reference
	// -> add this reference to results --> will be "AND" query
	
	// -- or --
	
	// use nested documents
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> searchMultiple(String queryJson, String... indices){
		
		Map<String, List<Map<String, Object>>> results = new HashMap<>();

		Map<String, Object> query = JsonHelpers.readJsonToMap(queryJson);
		boolean first = true;
		for(Entry<String, Object> entry : query.entrySet()) {
			Map<String, Object> value = (Map<String, Object>) entry.getValue();
			
			boolean must = (boolean) value.get("add");			
			String lower = (String) value.get("lower");
			String field = (String) value.get("field");
			String upper = value.containsKey("upper") && !value.get("upper").equals("") ? (String) value.get("upper") : null;
			
			QueryBuilder builder;
			if(field.equals("query")) {
				builder = QueryBuilders.queryStringQuery(lower);
			} else if(upper != null) {
				builder = QueryBuilders.rangeQuery(field).from(lower, true).to(upper, true);
			} else {
				builder = QueryBuilders.matchQuery(field, lower);
			}
			
			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
			if(must) {
				boolQuery.must(builder);
			} else {
				boolQuery.must(QueryBuilders.existsQuery(field));
				boolQuery.mustNot(builder);
			}
			
			SearchResponse response = client.prepareSearch(indices != null ? indices : new String[] {config.getIndex()}).setQuery(boolQuery).setSize(config.getScrollSize()).get();
			List<String> currentReferences = new ArrayList<>();
		
			
			for (SearchHit hit : response.getHits()) {
				Map<String, Object> result = hit.getSourceAsMap();
				String reference = (String) result.get(FIELD_REFERENCE);
				if(first) {
					results.put(reference, new ArrayList<>());
					results.get(reference).add(result);
				}
				
				currentReferences.add(reference);
			}
			
			results.keySet().retainAll(currentReferences);
			
			if(results.isEmpty()) {
				return new ArrayList<>();
			}
			first = false;
		}
		
		return results.values().stream().collect(ArrayList::new, List::addAll, List::addAll);
	}
	
	private List<Map<String, Object>> search(QueryBuilder queryBuilder, String... indices) {
		List<Map<String, Object>> results = new ArrayList<>();
		SearchResponse response = client.prepareSearch(indices != null ? indices : new String[] {config.getIndex()})
				.setQuery(queryBuilder).get();
		for (SearchHit hit : response.getHits()) {

			Map<String, Object> result = hit.getSourceAsMap();
			LOG.debug("> result: " + result.get(FIELD_REFERENCE) + " [" + hit.getId() + "]");
			results.add(result);
		}
		return results;
	}

	public List<Map<String, Object>> matchByValue(String value, String field, String... indices) {
		LOG.debug(String.format("Search of [%s] in field [%s]", value, field));
		return search(QueryBuilders.matchQuery(field, value), indices);
	}

	public List<Map<String, Object>> matchByRange(double lower, double upper, String field, String... indices) {
		LOG.debug(String.format("Search of [%s - %s] in field [%s]", String.valueOf(lower), String.valueOf(upper), field));
		return search(QueryBuilders.rangeQuery(field).from(lower, true).to(upper, true), indices);
	}

	public List<Map<String, Object>> searchByValue(String value, String... indices) {
		LOG.debug(String.format("Search of [%s] in all fields", value));
		return search(QueryBuilders.queryStringQuery(value), indices);
	}
	
	public List<String> getDocumentIds(String reference){
		List<String> results = new ArrayList<>();
		SearchResponse response = client.prepareSearch(new String[] {config.getIndex()})
				.setQuery(QueryBuilders.termQuery(FIELD_REFERENCE, reference)).get();
		for (SearchHit hit : response.getHits()) {
			results.add(hit.getId());
			LOG.debug("> result: " + hit.getSourceAsMap().get(FIELD_REFERENCE) + " [" + hit.getId() + "]");
		}
		return results;
	}
	
	public List<Map<String, Object>> getDocuments(String reference, String type){
		List<Map<String, Object>> results = search(QueryBuilders.boolQuery()
				.must(QueryBuilders.termQuery(FIELD_REFERENCE, reference))
				.must(QueryBuilders.termQuery(FIELD_TYPE, type)));
		
		if(results != null) {
			return results;
		} else {
			return new ArrayList<Map<String,Object>>();
		}
	}

	public Map<String, SuggestCategory> getSuggestions(String value) {
		return getSuggestions(value, typeService.getSuggestFields());
	}
	
	public Map<String, SuggestCategory> getSuggestions(String value, String field) {
		return getSuggestions(value, Arrays.asList(field));
	}

	public Map<String, SuggestCategory> getSuggestions(String value, List<String> fields) {
		
		Map<String, SuggestCategory> result = new HashMap<>();
//		Map<String, List<String>> result = new HashMap<>();

		SuggestBuilder builder = new SuggestBuilder();
		for (String field : fields) {
			CompletionSuggestionBuilder completionSuggestBuilder = SuggestBuilders.completionSuggestion(String.format("%s.completion", field));
			completionSuggestBuilder.skipDuplicates(true); // when skipDuplicates = false, suggestions will be
															// duplicated for example in case of arrays with multiple
															// entries
			completionSuggestBuilder.prefix(value, config.getFuzzySuggestion() ? Fuzziness.AUTO : Fuzziness.ZERO);
			builder.addSuggestion(String.format(SUGGEST_FORMAT, field), completionSuggestBuilder);
		}

		SearchResponse response = client.prepareSearch(config.getIndex()).setQuery(QueryBuilders.matchAllQuery())
				.suggest(builder).execute().actionGet();
		Suggest suggest = response.getSuggest();

		for (String field : fields) {
			CompletionSuggestion fieldSuggestion = suggest.getSuggestion(String.format(SUGGEST_FORMAT, field));
//			LOG.debug(String.format("Suggestion queried for [%s] in field [%s]", value, String.format("%s.completion", field)));
			for (CompletionSuggestion.Entry entry : fieldSuggestion.getEntries()) {
				for (Option option : entry.getOptions()) {
					if(result.get(field) == null) {
						result.put(field, new SuggestCategory(field));
					}
					String suggestion = option.getText().string();
//					LOG.debug("> suggestion: " + suggestion + " for Doc: " + option.getHit().getSourceAsMap().get(FIELD_REFERENCE) + " ["
//							+ option.getHit().getId() + "]");

					result.get(field).getResults().add(new SuggestItem(suggestion));
				}
			}
		}
		return result;
	}

}

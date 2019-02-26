package de.tuchemnitz.tomkr.msar.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import de.tuchemnitz.tomkr.msar.model.Location;


public interface LocationRepo extends ElasticsearchRepository<Location, String>{
	
//	Page<MetaFile> findByLocation(String location, Pageable pageable);
//	List<MetaFile> findByFileName(String fileName);
	
//	@Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"firstName\", \"lastName\"], \"fuzziness\": \"AUTO\"}}")
//	@Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"location\"], \"fuzziness\": \"AUTO\"}}")
//	@Query("{\"fuzzy\": {\"location\": \"?0\"}}")
//	@Query("{\"fuzzy\": {\"location\": { \"value\": \"?0\", \"fuzziness\": \"?1\"}}}")
//	Page<MetaFile> findByLocationFuzzy(String location, String fuzziness, Pageable pageable);
}

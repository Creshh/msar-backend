package de.tuchemnitz.tomkr.metaapp.es;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface MetaRepository extends ElasticsearchRepository<MetaFile, String>{
	
	Page<MetaFile> findByLocation(String location, Pageable pageable);
	List<MetaFile> findByFileName(String fileName);
	
//	@Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"firstName\", \"lastName\"], \"fuzziness\": \"AUTO\"}}")
//	@Query("{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"location\"], \"fuzziness\": \"AUTO\"}}")
//	@Query("{\"fuzzy\": {\"location\": \"?0\"}}")
	@Query("{\"fuzzy\": {\"location\": { \"value\": \"?0\", \"fuzziness\": \"?1\"}}}")
	Page<MetaFile> findByLocationFuzzy(String location, String fuzziness, Pageable pageable);
}

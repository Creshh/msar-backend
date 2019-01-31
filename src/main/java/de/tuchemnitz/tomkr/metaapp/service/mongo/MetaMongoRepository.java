package de.tuchemnitz.tomkr.metaapp.service.mongo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import de.tuchemnitz.tomkr.metaapp.model.MetaFile;

public interface MetaMongoRepository extends MongoRepository<MetaFile, String>{
	
	Page<MetaFile> findByLocation(String location, Pageable pageable);
	List<MetaFile> findByFileName(String fileName);
}

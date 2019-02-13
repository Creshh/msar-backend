package de.tuchemnitz.tomkr.meta.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.meta.model.Asset;
import de.tuchemnitz.tomkr.meta.model.MetaObject;

@Service
public class MetaRetrievalService {
	
	@Autowired
	private MetaRetrievalRepository repo;

	public Asset save(Asset asset) {
		return repo.save(asset);
	}
	
	public void delete(Asset asset) {
		repo.delete(asset);
	}
	
	public void update(Asset asset, String key, MetaObject value) {
		// findByReference
		// append new metaData to asset
		// save
	}
//
//	public List<MetaFile> findByFileName(String fileName) {
//		return metaRepository.findByFileName(fileName);
//	}
//
//	public Page<MetaFile> findByLocation(String location, PageRequest pageRequest) {
//		return metaRepository.findByLocation(location, pageRequest);
//	}
//	
//	public Page<MetaFile> findByLocationFuzzy(String location, String fuzziness, PageRequest pageRequest) {
//		return metaRepository.findByLocationFuzzy(location, fuzziness, pageRequest);
//	}
	
	public Iterable<Asset> findAll() {
		return repo.findAll();
	}
}

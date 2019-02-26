package de.tuchemnitz.tomkr.msar.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.msar.model.Location;

@Service
public class LocationService {
	
	@Autowired
	private LocationRepo repo;

	public Location save(Location location) {
		return repo.save(location);
	}
	
	public void delete(Location location) {
		repo.delete(location);
	}
	
	public void update(Location location) {
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
	
	public Iterable<Location> findAll() {
		return repo.findAll();
	}
}

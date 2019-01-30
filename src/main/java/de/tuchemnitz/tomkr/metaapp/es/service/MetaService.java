package de.tuchemnitz.tomkr.metaapp.es.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import de.tuchemnitz.tomkr.metaapp.model.MetaFile;

@Service
public class MetaService {

	@Autowired
	public MetaRepository metaRepository;

	public MetaFile save(MetaFile metaFile) {
		return metaRepository.save(metaFile);
	}
	
	public void delete(MetaFile metaFile) {
		metaRepository.delete(metaFile);
	}

	public List<MetaFile> findByFileName(String fileName) {
		return metaRepository.findByFileName(fileName);
	}

	public Page<MetaFile> findByLocation(String location, PageRequest pageRequest) {
		return metaRepository.findByLocation(location, pageRequest);
	}
	
	public Page<MetaFile> findByLocationFuzzy(String location, String fuzziness, PageRequest pageRequest) {
		return metaRepository.findByLocationFuzzy(location, fuzziness, pageRequest);
	}
	
	public Iterable<MetaFile> findAll() {
		return metaRepository.findAll();
	}
}

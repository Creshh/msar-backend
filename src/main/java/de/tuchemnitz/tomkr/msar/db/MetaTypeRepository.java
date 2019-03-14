package de.tuchemnitz.tomkr.msar.db;

import org.springframework.data.jpa.repository.JpaRepository;

import de.tuchemnitz.tomkr.msar.core.registry.types.MetaType;

public interface MetaTypeRepository extends JpaRepository<MetaType, Integer>{

	MetaType findByName(String name);
	
}

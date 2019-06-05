package de.tuchemnitz.tomkr.msar.db;

import org.springframework.data.jpa.repository.JpaRepository;

import de.tuchemnitz.tomkr.msar.db.types.Field;

public interface FieldRepository extends JpaRepository<Field, Integer>{

	Iterable<Field> findAllBySuggest(boolean b);

}

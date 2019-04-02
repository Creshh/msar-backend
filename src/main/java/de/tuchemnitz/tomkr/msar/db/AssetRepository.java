package de.tuchemnitz.tomkr.msar.db;

import org.springframework.data.jpa.repository.JpaRepository;

import de.tuchemnitz.tomkr.msar.db.types.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long>{

}

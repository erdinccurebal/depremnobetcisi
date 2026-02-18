package com.depremnobetcisi.infrastructure.output.persistence.repository;

import com.depremnobetcisi.infrastructure.output.persistence.entity.EarthquakeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface JpaEarthquakeRepository extends JpaRepository<EarthquakeEntity, Long> {

    boolean existsByEarthquakeId(String earthquakeId);

    List<EarthquakeEntity> findByEventTimeAfterOrderByEventTimeDesc(Instant since);
}

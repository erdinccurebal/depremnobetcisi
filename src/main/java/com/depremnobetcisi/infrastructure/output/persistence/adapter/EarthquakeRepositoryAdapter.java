package com.depremnobetcisi.infrastructure.output.persistence.adapter;

import com.depremnobetcisi.domain.model.Earthquake;
import com.depremnobetcisi.domain.port.output.EarthquakeRepository;
import com.depremnobetcisi.infrastructure.output.persistence.mapper.EarthquakeMapper;
import com.depremnobetcisi.infrastructure.output.persistence.repository.JpaEarthquakeRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public class EarthquakeRepositoryAdapter implements EarthquakeRepository {

    private final JpaEarthquakeRepository jpaRepository;
    private final EarthquakeMapper mapper;

    public EarthquakeRepositoryAdapter(JpaEarthquakeRepository jpaRepository, EarthquakeMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Earthquake save(Earthquake earthquake) {
        var entity = mapper.toEntity(earthquake);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existsByEarthquakeId(String earthquakeId) {
        return jpaRepository.existsByEarthquakeId(earthquakeId);
    }

    @Override
    public List<Earthquake> findByEventTimeAfter(Instant since) {
        return jpaRepository.findByEventTimeAfterOrderByEventTimeDesc(since)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}

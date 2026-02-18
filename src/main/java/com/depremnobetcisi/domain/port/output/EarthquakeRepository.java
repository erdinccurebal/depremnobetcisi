package com.depremnobetcisi.domain.port.output;

import com.depremnobetcisi.domain.model.Earthquake;

import java.time.Instant;
import java.util.List;

public interface EarthquakeRepository {
    Earthquake save(Earthquake earthquake);
    boolean existsByEarthquakeId(String earthquakeId);
    List<Earthquake> findByEventTimeAfter(Instant since);
}

package com.depremnobetcisi.domain.port.output;

import com.depremnobetcisi.domain.model.Earthquake;

import java.util.List;

public interface EarthquakeApiClient {
    List<Earthquake> fetchLatestEarthquakes();
}

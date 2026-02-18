package com.depremnobetcisi.domain.port.input;

import com.depremnobetcisi.domain.model.Earthquake;
import java.util.List;

public interface EarthquakeMonitoringUseCase {
    List<Earthquake> fetchAndStoreLatestEarthquakes();
    List<Earthquake> getRecentEarthquakes(int hours);
}

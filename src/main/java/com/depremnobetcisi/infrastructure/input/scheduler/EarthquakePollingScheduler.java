package com.depremnobetcisi.infrastructure.input.scheduler;

import com.depremnobetcisi.domain.port.input.EarthquakeMonitoringUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EarthquakePollingScheduler {

    private static final Logger log = LoggerFactory.getLogger(EarthquakePollingScheduler.class);

    private final EarthquakeMonitoringUseCase earthquakeMonitoring;

    public EarthquakePollingScheduler(EarthquakeMonitoringUseCase earthquakeMonitoring) {
        this.earthquakeMonitoring = earthquakeMonitoring;
    }

    @Scheduled(fixedDelayString = "${app.earthquake.poll-interval-ms:30000}")
    public void pollEarthquakes() {
        try {
            var newEarthquakes = earthquakeMonitoring.fetchAndStoreLatestEarthquakes();
            if (!newEarthquakes.isEmpty()) {
                log.info("Polling completed: {} new earthquakes stored", newEarthquakes.size());
            }
        } catch (Exception e) {
            log.error("Earthquake polling failed: {}", e.getMessage(), e);
        }
    }
}

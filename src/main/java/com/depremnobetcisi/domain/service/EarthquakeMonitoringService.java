package com.depremnobetcisi.domain.service;

import com.depremnobetcisi.domain.model.Earthquake;
import com.depremnobetcisi.domain.port.input.EarthquakeMonitoringUseCase;
import com.depremnobetcisi.domain.port.input.NotificationDispatchUseCase;
import com.depremnobetcisi.domain.port.output.EarthquakeApiClient;
import com.depremnobetcisi.domain.port.output.EarthquakeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EarthquakeMonitoringService implements EarthquakeMonitoringUseCase {

    private static final Logger log = LoggerFactory.getLogger(EarthquakeMonitoringService.class);
    private static final double NOTIFICATION_THRESHOLD_MAGNITUDE = 4.0;

    private final EarthquakeApiClient apiClient;
    private final EarthquakeRepository earthquakeRepository;
    private final NotificationDispatchUseCase notificationDispatch;

    public EarthquakeMonitoringService(EarthquakeApiClient apiClient,
                                        EarthquakeRepository earthquakeRepository,
                                        NotificationDispatchUseCase notificationDispatch) {
        this.apiClient = apiClient;
        this.earthquakeRepository = earthquakeRepository;
        this.notificationDispatch = notificationDispatch;
    }

    @Override
    public List<Earthquake> fetchAndStoreLatestEarthquakes() {
        List<Earthquake> fetched = apiClient.fetchLatestEarthquakes();
        List<Earthquake> newEarthquakes = new ArrayList<>();

        for (Earthquake eq : fetched) {
            if (earthquakeRepository.existsByEarthquakeId(eq.getEarthquakeId())) {
                continue;
            }
            Earthquake saved = earthquakeRepository.save(eq);
            newEarthquakes.add(saved);
            log.info("New earthquake stored: {} (M{}) at {}", saved.getTitle(), saved.getMagnitude(), saved.getEventTime());

            if (saved.getMagnitude() >= NOTIFICATION_THRESHOLD_MAGNITUDE) {
                try {
                    notificationDispatch.notifyUsersForEarthquake(saved);
                } catch (Exception e) {
                    log.error("Failed to dispatch notifications for earthquake {}: {}", saved.getEarthquakeId(), e.getMessage(), e);
                }
            }
        }

        return newEarthquakes;
    }

    @Override
    public List<Earthquake> getRecentEarthquakes(int hours) {
        Instant since = Instant.now().minus(hours, ChronoUnit.HOURS);
        return earthquakeRepository.findByEventTimeAfter(since);
    }
}

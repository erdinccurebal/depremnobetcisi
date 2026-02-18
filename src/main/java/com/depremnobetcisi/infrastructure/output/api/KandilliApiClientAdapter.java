package com.depremnobetcisi.infrastructure.output.api;

import com.depremnobetcisi.domain.exception.EarthquakeApiException;
import com.depremnobetcisi.domain.model.Earthquake;
import com.depremnobetcisi.domain.port.output.EarthquakeApiClient;
import com.depremnobetcisi.infrastructure.output.api.dto.KandilliResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class KandilliApiClientAdapter implements EarthquakeApiClient {

    private static final Logger log = LoggerFactory.getLogger(KandilliApiClientAdapter.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public KandilliApiClientAdapter(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Earthquake> fetchLatestEarthquakes() {
        try {
            KandilliResponse response = restClient.get()
                    .uri("/deprem/kandilli/live")
                    .retrieve()
                    .body(KandilliResponse.class);

            if (response == null || response.result() == null) {
                log.warn("Empty response from Kandilli API");
                return Collections.emptyList();
            }

            return response.result().stream()
                    .map(this::toDomainEarthquake)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to fetch earthquakes from Kandilli API: {}", e.getMessage(), e);
            throw new EarthquakeApiException("Failed to fetch earthquakes from Kandilli API", e);
        }
    }

    private Earthquake toDomainEarthquake(KandilliResponse.KandilliEarthquake ke) {
        Earthquake eq = new Earthquake();
        eq.setEarthquakeId(ke.earthquakeId());
        eq.setProvider(ke.provider() != null ? ke.provider() : "kandilli");
        eq.setTitle(ke.title());
        eq.setMagnitude(ke.magnitude());
        eq.setDepthKm(ke.depth());

        // GeoJSON uses [longitude, latitude] order
        if (ke.geojson() != null && ke.geojson().coordinates() != null && ke.geojson().coordinates().size() >= 2) {
            eq.setLongitude(ke.geojson().coordinates().get(0));
            eq.setLatitude(ke.geojson().coordinates().get(1));
        }

        if (ke.dateTime() != null) {
            try {
                LocalDateTime ldt = LocalDateTime.parse(ke.dateTime(), DATE_FORMATTER);
                eq.setEventTime(ldt.atZone(ZoneId.of("Europe/Istanbul")).toInstant());
            } catch (Exception e) {
                log.warn("Failed to parse date '{}', using now", ke.dateTime());
                eq.setEventTime(Instant.now());
            }
        } else {
            eq.setEventTime(Instant.now());
        }

        if (ke.locationProperties() != null && ke.locationProperties().closestCity() != null) {
            eq.setClosestCity(ke.locationProperties().closestCity().name());
        }

        try {
            eq.setRawJson(objectMapper.writeValueAsString(ke));
        } catch (Exception e) {
            log.warn("Failed to serialize earthquake raw JSON", e);
        }

        return eq;
    }
}

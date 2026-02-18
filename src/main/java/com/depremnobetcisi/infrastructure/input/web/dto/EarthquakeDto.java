package com.depremnobetcisi.infrastructure.input.web.dto;

import java.time.Instant;

public record EarthquakeDto(
        Long id,
        String earthquakeId,
        String title,
        double magnitude,
        double depthKm,
        double latitude,
        double longitude,
        Instant eventTime,
        String closestCity
) {}

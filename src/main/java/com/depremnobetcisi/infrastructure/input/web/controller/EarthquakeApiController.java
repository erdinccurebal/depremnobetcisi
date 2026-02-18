package com.depremnobetcisi.infrastructure.input.web.controller;

import com.depremnobetcisi.domain.model.Earthquake;
import com.depremnobetcisi.domain.port.input.EarthquakeMonitoringUseCase;
import com.depremnobetcisi.infrastructure.input.web.dto.EarthquakeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/earthquakes")
public class EarthquakeApiController {

    private final EarthquakeMonitoringUseCase earthquakeMonitoring;

    public EarthquakeApiController(EarthquakeMonitoringUseCase earthquakeMonitoring) {
        this.earthquakeMonitoring = earthquakeMonitoring;
    }

    @GetMapping("/recent")
    public ResponseEntity<List<EarthquakeDto>> getRecentEarthquakes(
            @RequestParam(defaultValue = "24") int hours) {
        List<EarthquakeDto> earthquakes = earthquakeMonitoring.getRecentEarthquakes(hours).stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(earthquakes);
    }

    private EarthquakeDto toDto(Earthquake eq) {
        return new EarthquakeDto(
                eq.getId(),
                eq.getEarthquakeId(),
                eq.getTitle(),
                eq.getMagnitude(),
                eq.getDepthKm(),
                eq.getLatitude(),
                eq.getLongitude(),
                eq.getEventTime(),
                eq.getClosestCity()
        );
    }
}

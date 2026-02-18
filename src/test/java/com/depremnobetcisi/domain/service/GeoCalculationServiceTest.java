package com.depremnobetcisi.domain.service;

import com.depremnobetcisi.domain.model.BoundingBox;
import com.depremnobetcisi.domain.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoCalculationServiceTest {

    private GeoCalculationService geoService;

    @BeforeEach
    void setUp() {
        geoService = new GeoCalculationService();
    }

    @Test
    void shouldCalculateDistanceBetweenIstanbulAndAnkara() {
        Location istanbul = new Location(41.0082, 28.9784);
        Location ankara = new Location(39.9334, 32.8597);

        double distance = geoService.calculateDistanceKm(istanbul, ankara);

        // Istanbul-Ankara is approximately 350 km
        assertTrue(distance > 340 && distance < 360,
                "Expected ~350 km, got " + distance);
    }

    @Test
    void shouldReturnZeroDistanceForSameLocation() {
        Location loc = new Location(39.0, 35.0);

        double distance = geoService.calculateDistanceKm(loc, loc);

        assertEquals(0.0, distance, 0.001);
    }

    @Test
    void shouldReturnTrueWhenWithinRadius() {
        Location loc1 = new Location(39.0, 35.0);
        Location loc2 = new Location(39.1, 35.1);

        assertTrue(geoService.isWithinRadius(loc1, loc2, 50));
    }

    @Test
    void shouldReturnFalseWhenOutsideRadius() {
        Location istanbul = new Location(41.0082, 28.9784);
        Location ankara = new Location(39.9334, 32.8597);

        assertFalse(geoService.isWithinRadius(istanbul, ankara, 100));
    }

    @Test
    void shouldCalculateBoundingBox() {
        Location center = new Location(39.0, 35.0);
        double radiusKm = 100;

        BoundingBox box = geoService.getBoundingBox(center, radiusKm);

        assertTrue(box.minLat() < center.latitude());
        assertTrue(box.maxLat() > center.latitude());
        assertTrue(box.minLon() < center.longitude());
        assertTrue(box.maxLon() > center.longitude());

        // The bounding box should contain the center
        assertTrue(box.minLat() < 39.0 && box.maxLat() > 39.0);
        assertTrue(box.minLon() < 35.0 && box.maxLon() > 35.0);
    }

    @Test
    void shouldCalculateDistanceBetweenAntipodes() {
        Location north = new Location(0.0, 0.0);
        Location south = new Location(0.0, 180.0);

        double distance = geoService.calculateDistanceKm(north, south);

        // Half the Earth's circumference: ~20015 km
        assertTrue(distance > 20000 && distance < 20100,
                "Expected ~20015 km, got " + distance);
    }
}

package com.depremnobetcisi.domain.service;

import com.depremnobetcisi.domain.model.BoundingBox;
import com.depremnobetcisi.domain.model.Location;

public class GeoCalculationService {

    private static final double EARTH_RADIUS_KM = 6371.0;

    public double calculateDistanceKm(Location from, Location to) {
        double lat1 = Math.toRadians(from.latitude());
        double lat2 = Math.toRadians(to.latitude());
        double dLat = Math.toRadians(to.latitude() - from.latitude());
        double dLon = Math.toRadians(to.longitude() - from.longitude());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public boolean isWithinRadius(Location from, Location to, double radiusKm) {
        return calculateDistanceKm(from, to) <= radiusKm;
    }

    public BoundingBox getBoundingBox(Location center, double radiusKm) {
        double latDelta = radiusKm / EARTH_RADIUS_KM * (180.0 / Math.PI);
        double lonDelta = radiusKm / (EARTH_RADIUS_KM * Math.cos(Math.toRadians(center.latitude()))) * (180.0 / Math.PI);

        return new BoundingBox(
                center.latitude() - latDelta,
                center.latitude() + latDelta,
                center.longitude() - lonDelta,
                center.longitude() + lonDelta
        );
    }
}

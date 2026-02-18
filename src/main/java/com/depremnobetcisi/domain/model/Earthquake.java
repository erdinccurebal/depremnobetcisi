package com.depremnobetcisi.domain.model;

import java.time.Instant;

public class Earthquake {
    private Long id;
    private String earthquakeId;
    private String provider;
    private String title;
    private double magnitude;
    private double depthKm;
    private double latitude;
    private double longitude;
    private Instant eventTime;
    private String closestCity;
    private String rawJson;
    private Instant createdAt;

    public Earthquake() {
        this.provider = "kandilli";
    }

    public Location getLocation() {
        return new Location(latitude, longitude);
    }

    public void setLocation(Location location) {
        this.latitude = location.latitude();
        this.longitude = location.longitude();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEarthquakeId() { return earthquakeId; }
    public void setEarthquakeId(String earthquakeId) { this.earthquakeId = earthquakeId; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getMagnitude() { return magnitude; }
    public void setMagnitude(double magnitude) { this.magnitude = magnitude; }

    public double getDepthKm() { return depthKm; }
    public void setDepthKm(double depthKm) { this.depthKm = depthKm; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public Instant getEventTime() { return eventTime; }
    public void setEventTime(Instant eventTime) { this.eventTime = eventTime; }

    public String getClosestCity() { return closestCity; }
    public void setClosestCity(String closestCity) { this.closestCity = closestCity; }

    public String getRawJson() { return rawJson; }
    public void setRawJson(String rawJson) { this.rawJson = rawJson; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

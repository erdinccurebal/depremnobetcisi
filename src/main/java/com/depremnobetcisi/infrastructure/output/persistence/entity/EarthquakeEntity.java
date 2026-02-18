package com.depremnobetcisi.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.Instant;

@Entity
@Table(name = "earthquakes")
public class EarthquakeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "earthquake_id", nullable = false, unique = true)
    private String earthquakeId;

    @Column
    private String provider = "kandilli";

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private double magnitude;

    @Column(name = "depth_km", nullable = false)
    private double depthKm;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    @Column(name = "closest_city")
    private String closestCity;

    @Column(name = "raw_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String rawJson;

    @Column(name = "created_at")
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
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

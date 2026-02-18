package com.depremnobetcisi.infrastructure.output.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "notification_log",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "earthquake_id"}))
public class NotificationLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "earthquake_id", nullable = false)
    private Long earthquakeId;

    @Column(name = "distance_km", nullable = false)
    private double distanceKm;

    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Column(name = "delivery_status")
    private String deliveryStatus = "SENT";

    @Column(name = "sent_at")
    private Instant sentAt;

    @PrePersist
    protected void onCreate() {
        sentAt = Instant.now();
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getEarthquakeId() { return earthquakeId; }
    public void setEarthquakeId(Long earthquakeId) { this.earthquakeId = earthquakeId; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public String getMessageText() { return messageText; }
    public void setMessageText(String messageText) { this.messageText = messageText; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }
}

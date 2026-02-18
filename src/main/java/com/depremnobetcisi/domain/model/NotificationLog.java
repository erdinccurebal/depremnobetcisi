package com.depremnobetcisi.domain.model;

import java.time.Instant;

public class NotificationLog {
    private Long id;
    private Long userId;
    private Long earthquakeId;
    private double distanceKm;
    private String messageText;
    private DeliveryStatus deliveryStatus;
    private Instant sentAt;

    public NotificationLog() {
        this.deliveryStatus = DeliveryStatus.SENT;
        this.sentAt = Instant.now();
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

    public DeliveryStatus getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(DeliveryStatus deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }
}

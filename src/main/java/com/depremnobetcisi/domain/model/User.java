package com.depremnobetcisi.domain.model;

import java.time.Instant;

public class User {
    private Long id;
    private Long telegramChatId;
    private String telegramUsername;
    private String fullName;
    private String phoneNumber;
    private String addressText;
    private double latitude;
    private double longitude;
    private double notificationRadiusKm;
    private double minMagnitude;
    private boolean active;
    private boolean kvkkConsent;
    private ConversationState conversationState;
    private Instant createdAt;
    private Instant updatedAt;

    public User() {
        this.notificationRadiusKm = 100.0;
        this.minMagnitude = 4.0;
        this.active = true;
        this.conversationState = ConversationState.IDLE;
    }

    public Location getLocation() {
        return new Location(latitude, longitude);
    }

    public void setLocation(Location location) {
        this.latitude = location.latitude();
        this.longitude = location.longitude();
    }

    public boolean hasCompleteProfile() {
        return latitude != 0 && longitude != 0 && kvkkConsent;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTelegramChatId() { return telegramChatId; }
    public void setTelegramChatId(Long telegramChatId) { this.telegramChatId = telegramChatId; }

    public String getTelegramUsername() { return telegramUsername; }
    public void setTelegramUsername(String telegramUsername) { this.telegramUsername = telegramUsername; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddressText() { return addressText; }
    public void setAddressText(String addressText) { this.addressText = addressText; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getNotificationRadiusKm() { return notificationRadiusKm; }
    public void setNotificationRadiusKm(double notificationRadiusKm) { this.notificationRadiusKm = notificationRadiusKm; }

    public double getMinMagnitude() { return minMagnitude; }
    public void setMinMagnitude(double minMagnitude) { this.minMagnitude = minMagnitude; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean isKvkkConsent() { return kvkkConsent; }
    public void setKvkkConsent(boolean kvkkConsent) { this.kvkkConsent = kvkkConsent; }

    public ConversationState getConversationState() { return conversationState; }
    public void setConversationState(ConversationState conversationState) { this.conversationState = conversationState; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

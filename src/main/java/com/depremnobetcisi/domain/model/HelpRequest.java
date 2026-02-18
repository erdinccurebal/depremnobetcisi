package com.depremnobetcisi.domain.model;

import java.time.Instant;

public class HelpRequest {
    private Long id;
    private Long userId;
    private String fullName;
    private String phoneNumber;
    private double latitude;
    private double longitude;
    private String addressText;
    private String needTypes;
    private String description;
    private boolean kvkkConsent;
    private HelpRequestStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    public HelpRequest() {
        this.status = HelpRequestStatus.ACTIVE;
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

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getAddressText() { return addressText; }
    public void setAddressText(String addressText) { this.addressText = addressText; }

    public String getNeedTypes() { return needTypes; }
    public void setNeedTypes(String needTypes) { this.needTypes = needTypes; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isKvkkConsent() { return kvkkConsent; }
    public void setKvkkConsent(boolean kvkkConsent) { this.kvkkConsent = kvkkConsent; }

    public HelpRequestStatus getStatus() { return status; }
    public void setStatus(HelpRequestStatus status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}

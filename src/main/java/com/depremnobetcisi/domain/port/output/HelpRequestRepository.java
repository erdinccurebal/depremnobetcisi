package com.depremnobetcisi.domain.port.output;

import com.depremnobetcisi.domain.model.HelpRequest;

import java.util.List;

public interface HelpRequestRepository {
    HelpRequest save(HelpRequest helpRequest);
    List<HelpRequest> findActiveRequests();
    List<HelpRequest> findActiveRequestsInBoundingBox(double minLat, double maxLat, double minLon, double maxLon);
    boolean hasActiveRequest(Long userId);
    void deleteActiveByUserId(Long userId);
}

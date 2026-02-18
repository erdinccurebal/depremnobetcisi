package com.depremnobetcisi.domain.port.input;

import com.depremnobetcisi.domain.model.HelpRequest;

import java.util.List;

public interface HelpRequestUseCase {
    HelpRequest createHelpRequest(HelpRequest helpRequest);
    List<HelpRequest> getActiveHelpRequests();
    List<HelpRequest> getNearbyHelpRequests(double lat, double lng, double radiusKm);
    boolean hasActiveRequest(Long userId);
    void deleteActiveByUserId(Long userId);
}

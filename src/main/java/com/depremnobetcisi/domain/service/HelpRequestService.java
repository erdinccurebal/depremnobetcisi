package com.depremnobetcisi.domain.service;

import com.depremnobetcisi.domain.model.BoundingBox;
import com.depremnobetcisi.domain.model.HelpRequest;
import com.depremnobetcisi.domain.model.Location;
import com.depremnobetcisi.domain.port.input.HelpRequestUseCase;
import com.depremnobetcisi.domain.port.output.HelpRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HelpRequestService implements HelpRequestUseCase {

    private static final Logger log = LoggerFactory.getLogger(HelpRequestService.class);

    private final HelpRequestRepository helpRequestRepository;
    private final GeoCalculationService geoService;

    public HelpRequestService(HelpRequestRepository helpRequestRepository, GeoCalculationService geoService) {
        this.helpRequestRepository = helpRequestRepository;
        this.geoService = geoService;
    }

    @Override
    public HelpRequest createHelpRequest(HelpRequest helpRequest) {
        HelpRequest saved = helpRequestRepository.save(helpRequest);
        log.info("Help request created: id={}, name={}, needs={}", saved.getId(), saved.getFullName(), saved.getNeedTypes());
        return saved;
    }

    @Override
    public List<HelpRequest> getActiveHelpRequests() {
        return helpRequestRepository.findActiveRequests();
    }

    @Override
    public boolean hasActiveRequest(Long userId) {
        return helpRequestRepository.hasActiveRequest(userId);
    }

    @Override
    public void deleteActiveByUserId(Long userId) {
        helpRequestRepository.deleteActiveByUserId(userId);
        log.info("Active help requests deleted for userId={}", userId);
    }

    @Override
    public List<HelpRequest> getNearbyHelpRequests(double lat, double lng, double radiusKm) {
        Location center = new Location(lat, lng);
        BoundingBox box = geoService.getBoundingBox(center, radiusKm);
        List<HelpRequest> candidates = helpRequestRepository.findActiveRequestsInBoundingBox(
                box.minLat(), box.maxLat(), box.minLon(), box.maxLon());

        return candidates.stream()
                .filter(hr -> geoService.isWithinRadius(center, hr.getLocation(), radiusKm))
                .toList();
    }
}

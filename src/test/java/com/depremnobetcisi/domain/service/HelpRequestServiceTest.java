package com.depremnobetcisi.domain.service;

import com.depremnobetcisi.domain.model.HelpRequest;
import com.depremnobetcisi.domain.model.HelpRequestStatus;
import com.depremnobetcisi.domain.port.output.HelpRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HelpRequestServiceTest {

    @Mock
    private HelpRequestRepository helpRequestRepository;

    private HelpRequestService service;

    @BeforeEach
    void setUp() {
        GeoCalculationService geoService = new GeoCalculationService();
        service = new HelpRequestService(helpRequestRepository, geoService);
    }

    @Test
    void shouldCreateHelpRequest() {
        HelpRequest request = new HelpRequest();
        request.setFullName("Test User");
        request.setPhoneNumber("5551234567");
        request.setLatitude(39.0);
        request.setLongitude(35.0);
        request.setNeedTypes("TIBBI,BARINMA");
        request.setKvkkConsent(true);

        when(helpRequestRepository.save(any())).thenAnswer(inv -> {
            HelpRequest hr = inv.getArgument(0);
            hr.setId(1L);
            return hr;
        });

        HelpRequest saved = service.createHelpRequest(request);

        assertNotNull(saved.getId());
        verify(helpRequestRepository).save(request);
    }

    @Test
    void shouldGetActiveHelpRequests() {
        HelpRequest request = new HelpRequest();
        request.setStatus(HelpRequestStatus.ACTIVE);
        when(helpRequestRepository.findActiveRequests()).thenReturn(List.of(request));

        List<HelpRequest> result = service.getActiveHelpRequests();

        assertEquals(1, result.size());
    }

    @Test
    void shouldFilterNearbyRequests() {
        HelpRequest nearby = new HelpRequest();
        nearby.setLatitude(39.01);
        nearby.setLongitude(35.01);

        HelpRequest farAway = new HelpRequest();
        farAway.setLatitude(41.0);
        farAway.setLongitude(29.0);

        when(helpRequestRepository.findActiveRequestsInBoundingBox(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(List.of(nearby, farAway));

        List<HelpRequest> result = service.getNearbyHelpRequests(39.0, 35.0, 10);

        assertEquals(1, result.size());
    }
}

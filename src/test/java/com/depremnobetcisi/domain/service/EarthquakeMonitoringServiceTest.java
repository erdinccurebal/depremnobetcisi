package com.depremnobetcisi.domain.service;

import com.depremnobetcisi.domain.model.Earthquake;
import com.depremnobetcisi.domain.port.input.NotificationDispatchUseCase;
import com.depremnobetcisi.domain.port.output.EarthquakeApiClient;
import com.depremnobetcisi.domain.port.output.EarthquakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EarthquakeMonitoringServiceTest {

    @Mock
    private EarthquakeApiClient apiClient;
    @Mock
    private EarthquakeRepository earthquakeRepository;
    @Mock
    private NotificationDispatchUseCase notificationDispatch;

    private EarthquakeMonitoringService service;

    @BeforeEach
    void setUp() {
        service = new EarthquakeMonitoringService(apiClient, earthquakeRepository, notificationDispatch);
    }

    @Test
    void shouldStoreNewEarthquakes() {
        Earthquake eq = createEarthquake("eq1", 3.5);
        when(apiClient.fetchLatestEarthquakes()).thenReturn(List.of(eq));
        when(earthquakeRepository.existsByEarthquakeId("eq1")).thenReturn(false);
        when(earthquakeRepository.save(any())).thenReturn(eq);

        List<Earthquake> result = service.fetchAndStoreLatestEarthquakes();

        assertEquals(1, result.size());
        verify(earthquakeRepository).save(eq);
    }

    @Test
    void shouldSkipExistingEarthquakes() {
        Earthquake eq = createEarthquake("eq1", 3.5);
        when(apiClient.fetchLatestEarthquakes()).thenReturn(List.of(eq));
        when(earthquakeRepository.existsByEarthquakeId("eq1")).thenReturn(true);

        List<Earthquake> result = service.fetchAndStoreLatestEarthquakes();

        assertTrue(result.isEmpty());
        verify(earthquakeRepository, never()).save(any());
    }

    @Test
    void shouldNotifyForLargeEarthquakes() {
        Earthquake eq = createEarthquake("eq1", 5.0);
        eq.setId(1L);
        when(apiClient.fetchLatestEarthquakes()).thenReturn(List.of(eq));
        when(earthquakeRepository.existsByEarthquakeId("eq1")).thenReturn(false);
        when(earthquakeRepository.save(any())).thenReturn(eq);

        service.fetchAndStoreLatestEarthquakes();

        verify(notificationDispatch).notifyUsersForEarthquake(eq);
    }

    @Test
    void shouldNotNotifyForSmallEarthquakes() {
        Earthquake eq = createEarthquake("eq1", 3.5);
        when(apiClient.fetchLatestEarthquakes()).thenReturn(List.of(eq));
        when(earthquakeRepository.existsByEarthquakeId("eq1")).thenReturn(false);
        when(earthquakeRepository.save(any())).thenReturn(eq);

        service.fetchAndStoreLatestEarthquakes();

        verify(notificationDispatch, never()).notifyUsersForEarthquake(any());
    }

    @Test
    void shouldGetRecentEarthquakes() {
        Earthquake eq = createEarthquake("eq1", 4.5);
        when(earthquakeRepository.findByEventTimeAfter(any())).thenReturn(List.of(eq));

        List<Earthquake> result = service.getRecentEarthquakes(24);

        assertEquals(1, result.size());
    }

    private Earthquake createEarthquake(String id, double magnitude) {
        Earthquake eq = new Earthquake();
        eq.setEarthquakeId(id);
        eq.setTitle("Test Earthquake");
        eq.setMagnitude(magnitude);
        eq.setDepthKm(10.0);
        eq.setLatitude(39.0);
        eq.setLongitude(35.0);
        eq.setEventTime(Instant.now());
        return eq;
    }
}

package com.depremnobetcisi.domain.service;

import com.depremnobetcisi.domain.model.Earthquake;
import com.depremnobetcisi.domain.model.User;
import com.depremnobetcisi.domain.port.output.NotificationLogRepository;
import com.depremnobetcisi.domain.port.output.NotificationSender;
import com.depremnobetcisi.domain.port.output.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationDispatchServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationLogRepository notificationLogRepository;
    @Mock
    private NotificationSender notificationSender;

    private NotificationDispatchService service;

    @BeforeEach
    void setUp() {
        GeoCalculationService geoService = new GeoCalculationService();
        service = new NotificationDispatchService(userRepository, notificationLogRepository, notificationSender, geoService);
    }

    @Test
    void shouldNotifyNearbyUsers() {
        Earthquake eq = createEarthquake(39.0, 35.0, 5.0);
        User user = createUser(1L, 123L, 39.05, 35.05, 100.0);

        when(userRepository.findActiveUsersInBoundingBox(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(List.of(user));
        when(notificationLogRepository.existsByUserIdAndEarthquakeId(1L, 1L)).thenReturn(false);
        when(notificationSender.sendMessage(eq(123L), anyString())).thenReturn(true);

        service.notifyUsersForEarthquake(eq);

        verify(notificationSender).sendMessage(eq(123L), anyString());
        verify(notificationLogRepository).save(any());
    }

    @Test
    void shouldNotNotifyFarAwayUsers() {
        Earthquake eq = createEarthquake(39.0, 35.0, 5.0);
        User user = createUser(1L, 123L, 41.0, 29.0, 50.0); // Istanbul - far away

        when(userRepository.findActiveUsersInBoundingBox(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(List.of(user));

        service.notifyUsersForEarthquake(eq);

        verify(notificationSender, never()).sendMessage(anyLong(), anyString());
    }

    @Test
    void shouldNotDuplicateNotifications() {
        Earthquake eq = createEarthquake(39.0, 35.0, 5.0);
        User user = createUser(1L, 123L, 39.05, 35.05, 100.0);

        when(userRepository.findActiveUsersInBoundingBox(anyDouble(), anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(List.of(user));
        when(notificationLogRepository.existsByUserIdAndEarthquakeId(1L, 1L)).thenReturn(true);

        service.notifyUsersForEarthquake(eq);

        verify(notificationSender, never()).sendMessage(anyLong(), anyString());
    }

    private Earthquake createEarthquake(double lat, double lon, double magnitude) {
        Earthquake eq = new Earthquake();
        eq.setId(1L);
        eq.setEarthquakeId("test-eq");
        eq.setTitle("Test Location");
        eq.setMagnitude(magnitude);
        eq.setDepthKm(10.0);
        eq.setLatitude(lat);
        eq.setLongitude(lon);
        eq.setEventTime(Instant.now());
        return eq;
    }

    private User createUser(Long id, Long chatId, double lat, double lon, double radius) {
        User user = new User();
        user.setId(id);
        user.setTelegramChatId(chatId);
        user.setLatitude(lat);
        user.setLongitude(lon);
        user.setNotificationRadiusKm(radius);
        user.setMinMagnitude(4.0);
        user.setActive(true);
        return user;
    }
}

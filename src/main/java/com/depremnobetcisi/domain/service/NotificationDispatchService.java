package com.depremnobetcisi.domain.service;

import com.depremnobetcisi.domain.model.*;
import com.depremnobetcisi.domain.port.input.NotificationDispatchUseCase;
import com.depremnobetcisi.domain.port.output.NotificationLogRepository;
import com.depremnobetcisi.domain.port.output.NotificationSender;
import com.depremnobetcisi.domain.port.output.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotificationDispatchService implements NotificationDispatchUseCase {

    private static final Logger log = LoggerFactory.getLogger(NotificationDispatchService.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
            .withZone(ZoneId.of("Europe/Istanbul"));

    private final UserRepository userRepository;
    private final NotificationLogRepository notificationLogRepository;
    private final NotificationSender notificationSender;
    private final GeoCalculationService geoService;

    public NotificationDispatchService(UserRepository userRepository,
                                        NotificationLogRepository notificationLogRepository,
                                        NotificationSender notificationSender,
                                        GeoCalculationService geoService) {
        this.userRepository = userRepository;
        this.notificationLogRepository = notificationLogRepository;
        this.notificationSender = notificationSender;
        this.geoService = geoService;
    }

    @Override
    public void notifyUsersForEarthquake(Earthquake earthquake) {
        Location eqLocation = earthquake.getLocation();
        BoundingBox box = geoService.getBoundingBox(eqLocation, 500);

        List<User> candidates = userRepository.findActiveUsersInBoundingBox(
                box.minLat(), box.maxLat(), box.minLon(), box.maxLon(), earthquake.getMagnitude());

        int notified = 0;
        for (User user : candidates) {
            double distance = geoService.calculateDistanceKm(user.getLocation(), eqLocation);

            if (distance > user.getNotificationRadiusKm()) {
                continue;
            }

            if (notificationLogRepository.existsByUserIdAndEarthquakeId(user.getId(), earthquake.getId())) {
                continue;
            }

            String message = buildNotificationMessage(earthquake, distance);

            boolean sent = notificationSender.sendMessage(user.getTelegramChatId(), message);

            NotificationLog logEntry = new NotificationLog();
            logEntry.setUserId(user.getId());
            logEntry.setEarthquakeId(earthquake.getId());
            logEntry.setDistanceKm(distance);
            logEntry.setMessageText(message);
            logEntry.setDeliveryStatus(sent ? DeliveryStatus.SENT : DeliveryStatus.FAILED);
            notificationLogRepository.save(logEntry);

            if (sent) notified++;
        }

        log.info("Notified {} users for earthquake {} (M{})", notified, earthquake.getEarthquakeId(), earthquake.getMagnitude());
    }

    private String buildNotificationMessage(Earthquake earthquake, double distanceKm) {
        return String.format("""
                🚨 *DEPREM BİLDİRİMİ*

                📍 *Konum:* %s
                📏 *Büyüklük:* %.1f
                📐 *Derinlik:* %.1f km
                🕐 *Zaman:* %s
                📌 *Size uzaklık:* %.1f km

                Lütfen güvende kalın!""",
                earthquake.getTitle(),
                earthquake.getMagnitude(),
                earthquake.getDepthKm(),
                FORMATTER.format(earthquake.getEventTime()),
                distanceKm);
    }
}

package com.depremnobetcisi.domain.port.output;

import com.depremnobetcisi.domain.model.NotificationLog;

public interface NotificationLogRepository {
    NotificationLog save(NotificationLog notificationLog);
    boolean existsByUserIdAndEarthquakeId(Long userId, Long earthquakeId);
}

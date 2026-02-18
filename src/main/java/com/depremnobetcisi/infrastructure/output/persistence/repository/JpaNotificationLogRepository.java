package com.depremnobetcisi.infrastructure.output.persistence.repository;

import com.depremnobetcisi.infrastructure.output.persistence.entity.NotificationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationLogRepository extends JpaRepository<NotificationLogEntity, Long> {

    boolean existsByUserIdAndEarthquakeId(Long userId, Long earthquakeId);
}

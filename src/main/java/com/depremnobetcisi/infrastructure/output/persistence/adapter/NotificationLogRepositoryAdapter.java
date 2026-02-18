package com.depremnobetcisi.infrastructure.output.persistence.adapter;

import com.depremnobetcisi.domain.model.NotificationLog;
import com.depremnobetcisi.domain.port.output.NotificationLogRepository;
import com.depremnobetcisi.infrastructure.output.persistence.mapper.NotificationLogMapper;
import com.depremnobetcisi.infrastructure.output.persistence.repository.JpaNotificationLogRepository;
import org.springframework.stereotype.Repository;

@Repository
public class NotificationLogRepositoryAdapter implements NotificationLogRepository {

    private final JpaNotificationLogRepository jpaRepository;
    private final NotificationLogMapper mapper;

    public NotificationLogRepositoryAdapter(JpaNotificationLogRepository jpaRepository, NotificationLogMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public NotificationLog save(NotificationLog notificationLog) {
        var entity = mapper.toEntity(notificationLog);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existsByUserIdAndEarthquakeId(Long userId, Long earthquakeId) {
        return jpaRepository.existsByUserIdAndEarthquakeId(userId, earthquakeId);
    }
}

package com.depremnobetcisi.infrastructure.output.persistence.mapper;

import com.depremnobetcisi.domain.model.DeliveryStatus;
import com.depremnobetcisi.domain.model.NotificationLog;
import com.depremnobetcisi.infrastructure.output.persistence.entity.NotificationLogEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationLogMapper {

    public NotificationLog toDomain(NotificationLogEntity entity) {
        NotificationLog nl = new NotificationLog();
        nl.setId(entity.getId());
        nl.setUserId(entity.getUserId());
        nl.setEarthquakeId(entity.getEarthquakeId());
        nl.setDistanceKm(entity.getDistanceKm());
        nl.setMessageText(entity.getMessageText());
        nl.setDeliveryStatus(DeliveryStatus.valueOf(entity.getDeliveryStatus()));
        nl.setSentAt(entity.getSentAt());
        return nl;
    }

    public NotificationLogEntity toEntity(NotificationLog nl) {
        NotificationLogEntity entity = new NotificationLogEntity();
        entity.setId(nl.getId());
        entity.setUserId(nl.getUserId());
        entity.setEarthquakeId(nl.getEarthquakeId());
        entity.setDistanceKm(nl.getDistanceKm());
        entity.setMessageText(nl.getMessageText());
        entity.setDeliveryStatus(nl.getDeliveryStatus().name());
        entity.setSentAt(nl.getSentAt());
        return entity;
    }
}

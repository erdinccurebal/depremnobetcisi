package com.depremnobetcisi.infrastructure.output.persistence.mapper;

import com.depremnobetcisi.domain.model.Earthquake;
import com.depremnobetcisi.infrastructure.output.persistence.entity.EarthquakeEntity;
import org.springframework.stereotype.Component;

@Component
public class EarthquakeMapper {

    public Earthquake toDomain(EarthquakeEntity entity) {
        Earthquake eq = new Earthquake();
        eq.setId(entity.getId());
        eq.setEarthquakeId(entity.getEarthquakeId());
        eq.setProvider(entity.getProvider());
        eq.setTitle(entity.getTitle());
        eq.setMagnitude(entity.getMagnitude());
        eq.setDepthKm(entity.getDepthKm());
        eq.setLatitude(entity.getLatitude());
        eq.setLongitude(entity.getLongitude());
        eq.setEventTime(entity.getEventTime());
        eq.setClosestCity(entity.getClosestCity());
        eq.setRawJson(entity.getRawJson());
        eq.setCreatedAt(entity.getCreatedAt());
        return eq;
    }

    public EarthquakeEntity toEntity(Earthquake eq) {
        EarthquakeEntity entity = new EarthquakeEntity();
        entity.setId(eq.getId());
        entity.setEarthquakeId(eq.getEarthquakeId());
        entity.setProvider(eq.getProvider());
        entity.setTitle(eq.getTitle());
        entity.setMagnitude(eq.getMagnitude());
        entity.setDepthKm(eq.getDepthKm());
        entity.setLatitude(eq.getLatitude());
        entity.setLongitude(eq.getLongitude());
        entity.setEventTime(eq.getEventTime());
        entity.setClosestCity(eq.getClosestCity());
        entity.setRawJson(eq.getRawJson());
        entity.setCreatedAt(eq.getCreatedAt());
        return entity;
    }
}

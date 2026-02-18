package com.depremnobetcisi.infrastructure.output.persistence.mapper;

import com.depremnobetcisi.domain.model.HelpRequest;
import com.depremnobetcisi.domain.model.HelpRequestStatus;
import com.depremnobetcisi.infrastructure.output.persistence.entity.HelpRequestEntity;
import org.springframework.stereotype.Component;

@Component
public class HelpRequestMapper {

    public HelpRequest toDomain(HelpRequestEntity entity) {
        HelpRequest hr = new HelpRequest();
        hr.setId(entity.getId());
        hr.setUserId(entity.getUserId());
        hr.setFullName(entity.getFullName());
        hr.setPhoneNumber(entity.getPhoneNumber());
        hr.setLatitude(entity.getLatitude());
        hr.setLongitude(entity.getLongitude());
        hr.setAddressText(entity.getAddressText());
        hr.setNeedTypes(entity.getNeedTypes());
        hr.setDescription(entity.getDescription());
        hr.setKvkkConsent(entity.isKvkkConsent());
        hr.setStatus(HelpRequestStatus.valueOf(entity.getStatus()));
        hr.setCreatedAt(entity.getCreatedAt());
        hr.setUpdatedAt(entity.getUpdatedAt());
        return hr;
    }

    public HelpRequestEntity toEntity(HelpRequest hr) {
        HelpRequestEntity entity = new HelpRequestEntity();
        entity.setId(hr.getId());
        entity.setUserId(hr.getUserId());
        entity.setFullName(hr.getFullName());
        entity.setPhoneNumber(hr.getPhoneNumber());
        entity.setLatitude(hr.getLatitude());
        entity.setLongitude(hr.getLongitude());
        entity.setAddressText(hr.getAddressText());
        entity.setNeedTypes(hr.getNeedTypes());
        entity.setDescription(hr.getDescription());
        entity.setKvkkConsent(hr.isKvkkConsent());
        entity.setStatus(hr.getStatus().name());
        entity.setCreatedAt(hr.getCreatedAt());
        entity.setUpdatedAt(hr.getUpdatedAt());
        return entity;
    }
}

package com.depremnobetcisi.infrastructure.output.persistence.mapper;

import com.depremnobetcisi.domain.model.ConversationState;
import com.depremnobetcisi.domain.model.User;
import com.depremnobetcisi.infrastructure.output.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserEntity entity) {
        User user = new User();
        user.setId(entity.getId());
        user.setTelegramChatId(entity.getTelegramChatId());
        user.setTelegramUsername(entity.getTelegramUsername());
        user.setFullName(entity.getFullName());
        user.setPhoneNumber(entity.getPhoneNumber());
        user.setAddressText(entity.getAddressText());
        user.setLatitude(entity.getLatitude());
        user.setLongitude(entity.getLongitude());
        user.setNotificationRadiusKm(entity.getNotificationRadiusKm());
        user.setMinMagnitude(entity.getMinMagnitude());
        user.setActive(entity.isActive());
        user.setKvkkConsent(entity.isKvkkConsent());
        user.setConversationState(ConversationState.valueOf(entity.getConversationState()));
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        return user;
    }

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setTelegramChatId(user.getTelegramChatId());
        entity.setTelegramUsername(user.getTelegramUsername());
        entity.setFullName(user.getFullName());
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setAddressText(user.getAddressText());
        entity.setLatitude(user.getLatitude());
        entity.setLongitude(user.getLongitude());
        entity.setNotificationRadiusKm(user.getNotificationRadiusKm());
        entity.setMinMagnitude(user.getMinMagnitude());
        entity.setActive(user.isActive());
        entity.setKvkkConsent(user.isKvkkConsent());
        entity.setConversationState(user.getConversationState().name());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }
}

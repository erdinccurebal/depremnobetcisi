package com.depremnobetcisi.domain.port.output;

import com.depremnobetcisi.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByTelegramChatId(Long chatId);
    List<User> findActiveUsersInBoundingBox(double minLat, double maxLat, double minLon, double maxLon, double minMagnitude);
    void deleteByTelegramChatId(Long chatId);
}

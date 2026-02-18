package com.depremnobetcisi.domain.port.input;

import com.depremnobetcisi.domain.model.ConversationState;
import com.depremnobetcisi.domain.model.Location;
import com.depremnobetcisi.domain.model.User;

import java.util.Optional;

public interface UserSubscriptionUseCase {
    User subscribeUser(Long chatId, String username, Location location);
    Optional<User> findByChatId(Long chatId);
    User updateConversationState(Long chatId, ConversationState state);
    User getOrCreateUser(Long chatId, String username);
    User saveUser(User user);
    void deleteUser(Long chatId);
}

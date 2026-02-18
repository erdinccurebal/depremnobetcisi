package com.depremnobetcisi.domain.service;

import com.depremnobetcisi.domain.model.ConversationState;
import com.depremnobetcisi.domain.model.Location;
import com.depremnobetcisi.domain.model.User;
import com.depremnobetcisi.domain.port.input.UserSubscriptionUseCase;
import com.depremnobetcisi.domain.port.output.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class UserSubscriptionService implements UserSubscriptionUseCase {

    private static final Logger log = LoggerFactory.getLogger(UserSubscriptionService.class);

    private final UserRepository userRepository;

    public UserSubscriptionService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User subscribeUser(Long chatId, String username, Location location) {
        User user = userRepository.findByTelegramChatId(chatId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setTelegramChatId(chatId);
                    return newUser;
                });

        user.setTelegramUsername(username);
        user.setLocation(location);
        user.setActive(true);

        User saved = userRepository.save(user);
        log.info("User subscribed: chatId={}, location=({}, {})", chatId, location.latitude(), location.longitude());
        return saved;
    }

    @Override
    public Optional<User> findByChatId(Long chatId) {
        return userRepository.findByTelegramChatId(chatId);
    }

    @Override
    public User updateConversationState(Long chatId, ConversationState state) {
        User user = getOrCreateUser(chatId, null);
        user.setConversationState(state);
        return userRepository.save(user);
    }

    @Override
    public User getOrCreateUser(Long chatId, String username) {
        return userRepository.findByTelegramChatId(chatId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setTelegramChatId(chatId);
                    newUser.setTelegramUsername(username);
                    return userRepository.save(newUser);
                });
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long chatId) {
        userRepository.deleteByTelegramChatId(chatId);
        log.info("User deleted: chatId={}", chatId);
    }
}

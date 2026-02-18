package com.depremnobetcisi.infrastructure.output.persistence.adapter;

import com.depremnobetcisi.domain.model.User;
import com.depremnobetcisi.domain.port.output.UserRepository;
import com.depremnobetcisi.infrastructure.output.persistence.mapper.UserMapper;
import com.depremnobetcisi.infrastructure.output.persistence.repository.JpaUserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaRepository;
    private final UserMapper mapper;

    public UserRepositoryAdapter(JpaUserRepository jpaRepository, UserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        var entity = mapper.toEntity(user);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByTelegramChatId(Long chatId) {
        return jpaRepository.findByTelegramChatId(chatId).map(mapper::toDomain);
    }

    @Override
    public List<User> findActiveUsersInBoundingBox(double minLat, double maxLat, double minLon, double maxLon, double minMagnitude) {
        return jpaRepository.findActiveUsersInBoundingBox(minLat, maxLat, minLon, maxLon, minMagnitude)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteByTelegramChatId(Long chatId) {
        jpaRepository.deleteByTelegramChatId(chatId);
    }
}

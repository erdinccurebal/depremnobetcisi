package com.depremnobetcisi.infrastructure.output.persistence.repository;

import com.depremnobetcisi.infrastructure.output.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByTelegramChatId(Long telegramChatId);

    @Query("""
        SELECT u FROM UserEntity u
        WHERE u.active = true
          AND u.latitude BETWEEN :minLat AND :maxLat
          AND u.longitude BETWEEN :minLon AND :maxLon
          AND u.minMagnitude <= :magnitude
    """)
    List<UserEntity> findActiveUsersInBoundingBox(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon,
            @Param("magnitude") double magnitude);

    @Transactional
    @Modifying
    void deleteByTelegramChatId(Long telegramChatId);
}

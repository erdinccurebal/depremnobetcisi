package com.depremnobetcisi.infrastructure.output.persistence.repository;

import com.depremnobetcisi.infrastructure.output.persistence.entity.HelpRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface JpaHelpRequestRepository extends JpaRepository<HelpRequestEntity, Long> {

    List<HelpRequestEntity> findByStatus(String status);

    boolean existsByUserIdAndStatus(Long userId, String status);

    @Transactional
    @Modifying
    @Query("DELETE FROM HelpRequestEntity h WHERE h.userId = :userId AND h.status = 'ACTIVE'")
    void deleteActiveByUserId(@Param("userId") Long userId);

    @Query("""
        SELECT h FROM HelpRequestEntity h
        WHERE h.status = 'ACTIVE'
          AND h.latitude BETWEEN :minLat AND :maxLat
          AND h.longitude BETWEEN :minLon AND :maxLon
    """)
    List<HelpRequestEntity> findActiveInBoundingBox(
            @Param("minLat") double minLat,
            @Param("maxLat") double maxLat,
            @Param("minLon") double minLon,
            @Param("maxLon") double maxLon);
}

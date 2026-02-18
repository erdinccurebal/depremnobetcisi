package com.depremnobetcisi.infrastructure.output.persistence.adapter;

import com.depremnobetcisi.domain.model.HelpRequest;
import com.depremnobetcisi.domain.port.output.HelpRequestRepository;
import com.depremnobetcisi.infrastructure.output.persistence.mapper.HelpRequestMapper;
import com.depremnobetcisi.infrastructure.output.persistence.repository.JpaHelpRequestRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HelpRequestRepositoryAdapter implements HelpRequestRepository {

    private final JpaHelpRequestRepository jpaRepository;
    private final HelpRequestMapper mapper;

    public HelpRequestRepositoryAdapter(JpaHelpRequestRepository jpaRepository, HelpRequestMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public HelpRequest save(HelpRequest helpRequest) {
        var entity = mapper.toEntity(helpRequest);
        var saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public List<HelpRequest> findActiveRequests() {
        return jpaRepository.findByStatus("ACTIVE")
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<HelpRequest> findActiveRequestsInBoundingBox(double minLat, double maxLat, double minLon, double maxLon) {
        return jpaRepository.findActiveInBoundingBox(minLat, maxLat, minLon, maxLon)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean hasActiveRequest(Long userId) {
        return jpaRepository.existsByUserIdAndStatus(userId, "ACTIVE");
    }

    @Override
    public void deleteActiveByUserId(Long userId) {
        jpaRepository.deleteActiveByUserId(userId);
    }
}

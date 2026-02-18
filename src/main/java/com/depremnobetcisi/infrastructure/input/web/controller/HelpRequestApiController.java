package com.depremnobetcisi.infrastructure.input.web.controller;

import com.depremnobetcisi.domain.model.HelpRequest;
import com.depremnobetcisi.domain.port.input.HelpRequestUseCase;
import com.depremnobetcisi.infrastructure.input.web.dto.HelpRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/help-requests")
public class HelpRequestApiController {

    private final HelpRequestUseCase helpRequestUseCase;

    public HelpRequestApiController(HelpRequestUseCase helpRequestUseCase) {
        this.helpRequestUseCase = helpRequestUseCase;
    }

    @GetMapping
    public ResponseEntity<List<HelpRequestDto>> getActiveHelpRequests() {
        List<HelpRequestDto> requests = helpRequestUseCase.getActiveHelpRequests().stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<HelpRequestDto>> getNearbyHelpRequests(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "50") double radius) {
        List<HelpRequestDto> requests = helpRequestUseCase.getNearbyHelpRequests(lat, lng, radius).stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(requests);
    }

    private HelpRequestDto toDto(HelpRequest hr) {
        return new HelpRequestDto(
                hr.getId(),
                hr.getFullName(),
                hr.getPhoneNumber(),
                hr.getLatitude(),
                hr.getLongitude(),
                hr.getAddressText(),
                hr.getNeedTypes(),
                hr.getDescription(),
                hr.getStatus().name(),
                hr.getCreatedAt()
        );
    }
}

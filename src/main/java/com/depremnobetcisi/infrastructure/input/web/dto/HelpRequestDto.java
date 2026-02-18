package com.depremnobetcisi.infrastructure.input.web.dto;

import java.time.Instant;

public record HelpRequestDto(
        Long id,
        String fullName,
        String phoneNumber,
        double latitude,
        double longitude,
        String addressText,
        String needTypes,
        String description,
        String status,
        Instant createdAt
) {}

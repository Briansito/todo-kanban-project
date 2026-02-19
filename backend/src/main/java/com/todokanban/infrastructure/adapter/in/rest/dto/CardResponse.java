package com.todokanban.infrastructure.adapter.in.rest.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for a {@link com.todokanban.domain.model.Card}.
 * Java 21 record – immutable and serialization-ready.
 */
public record CardResponse(
        UUID id,
        String title,
        String description,
        int position,
        Instant createdAt,
        Instant updatedAt
) {}

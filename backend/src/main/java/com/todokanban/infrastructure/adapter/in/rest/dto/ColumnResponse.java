package com.todokanban.infrastructure.adapter.in.rest.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for a {@link com.todokanban.domain.model.Column}.
 * Java 21 record – immutable and serialization-ready.
 */
public record ColumnResponse(
        UUID id,
        String name,
        int position,
        List<CardResponse> cards,
        Instant createdAt,
        Instant updatedAt
) {}

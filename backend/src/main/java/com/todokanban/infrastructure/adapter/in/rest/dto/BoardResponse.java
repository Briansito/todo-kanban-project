package com.todokanban.infrastructure.adapter.in.rest.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for a {@link com.todokanban.domain.model.Board}.
 * Java 21 record – immutable and serialization-ready.
 */
public record BoardResponse(
        UUID id,
        UUID workspaceId,
        String name,
        String description,
        List<ColumnResponse> columns,
        Instant createdAt,
        Instant updatedAt
) {}

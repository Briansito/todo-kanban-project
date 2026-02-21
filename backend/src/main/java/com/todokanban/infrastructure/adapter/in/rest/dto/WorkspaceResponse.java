package com.todokanban.infrastructure.adapter.in.rest.dto;

import java.time.Instant;
import java.util.UUID;

/** Response DTO for a Workspace. */
public record WorkspaceResponse(
        UUID id,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt
) {}

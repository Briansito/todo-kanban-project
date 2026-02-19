package com.todokanban.infrastructure.adapter.in.rest.dto;

import java.util.UUID;

/**
 * Request DTO for creating a new Board.
 *
 * <p>Java 21 record – Jackson deserializes this from the request body.
 * Field-level validation is handled by the compact constructor.</p>
 *
 * @param workspaceId the workspace this board belongs to (required)
 * @param name        the board name (required, not blank)
 * @param description optional description
 */
public record BoardRequest(
        UUID workspaceId,
        String name,
        String description
) {
    public BoardRequest {
        if (workspaceId == null) {
            throw new IllegalArgumentException("workspaceId is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required and must not be blank");
        }
    }
}

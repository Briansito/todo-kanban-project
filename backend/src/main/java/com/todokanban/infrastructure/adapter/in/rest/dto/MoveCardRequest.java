package com.todokanban.infrastructure.adapter.in.rest.dto;

import java.util.UUID;

/**
 * Request DTO for the move-card operation.
 *
 * <p>PATCH /api/v1/boards/{boardId}/cards/{cardId}/move</p>
 *
 * @param sourceColumnId the column the card currently belongs to (required)
 * @param targetColumnId the column to move the card into (required)
 */
public record MoveCardRequest(
        UUID sourceColumnId,
        UUID targetColumnId
) {
    public MoveCardRequest {
        if (sourceColumnId == null) {
            throw new IllegalArgumentException("sourceColumnId is required");
        }
        if (targetColumnId == null) {
            throw new IllegalArgumentException("targetColumnId is required");
        }
        if (sourceColumnId.equals(targetColumnId)) {
            throw new IllegalArgumentException("sourceColumnId and targetColumnId must be different");
        }
    }
}

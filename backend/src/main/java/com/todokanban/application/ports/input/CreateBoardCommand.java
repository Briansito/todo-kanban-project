package com.todokanban.application.ports.input;

import com.todokanban.domain.model.WorkspaceId;

/**
 * Command object for the {@link CreateBoardUseCase}.
 *
 * <p>Using a {@code record} leverages Java 21 conciseness and guarantees
 * immutability out of the box. No Spring or JPA annotations.</p>
 *
 * @param workspaceId the workspace this board belongs to
 * @param name        the board name (must not be blank)
 * @param description optional description of the board
 */
public record CreateBoardCommand(WorkspaceId workspaceId, String name, String description) {

    public CreateBoardCommand {
        if (workspaceId == null) {
            throw new IllegalArgumentException("WorkspaceId must not be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Board name must not be blank");
        }
    }
}

package com.todokanban.application.ports.input;

import com.todokanban.domain.model.Workspace;

/** Input port for creating a new {@link Workspace}. */
public interface CreateWorkspaceUseCase {
    Workspace createWorkspace(CreateWorkspaceCommand command);
}

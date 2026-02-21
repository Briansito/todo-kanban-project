package com.todokanban.application.ports.input;

import com.todokanban.domain.model.Workspace;

import java.util.List;

/** Input port for listing all Workspaces. */
public interface GetWorkspacesUseCase {
    List<Workspace> getWorkspaces();
}

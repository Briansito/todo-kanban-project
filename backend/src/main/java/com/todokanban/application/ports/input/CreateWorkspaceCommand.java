package com.todokanban.application.ports.input;

/** Command for creating a new Workspace. */
public record CreateWorkspaceCommand(String name, String description) {
    public CreateWorkspaceCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Workspace name must not be blank");
        }
    }
}

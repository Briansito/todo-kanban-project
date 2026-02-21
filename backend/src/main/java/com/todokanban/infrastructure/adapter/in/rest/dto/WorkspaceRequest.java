package com.todokanban.infrastructure.adapter.in.rest.dto;

/** Request DTO for creating a Workspace. */
public record WorkspaceRequest(String name, String description) {
    public WorkspaceRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required and must not be blank");
        }
    }
}

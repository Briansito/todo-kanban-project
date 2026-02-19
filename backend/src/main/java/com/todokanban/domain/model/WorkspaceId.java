package com.todokanban.domain.model;

import java.util.UUID;

/**
 * Value object representing a Workspace identifier.
 * Wraps UUID for type safety.
 */
public record WorkspaceId(UUID value) {

    public WorkspaceId {
        if (value == null) {
            throw new IllegalArgumentException("WorkspaceId value must not be null");
        }
    }

    public static WorkspaceId generate() {
        return new WorkspaceId(UUID.randomUUID());
    }

    public static WorkspaceId of(String value) {
        return new WorkspaceId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

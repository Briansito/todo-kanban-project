package com.todokanban.domain.model;

import java.util.UUID;

/**
 * Value object representing a Board identifier.
 * Wraps UUID for type safety.
 */
public record BoardId(UUID value) {

    public BoardId {
        if (value == null) {
            throw new IllegalArgumentException("BoardId value must not be null");
        }
    }

    public static BoardId generate() {
        return new BoardId(UUID.randomUUID());
    }

    public static BoardId of(String value) {
        return new BoardId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

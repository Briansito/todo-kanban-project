package com.todokanban.domain.model;

import java.util.UUID;

/**
 * Value object representing a Column identifier.
 * Wraps UUID for type safety.
 */
public record ColumnId(UUID value) {

    public ColumnId {
        if (value == null) {
            throw new IllegalArgumentException("ColumnId value must not be null");
        }
    }

    public static ColumnId generate() {
        return new ColumnId(UUID.randomUUID());
    }

    public static ColumnId of(String value) {
        return new ColumnId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

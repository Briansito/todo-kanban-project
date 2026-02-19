package com.todokanban.domain.model;

import java.util.UUID;

/**
 * Value object representing a Card identifier.
 * Wraps UUID for type safety.
 */
public record CardId(UUID value) {

    public CardId {
        if (value == null) {
            throw new IllegalArgumentException("CardId value must not be null");
        }
    }

    public static CardId generate() {
        return new CardId(UUID.randomUUID());
    }

    public static CardId of(String value) {
        return new CardId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

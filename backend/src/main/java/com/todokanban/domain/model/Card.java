package com.todokanban.domain.model;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain entity representing a Trello-like card.
 * Pure Java 21 – no JPA or Spring annotations.
 */
public final class Card {

    private final CardId id;
    private String title;
    private String description;
    private int position;
    private final Instant createdAt;
    private Instant updatedAt;

    private Card(CardId id, String title, String description, int position, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    /**
     * Factory method to create a new Card with a generated ID.
     */
    public static Card create(String title, String description, int position) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Card title must not be blank");
        }
        return new Card(CardId.generate(), title, description, position, Instant.now());
    }

    /**
     * Reconstitution factory – used by persistence adapters to rebuild the entity.
     */
    public static Card reconstitute(CardId id, String title, String description,
                                    int position, Instant createdAt, Instant updatedAt) {
        Card card = new Card(id, title, description, position, createdAt);
        card.updatedAt = updatedAt;
        return card;
    }

    // ── Business behaviour ────────────────────────────────────────────────────

    public void updateTitle(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("Card title must not be blank");
        }
        this.title = newTitle;
        this.updatedAt = Instant.now();
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
        this.updatedAt = Instant.now();
    }

    public void updatePosition(int newPosition) {
        if (newPosition < 0) {
            throw new IllegalArgumentException("Card position must be non-negative");
        }
        this.position = newPosition;
        this.updatedAt = Instant.now();
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public CardId getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getPosition() { return position; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // ── Equality (by identity) ────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card card)) return false;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Card{id=%s, title='%s', position=%d}".formatted(id, title, position);
    }
}

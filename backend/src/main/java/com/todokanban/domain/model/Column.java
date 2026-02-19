package com.todokanban.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Domain entity representing a Kanban column (e.g. "To Do", "In Progress", "Done").
 * Owns an ordered list of {@link Card}s.
 * Pure Java 21 – no JPA or Spring annotations.
 */
public final class Column {

    private final ColumnId id;
    private String name;
    private int position;
    private final List<Card> cards;
    private final Instant createdAt;
    private Instant updatedAt;

    private Column(ColumnId id, String name, int position, List<Card> cards, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.cards = new ArrayList<>(cards);
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    /**
     * Factory method to create a new empty Column.
     */
    public static Column create(String name, int position) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Column name must not be blank");
        }
        return new Column(ColumnId.generate(), name, position, List.of(), Instant.now());
    }

    /**
     * Reconstitution factory – used by persistence adapters to rebuild the entity.
     */
    public static Column reconstitute(ColumnId id, String name, int position,
                                      List<Card> cards, Instant createdAt, Instant updatedAt) {
        Column column = new Column(id, name, position, cards, createdAt);
        column.updatedAt = updatedAt;
        return column;
    }

    // ── Business behaviour ────────────────────────────────────────────────────

    public void addCard(Card card) {
        Objects.requireNonNull(card, "Card must not be null");
        cards.add(card);
        this.updatedAt = Instant.now();
    }

    public Card removeCard(CardId cardId) {
        Objects.requireNonNull(cardId, "CardId must not be null");
        Card card = findCard(cardId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Card with id '%s' not found in column '%s'".formatted(cardId, id)));
        cards.remove(card);
        this.updatedAt = Instant.now();
        return card;
    }

    public Optional<Card> findCard(CardId cardId) {
        Objects.requireNonNull(cardId, "CardId must not be null");
        return cards.stream()
                .filter(c -> c.getId().equals(cardId))
                .findFirst();
    }

    public boolean containsCard(CardId cardId) {
        return findCard(cardId).isPresent();
    }

    public void updateName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Column name must not be blank");
        }
        this.name = newName;
        this.updatedAt = Instant.now();
    }

    public void updatePosition(int newPosition) {
        if (newPosition < 0) {
            throw new IllegalArgumentException("Column position must be non-negative");
        }
        this.position = newPosition;
        this.updatedAt = Instant.now();
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public ColumnId getId() { return id; }
    public String getName() { return name; }
    public int getPosition() { return position; }
    public List<Card> getCards() { return Collections.unmodifiableList(cards); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // ── Equality (by identity) ────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Column column)) return false;
        return Objects.equals(id, column.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Column{id=%s, name='%s', position=%d, cards=%d}"
                .formatted(id, name, position, cards.size());
    }
}

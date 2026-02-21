package com.todokanban.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Aggregate Root representing a Kanban board.
 *
 * <p>Owns an ordered list of {@link Column}s. All business invariants
 * related to card movement are enforced here, keeping the domain logic
 * inside the aggregate and out of application services.</p>
 *
 * <p>Pure Java 21 – no JPA or Spring annotations.</p>
 */
public final class Board {

    private final BoardId id;
    private final WorkspaceId workspaceId;
    private String name;
    private String description;
    private final List<Column> columns;
    private final Instant createdAt;
    private Instant updatedAt;

    private Board(BoardId id, WorkspaceId workspaceId, String name, String description,
                  List<Column> columns, Instant createdAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.name = name;
        this.description = description;
        this.columns = new ArrayList<>(columns);
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    /**
     * Factory method to create a new Board with no columns.
     */
    public static Board create(WorkspaceId workspaceId, String name, String description) {
        if (workspaceId == null) {
            throw new IllegalArgumentException("WorkspaceId must not be null");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Board name must not be blank");
        }
        return new Board(BoardId.generate(), workspaceId, name, description, List.of(), Instant.now());
    }

    /**
     * Reconstitution factory – used by persistence adapters to rebuild the aggregate.
     */
    public static Board reconstitute(BoardId id, WorkspaceId workspaceId, String name,
                                     String description, List<Column> columns,
                                     Instant createdAt, Instant updatedAt) {
        Board board = new Board(id, workspaceId, name, description, columns, createdAt);
        board.updatedAt = updatedAt;
        return board;
    }

    // ── Business behaviour ────────────────────────────────────────────────────

    /**
     * Moves a {@link Card} from one {@link Column} to another within this Board.
     *
     * <p>Business rules enforced:</p>
     * <ul>
     *   <li>Source column must belong to this board.</li>
     *   <li>Card must exist in the source column.</li>
     *   <li>Target column must belong to this board.</li>
     * </ul>
     *
     * @param cardId         the card to move
     * @param sourceColumnId the column the card currently belongs to
     * @param targetColumnId the column to move the card into
     * @throws IllegalArgumentException if any column or card is not found
     */
    public void moveCard(CardId cardId, ColumnId sourceColumnId, ColumnId targetColumnId) {
        Objects.requireNonNull(cardId,         "CardId must not be null");
        Objects.requireNonNull(sourceColumnId, "Source ColumnId must not be null");
        Objects.requireNonNull(targetColumnId, "Target ColumnId must not be null");

        Column source = findColumn(sourceColumnId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Source column '%s' not found in board '%s'".formatted(sourceColumnId, id)));

        // removeCard already validates the card exists in the source column
        Card card = source.removeCard(cardId);

        Column target = findColumn(targetColumnId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Target column '%s' not found in board '%s'".formatted(targetColumnId, id)));

        // Position card at the end of the target column
        card.updatePosition(target.getCards().size());
        target.addCard(card);

        this.updatedAt = Instant.now();
    }

    public void addColumn(Column column) {
        Objects.requireNonNull(column, "Column must not be null");
        columns.add(column);
        this.updatedAt = Instant.now();
    }

    public Column removeColumn(ColumnId columnId) {
        Column column = findColumn(columnId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Column '%s' not found in board '%s'".formatted(columnId, id)));
        columns.remove(column);
        this.updatedAt = Instant.now();
        return column;
    }

    /**
     * Adds a new {@link Card} to the specified column.
     *
     * @throws IllegalArgumentException if the column is not found in this board
     */
    public void addCardToColumn(ColumnId columnId, Card card) {
        Objects.requireNonNull(card, "Card must not be null");
        Column column = findColumn(columnId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Column '%s' not found in board '%s'".formatted(columnId, id)));
        card.updatePosition(column.getCards().size());
        column.addCard(card);
        this.updatedAt = Instant.now();
    }

    /**
     * Updates the title and/or description of a card within a column.
     * Pass {@code null} for any field to leave it unchanged.
     *
     * @throws IllegalArgumentException if the column or card is not found
     */
    public void updateCard(ColumnId columnId, CardId cardId, String title, String description) {
        Column column = findColumn(columnId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Column '%s' not found in board '%s'".formatted(columnId, id)));
        Card card = column.findCard(cardId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Card '%s' not found in column '%s'".formatted(cardId, columnId)));
        if (title != null && !title.isBlank()) {
            card.updateTitle(title);
        }
        if (description != null) {
            card.updateDescription(description);
        }
        this.updatedAt = Instant.now();
    }

    /**
     * Removes a card from the specified column.
     *
     * @throws IllegalArgumentException if the column or card is not found
     */
    public void removeCardFromColumn(ColumnId columnId, CardId cardId) {
        Column column = findColumn(columnId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Column '%s' not found in board '%s'".formatted(columnId, id)));
        column.removeCard(cardId);
        this.updatedAt = Instant.now();
    }


    public Optional<Column> findColumn(ColumnId columnId) {
        Objects.requireNonNull(columnId, "ColumnId must not be null");
        return columns.stream()
                .filter(c -> c.getId().equals(columnId))
                .findFirst();
    }

    public void updateName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Board name must not be blank");
        }
        this.name = newName;
        this.updatedAt = Instant.now();
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
        this.updatedAt = Instant.now();
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public BoardId getId()               { return id; }
    public WorkspaceId getWorkspaceId()  { return workspaceId; }
    public String getName()              { return name; }
    public String getDescription()       { return description; }
    public List<Column> getColumns()     { return Collections.unmodifiableList(columns); }
    public Instant getCreatedAt()        { return createdAt; }
    public Instant getUpdatedAt()        { return updatedAt; }

    // ── Equality (by identity) ────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board board)) return false;
        return Objects.equals(id, board.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Board{id=%s, name='%s', columns=%d}".formatted(id, name, columns.size());
    }
}

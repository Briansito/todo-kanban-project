package com.todokanban.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Domain entity representing a Workspace (top-level container, similar to a Trello account workspace).
 * Owns an ordered list of {@link Board}s.
 * Pure Java 21 – no JPA or Spring annotations.
 */
public final class Workspace {

    private final WorkspaceId id;
    private String name;
    private String description;
    private final List<Board> boards;
    private final Instant createdAt;
    private Instant updatedAt;

    private Workspace(WorkspaceId id, String name, String description,
                      List<Board> boards, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.boards = new ArrayList<>(boards);
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    /**
     * Factory method to create a new Workspace with no boards.
     */
    public static Workspace create(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Workspace name must not be blank");
        }
        return new Workspace(WorkspaceId.generate(), name, description, List.of(), Instant.now());
    }

    /**
     * Reconstitution factory – used by persistence adapters to rebuild the entity.
     */
    public static Workspace reconstitute(WorkspaceId id, String name, String description,
                                         List<Board> boards, Instant createdAt, Instant updatedAt) {
        Workspace workspace = new Workspace(id, name, description, boards, createdAt);
        workspace.updatedAt = updatedAt;
        return workspace;
    }

    // ── Business behaviour ────────────────────────────────────────────────────

    public void addBoard(Board board) {
        Objects.requireNonNull(board, "Board must not be null");
        boards.add(board);
        this.updatedAt = Instant.now();
    }

    public Board removeBoard(BoardId boardId) {
        Board board = findBoard(boardId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Board '%s' not found in workspace '%s'".formatted(boardId, id)));
        boards.remove(board);
        this.updatedAt = Instant.now();
        return board;
    }

    public Optional<Board> findBoard(BoardId boardId) {
        Objects.requireNonNull(boardId, "BoardId must not be null");
        return boards.stream()
                .filter(b -> b.getId().equals(boardId))
                .findFirst();
    }

    public void updateName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Workspace name must not be blank");
        }
        this.name = newName;
        this.updatedAt = Instant.now();
    }

    public void updateDescription(String newDescription) {
        this.description = newDescription;
        this.updatedAt = Instant.now();
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public WorkspaceId getId()      { return id; }
    public String getName()         { return name; }
    public String getDescription()  { return description; }
    public List<Board> getBoards()  { return Collections.unmodifiableList(boards); }
    public Instant getCreatedAt()   { return createdAt; }
    public Instant getUpdatedAt()   { return updatedAt; }

    // ── Equality (by identity) ────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workspace ws)) return false;
        return Objects.equals(id, ws.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Workspace{id=%s, name='%s', boards=%d}".formatted(id, name, boards.size());
    }
}

package com.todokanban.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA entity for the {@code workspaces} table.
 * Infrastructure concern only – never exposed to the domain.
 */
@Entity
@Table(name = "workspaces")
public class WorkspaceEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true,
               fetch = FetchType.LAZY)
    @OrderBy("name ASC")
    private List<BoardEntity> boards = new ArrayList<>();

    protected WorkspaceEntity() {}

    public WorkspaceEntity(UUID id, String name, String description,
                           Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public UUID getId()             { return id; }
    public String getName()         { return name; }
    public void setName(String n)   { this.name = n; }
    public String getDescription()  { return description; }
    public void setDescription(String d) { this.description = d; }
    public Instant getCreatedAt()   { return createdAt; }
    public Instant getUpdatedAt()   { return updatedAt; }
    public void setUpdatedAt(Instant u) { this.updatedAt = u; }
    public List<BoardEntity> getBoards() { return boards; }
    public void setBoards(List<BoardEntity> b) { this.boards = b; }
}

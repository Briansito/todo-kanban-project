package com.todokanban.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA entity for the {@code boards} table.
 * Infrastructure concern only – never exposed to the domain.
 */
@Entity
@Table(name = "boards")
public class BoardEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "workspace_id", nullable = false, updatable = false)
    private WorkspaceEntity workspace;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true,
               fetch = FetchType.EAGER)
    @OrderBy("position ASC")
    private List<ColumnEntity> columns = new ArrayList<>();

    protected BoardEntity() {}

    public BoardEntity(UUID id, WorkspaceEntity workspace, String name, String description,
                       Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.workspace = workspace;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public UUID getId()                       { return id; }
    public WorkspaceEntity getWorkspace()     { return workspace; }
    public void setWorkspace(WorkspaceEntity w) { this.workspace = w; }
    public String getName()                   { return name; }
    public void setName(String n)             { this.name = n; }
    public String getDescription()            { return description; }
    public void setDescription(String d)      { this.description = d; }
    public Instant getCreatedAt()             { return createdAt; }
    public Instant getUpdatedAt()             { return updatedAt; }
    public void setUpdatedAt(Instant u)       { this.updatedAt = u; }
    public List<ColumnEntity> getColumns()    { return columns; }
    public void setColumns(List<ColumnEntity> c) { this.columns = c; }
}

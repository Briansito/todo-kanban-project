package com.todokanban.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

/**
 * JPA entity for the {@code cards} table.
 * Infrastructure concern only – never exposed to the domain.
 */
@Entity
@Table(name = "cards")
public class CardEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "column_id", nullable = false)
    private ColumnEntity column;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected CardEntity() {}

    public CardEntity(UUID id, ColumnEntity column, String title, String description,
                      int position, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.column = column;
        this.title = title;
        this.description = description;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public UUID getId()                   { return id; }
    public ColumnEntity getColumn()       { return column; }
    public void setColumn(ColumnEntity c) { this.column = c; }
    public String getTitle()              { return title; }
    public void setTitle(String t)        { this.title = t; }
    public String getDescription()        { return description; }
    public void setDescription(String d)  { this.description = d; }
    public int getPosition()              { return position; }
    public void setPosition(int p)        { this.position = p; }
    public Instant getCreatedAt()         { return createdAt; }
    public Instant getUpdatedAt()         { return updatedAt; }
    public void setUpdatedAt(Instant u)   { this.updatedAt = u; }
}

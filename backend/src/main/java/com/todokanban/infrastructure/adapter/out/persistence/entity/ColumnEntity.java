package com.todokanban.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA entity for the {@code columns} table.
 * Infrastructure concern only – never exposed to the domain.
 */
@Entity
@Table(name = "columns")
public class ColumnEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "board_id", nullable = false, updatable = false)
    private BoardEntity board;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true,
               fetch = FetchType.EAGER)
    @OrderBy("position ASC")
    private List<CardEntity> cards = new ArrayList<>();

    protected ColumnEntity() {}

    public ColumnEntity(UUID id, BoardEntity board, String name, int position,
                        Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.board = board;
        this.name = name;
        this.position = position;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public UUID getId()                   { return id; }
    public BoardEntity getBoard()         { return board; }
    public void setBoard(BoardEntity b)   { this.board = b; }
    public String getName()               { return name; }
    public void setName(String n)         { this.name = n; }
    public int getPosition()              { return position; }
    public void setPosition(int p)        { this.position = p; }
    public Instant getCreatedAt()         { return createdAt; }
    public Instant getUpdatedAt()         { return updatedAt; }
    public void setUpdatedAt(Instant u)   { this.updatedAt = u; }
    public List<CardEntity> getCards()    { return cards; }
    public void setCards(List<CardEntity> c) { this.cards = c; }
}

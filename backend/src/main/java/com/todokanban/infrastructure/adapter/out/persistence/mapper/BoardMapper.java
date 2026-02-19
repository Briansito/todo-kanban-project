package com.todokanban.infrastructure.adapter.out.persistence.mapper;

import com.todokanban.domain.model.*;
import com.todokanban.infrastructure.adapter.out.persistence.entity.*;

import java.util.List;

/**
 * Manual mapper between domain model objects and JPA entities.
 *
 * <p>Uses static methods (no Spring injection) to keep it a pure utility class.
 * The domain {@code reconstitute()} factories are used here so the domain model
 * itself controls how it is rebuilt from persisted data.</p>
 *
 * <p>No framework annotations – infrastructure utility only.</p>
 */
public final class BoardMapper {

    private BoardMapper() {}

    // ── Entity → Domain ───────────────────────────────────────────────────────

    /**
     * Reconstitutes a full {@link Board} aggregate from its JPA representation.
     */
    public static Board toDomain(BoardEntity entity) {
        List<Column> columns = entity.getColumns().stream()
                .map(BoardMapper::toDomain)
                .toList();

        return Board.reconstitute(
                new BoardId(entity.getId()),
                new WorkspaceId(entity.getWorkspace().getId()),
                entity.getName(),
                entity.getDescription(),
                columns,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Column toDomain(ColumnEntity entity) {
        List<Card> cards = entity.getCards().stream()
                .map(BoardMapper::toDomain)
                .toList();

        return Column.reconstitute(
                new ColumnId(entity.getId()),
                entity.getName(),
                entity.getPosition(),
                cards,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static Card toDomain(CardEntity entity) {
        return Card.reconstitute(
                new CardId(entity.getId()),
                entity.getTitle(),
                entity.getDescription(),
                entity.getPosition(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    // ── Domain → Entity ───────────────────────────────────────────────────────

    /**
     * Converts a {@link Board} aggregate into a {@link BoardEntity} ready for persistence.
     * The {@link WorkspaceEntity} must already exist in the persistence context.
     *
     * @param board           the domain aggregate
     * @param workspaceEntity the managed workspace entity (FK reference)
     */
    public static BoardEntity toEntity(Board board, WorkspaceEntity workspaceEntity) {
        BoardEntity boardEntity = new BoardEntity(
                board.getId().value(),
                workspaceEntity,
                board.getName(),
                board.getDescription(),
                board.getCreatedAt(),
                board.getUpdatedAt()
        );

        List<ColumnEntity> columnEntities = board.getColumns().stream()
                .map(col -> toEntity(col, boardEntity))
                .toList();
        boardEntity.setColumns(columnEntities);

        return boardEntity;
    }

    public static ColumnEntity toEntity(Column column, BoardEntity boardEntity) {
        ColumnEntity columnEntity = new ColumnEntity(
                column.getId().value(),
                boardEntity,
                column.getName(),
                column.getPosition(),
                column.getCreatedAt(),
                column.getUpdatedAt()
        );

        List<CardEntity> cardEntities = column.getCards().stream()
                .map(card -> toEntity(card, columnEntity))
                .toList();
        columnEntity.setCards(cardEntities);

        return columnEntity;
    }

    public static CardEntity toEntity(Card card, ColumnEntity columnEntity) {
        return new CardEntity(
                card.getId().value(),
                columnEntity,
                card.getTitle(),
                card.getDescription(),
                card.getPosition(),
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }
}

package com.todokanban.infrastructure.adapter.in.rest.mapper;

import com.todokanban.application.ports.input.CreateBoardCommand;
import com.todokanban.application.ports.input.MoveCardCommand;
import com.todokanban.domain.model.*;
import com.todokanban.infrastructure.adapter.in.rest.dto.*;

import java.util.List;
import java.util.UUID;

/**
 * Mapper between REST layer objects (DTOs) and application/domain objects.
 *
 * <p>Static utility class — no Spring injection needed. Converts:</p>
 * <ul>
 *   <li>DTOs → Application Commands</li>
 *   <li>Domain entities → Response DTOs</li>
 * </ul>
 */
public final class RestMapper {

    private RestMapper() {}

    // ── DTO → Command ─────────────────────────────────────────────────────────

    /**
     * Converts a {@link BoardRequest} into a {@link CreateBoardCommand}.
     */
    public static CreateBoardCommand toCommand(BoardRequest request) {
        return new CreateBoardCommand(
                new WorkspaceId(request.workspaceId()),
                request.name(),
                request.description()
        );
    }

    /**
     * Converts a {@link MoveCardRequest} plus path-variable UUIDs into a {@link MoveCardCommand}.
     */
    public static MoveCardCommand toCommand(UUID boardId, UUID cardId, MoveCardRequest request) {
        return new MoveCardCommand(
                new BoardId(boardId),
                new CardId(cardId),
                new ColumnId(request.sourceColumnId()),
                new ColumnId(request.targetColumnId())
        );
    }

    // ── Domain → Response DTO ─────────────────────────────────────────────────

    /**
     * Maps a {@link Board} aggregate to a {@link BoardResponse}.
     */
    public static BoardResponse toResponse(Board board) {
        List<ColumnResponse> columns = board.getColumns().stream()
                .map(RestMapper::toResponse)
                .toList();

        return new BoardResponse(
                board.getId().value(),
                board.getWorkspaceId().value(),
                board.getName(),
                board.getDescription(),
                columns,
                board.getCreatedAt(),
                board.getUpdatedAt()
        );
    }

    public static ColumnResponse toResponse(Column column) {
        List<CardResponse> cards = column.getCards().stream()
                .map(RestMapper::toResponse)
                .toList();

        return new ColumnResponse(
                column.getId().value(),
                column.getName(),
                column.getPosition(),
                cards,
                column.getCreatedAt(),
                column.getUpdatedAt()
        );
    }

    public static CardResponse toResponse(Card card) {
        return new CardResponse(
                card.getId().value(),
                card.getTitle(),
                card.getDescription(),
                card.getPosition(),
                card.getCreatedAt(),
                card.getUpdatedAt()
        );
    }
}

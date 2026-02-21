package com.todokanban.infrastructure.adapter.in.rest.mapper;

import com.todokanban.application.ports.input.*;
import com.todokanban.domain.model.*;
import com.todokanban.infrastructure.adapter.in.rest.dto.*;

import java.util.List;
import java.util.UUID;

/**
 * Static mapper between REST DTOs and application/domain objects.
 * No Spring injection — pure utility class.
 */
public final class RestMapper {

    private RestMapper() {}

    // ── DTO → Command ─────────────────────────────────────────────────────────

    public static CreateWorkspaceCommand toCommand(WorkspaceRequest request) {
        return new CreateWorkspaceCommand(request.name(), request.description());
    }

    public static CreateBoardCommand toCommand(BoardRequest request) {
        return new CreateBoardCommand(
                new WorkspaceId(request.workspaceId()),
                request.name(),
                request.description());
    }

    public static MoveCardCommand toCommand(UUID boardId, UUID cardId,
                                            MoveCardRequest request) {
        return new MoveCardCommand(
                new BoardId(boardId),
                new CardId(cardId),
                new ColumnId(request.sourceColumnId()),
                new ColumnId(request.targetColumnId()));
    }

    public static CreateColumnCommand toCommand(UUID boardId,
                                                CreateColumnRequest request) {
        return new CreateColumnCommand(
                new BoardId(boardId),
                request.name(),
                request.position());
    }

    public static CreateCardCommand toCreateCardCommand(UUID boardId, UUID columnId,
                                                        CreateCardRequest request) {
        return new CreateCardCommand(
                new BoardId(boardId),
                new ColumnId(columnId),
                request.title(),
                request.description());
    }

    public static UpdateCardCommand toUpdateCardCommand(UUID boardId, UUID columnId,
                                                        UUID cardId,
                                                        UpdateCardRequest request) {
        return new UpdateCardCommand(
                new BoardId(boardId),
                new ColumnId(columnId),
                new CardId(cardId),
                request.title(),
                request.description());
    }

    public static DeleteCardCommand toDeleteCardCommand(UUID boardId, UUID columnId,
                                                        UUID cardId) {
        return new DeleteCardCommand(
                new BoardId(boardId),
                new ColumnId(columnId),
                new CardId(cardId));
    }

    // ── Domain → Response DTO ─────────────────────────────────────────────────

    public static WorkspaceResponse toResponse(Workspace workspace) {
        return new WorkspaceResponse(
                workspace.getId().value(),
                workspace.getName(),
                workspace.getDescription(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt());
    }

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
                board.getUpdatedAt());
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
                column.getUpdatedAt());
    }

    public static CardResponse toResponse(Card card) {
        return new CardResponse(
                card.getId().value(),
                card.getTitle(),
                card.getDescription(),
                card.getPosition(),
                card.getCreatedAt(),
                card.getUpdatedAt());
    }
}

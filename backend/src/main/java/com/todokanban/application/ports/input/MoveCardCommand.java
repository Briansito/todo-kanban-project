package com.todokanban.application.ports.input;

import com.todokanban.domain.model.BoardId;
import com.todokanban.domain.model.CardId;
import com.todokanban.domain.model.ColumnId;

/**
 * Command object for the {@link MoveCardUseCase}.
 *
 * <p>Immutable Java 21 record validated at construction time.</p>
 *
 * @param boardId        the board that contains both columns
 * @param cardId         the card to move
 * @param sourceColumnId the column where the card currently lives
 * @param targetColumnId the column to move the card into
 */
public record MoveCardCommand(BoardId boardId, CardId cardId,
                              ColumnId sourceColumnId, ColumnId targetColumnId) {

    public MoveCardCommand {
        if (boardId == null) {
            throw new IllegalArgumentException("BoardId must not be null");
        }
        if (cardId == null) {
            throw new IllegalArgumentException("CardId must not be null");
        }
        if (sourceColumnId == null) {
            throw new IllegalArgumentException("Source ColumnId must not be null");
        }
        if (targetColumnId == null) {
            throw new IllegalArgumentException("Target ColumnId must not be null");
        }
        if (sourceColumnId.equals(targetColumnId)) {
            throw new IllegalArgumentException("Source and target columns must be different");
        }
    }
}

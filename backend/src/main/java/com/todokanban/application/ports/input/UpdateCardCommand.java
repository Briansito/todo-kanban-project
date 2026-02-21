package com.todokanban.application.ports.input;

import com.todokanban.domain.model.BoardId;
import com.todokanban.domain.model.CardId;
import com.todokanban.domain.model.ColumnId;

/**
 * Command for updating a Card's title and/or description.
 * Null values are ignored (partial update semantics).
 */
public record UpdateCardCommand(BoardId boardId, ColumnId columnId,
                                CardId cardId, String title, String description) {
    public UpdateCardCommand {
        if (boardId == null)  throw new IllegalArgumentException("BoardId must not be null");
        if (columnId == null) throw new IllegalArgumentException("ColumnId must not be null");
        if (cardId == null)   throw new IllegalArgumentException("CardId must not be null");
    }
}

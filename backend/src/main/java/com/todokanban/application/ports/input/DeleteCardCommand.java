package com.todokanban.application.ports.input;

import com.todokanban.domain.model.BoardId;
import com.todokanban.domain.model.CardId;
import com.todokanban.domain.model.ColumnId;

/** Command for removing a Card from a Column within a Board. */
public record DeleteCardCommand(BoardId boardId, ColumnId columnId, CardId cardId) {
    public DeleteCardCommand {
        if (boardId == null)  throw new IllegalArgumentException("BoardId must not be null");
        if (columnId == null) throw new IllegalArgumentException("ColumnId must not be null");
        if (cardId == null)   throw new IllegalArgumentException("CardId must not be null");
    }
}

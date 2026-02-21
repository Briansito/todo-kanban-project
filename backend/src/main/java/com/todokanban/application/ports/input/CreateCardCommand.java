package com.todokanban.application.ports.input;

import com.todokanban.domain.model.BoardId;
import com.todokanban.domain.model.ColumnId;

/** Command for adding a Card to a Column within a Board. */
public record CreateCardCommand(BoardId boardId, ColumnId columnId,
                                String title, String description) {
    public CreateCardCommand {
        if (boardId == null)  throw new IllegalArgumentException("BoardId must not be null");
        if (columnId == null) throw new IllegalArgumentException("ColumnId must not be null");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Card title must not be blank");
    }
}

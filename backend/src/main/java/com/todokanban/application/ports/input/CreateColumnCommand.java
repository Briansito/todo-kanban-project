package com.todokanban.application.ports.input;

import com.todokanban.domain.model.BoardId;

/** Command for adding a new Column to a Board. */
public record CreateColumnCommand(BoardId boardId, String name, int position) {
    public CreateColumnCommand {
        if (boardId == null) throw new IllegalArgumentException("BoardId must not be null");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Column name must not be blank");
        if (position < 0) throw new IllegalArgumentException("Position must be non-negative");
    }
}

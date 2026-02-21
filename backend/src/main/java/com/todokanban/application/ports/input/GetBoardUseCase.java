package com.todokanban.application.ports.input;

import com.todokanban.domain.model.Board;
import com.todokanban.domain.model.BoardId;

/** Input port for loading one Board by ID (with its full columns and cards). */
public interface GetBoardUseCase {
    /**
     * Returns the Board for the given id.
     *
     * @throws java.util.NoSuchElementException if not found (→ 404 via GlobalExceptionHandler)
     */
    Board getBoard(BoardId boardId);
}

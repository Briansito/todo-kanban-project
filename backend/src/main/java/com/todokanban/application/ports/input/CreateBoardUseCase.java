package com.todokanban.application.ports.input;

import com.todokanban.domain.model.Board;

/**
 * Input port (primary port / use case) for creating a new {@link Board}.
 *
 * <p>The implementation lives in the application layer ({@code CreateBoardService}).
 * Pure Java 21 – no Spring annotations.</p>
 */
public interface CreateBoardUseCase {

    /**
     * Creates a new board inside the specified workspace.
     *
     * @param command the command carrying all data needed to create the board
     * @return the newly created and persisted {@link Board}
     * @throws IllegalArgumentException if any command field is invalid
     */
    Board createBoard(CreateBoardCommand command);
}

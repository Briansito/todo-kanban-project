package com.todokanban.application.ports.input;

import com.todokanban.domain.model.Board;

/**
 * Input port (primary port / use case) for moving a Card between Columns on a Board.
 *
 * <p>The core movement logic lives inside the {@link com.todokanban.domain.model.Board}
 * aggregate root ({@code Board#moveCard}). This use case is responsible for loading
 * the board, delegating the business operation to the aggregate, and persisting the result.</p>
 *
 * <p>Pure Java 21 – no Spring annotations.</p>
 */
public interface MoveCardUseCase {

    /**
     * Moves a card from one column to another within the same board.
     *
     * @param command the move card command containing board, card, and column identifiers
     * @return the updated {@link Board} after the card has been moved
     * @throws IllegalArgumentException if the board, card, or any column is not found
     */
    Board moveCard(MoveCardCommand command);
}

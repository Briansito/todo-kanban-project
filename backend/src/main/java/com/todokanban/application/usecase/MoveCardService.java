package com.todokanban.application.usecase;

import com.todokanban.application.ports.input.MoveCardCommand;
import com.todokanban.application.ports.input.MoveCardUseCase;
import com.todokanban.domain.model.Board;
import com.todokanban.domain.ports.output.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service that orchestrates moving a card between columns on a board.
 *
 * <p>The actual business invariants (source column exists, card exists, target column exists)
 * are enforced entirely inside {@link Board#moveCard(com.todokanban.domain.model.CardId,
 * com.todokanban.domain.model.ColumnId, com.todokanban.domain.model.ColumnId)}.
 * This service only loads the aggregate and delegates.</p>
 */
@Service
@Transactional
public class MoveCardService implements MoveCardUseCase {

    private final BoardRepository boardRepository;

    public MoveCardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board moveCard(MoveCardCommand command) {
        // Load the aggregate
        Board board = boardRepository.findById(command.boardId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Board '%s' not found".formatted(command.boardId())));

        // Delegate movement to the aggregate root – business logic stays in domain
        board.moveCard(command.cardId(), command.sourceColumnId(), command.targetColumnId());

        // Persist the updated state
        return boardRepository.save(board);
    }
}

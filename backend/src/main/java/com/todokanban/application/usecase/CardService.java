package com.todokanban.application.usecase;

import com.todokanban.application.ports.input.*;
import com.todokanban.domain.model.Board;
import com.todokanban.domain.model.Card;
import com.todokanban.domain.ports.output.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service handling all Card lifecycle operations:
 * create, update (title/description), and delete.
 *
 * <p>All business logic (adding to column, updating fields, removing) lives
 * inside the {@link Board} aggregate root. This service only orchestrates
 * load → act → save.</p>
 */
@Service
@Transactional
public class CardService implements CreateCardUseCase, UpdateCardUseCase, DeleteCardUseCase {

    private final BoardRepository boardRepository;

    public CardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board createCard(CreateCardCommand command) {
        Board board = loadBoard(command.boardId());
        Card card = Card.create(command.title(), command.description(), 0);
        board.addCardToColumn(command.columnId(), card);
        return boardRepository.save(board);
    }

    @Override
    public Board updateCard(UpdateCardCommand command) {
        Board board = loadBoard(command.boardId());
        board.updateCard(command.columnId(), command.cardId(),
                command.title(), command.description());
        return boardRepository.save(board);
    }

    @Override
    public void deleteCard(DeleteCardCommand command) {
        Board board = loadBoard(command.boardId());
        board.removeCardFromColumn(command.columnId(), command.cardId());
        boardRepository.save(board);
    }

    private Board loadBoard(com.todokanban.domain.model.BoardId boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Board '%s' not found".formatted(boardId)));
    }
}

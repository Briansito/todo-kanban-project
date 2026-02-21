package com.todokanban.application.usecase;

import com.todokanban.application.ports.input.CreateColumnCommand;
import com.todokanban.application.ports.input.CreateColumnUseCase;
import com.todokanban.domain.model.Board;
import com.todokanban.domain.model.Column;
import com.todokanban.domain.ports.output.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateColumnService implements CreateColumnUseCase {

    private final BoardRepository boardRepository;

    public CreateColumnService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board createColumn(CreateColumnCommand command) {
        Board board = boardRepository.findById(command.boardId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Board '%s' not found".formatted(command.boardId())));

        Column column = Column.create(command.name(), command.position());
        board.addColumn(column);

        return boardRepository.save(board);
    }
}

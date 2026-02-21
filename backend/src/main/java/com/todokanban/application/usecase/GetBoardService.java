package com.todokanban.application.usecase;

import com.todokanban.application.ports.input.GetBoardUseCase;
import com.todokanban.domain.model.Board;
import com.todokanban.domain.model.BoardId;
import com.todokanban.domain.ports.output.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class GetBoardService implements GetBoardUseCase {

    private final BoardRepository boardRepository;

    public GetBoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board getBoard(BoardId boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Board '%s' not found".formatted(boardId)));
    }
}

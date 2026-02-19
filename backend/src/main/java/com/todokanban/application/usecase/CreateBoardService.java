package com.todokanban.application.usecase;

import com.todokanban.application.ports.input.CreateBoardCommand;
import com.todokanban.application.ports.input.CreateBoardUseCase;
import com.todokanban.domain.model.Board;
import com.todokanban.domain.ports.output.BoardRepository;
import com.todokanban.domain.ports.output.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service that orchestrates the creation of a {@link Board}.
 *
 * <p>This class lives in the application layer. It coordinates domain objects
 * and output ports, but contains no business logic itself – that lives in the domain.</p>
 *
 * <p>Allowed to use {@code @Service} and {@code @Transactional} because it is
 * outside the domain package.</p>
 */
@Service
@Transactional
public class CreateBoardService implements CreateBoardUseCase {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;

    public CreateBoardService(BoardRepository boardRepository,
                              WorkspaceRepository workspaceRepository) {
        this.boardRepository = boardRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public Board createBoard(CreateBoardCommand command) {
        // Validate that the workspace exists
        workspaceRepository.findById(command.workspaceId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Workspace '%s' not found".formatted(command.workspaceId())));

        // Delegate board construction to the domain factory
        Board board = Board.create(command.workspaceId(), command.name(), command.description());

        // Persist via output port
        return boardRepository.save(board);
    }
}

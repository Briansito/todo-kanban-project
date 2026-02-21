package com.todokanban.infrastructure.adapter.in.rest;

import com.todokanban.application.ports.input.CreateBoardUseCase;
import com.todokanban.application.ports.input.GetBoardUseCase;
import com.todokanban.application.ports.input.MoveCardUseCase;
import com.todokanban.domain.model.Board;
import com.todokanban.domain.model.BoardId;
import com.todokanban.infrastructure.adapter.in.rest.dto.BoardRequest;
import com.todokanban.infrastructure.adapter.in.rest.dto.BoardResponse;
import com.todokanban.infrastructure.adapter.in.rest.dto.MoveCardRequest;
import com.todokanban.infrastructure.adapter.in.rest.mapper.RestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST input adapter for Board operations.
 *
 * <pre>
 * GET   /api/v1/boards/{boardId}                          → 200 OK      + BoardResponse
 * POST  /api/v1/boards                                    → 201 Created + BoardResponse
 * PATCH /api/v1/boards/{boardId}/cards/{cardId}/move      → 200 OK      + BoardResponse
 * </pre>
 *
 * <p>No business logic – delegates to use-case ports via {@link RestMapper}.</p>
 */
@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final CreateBoardUseCase createBoardUseCase;
    private final GetBoardUseCase    getBoardUseCase;
    private final MoveCardUseCase    moveCardUseCase;

    public BoardController(CreateBoardUseCase createBoardUseCase,
                           GetBoardUseCase getBoardUseCase,
                           MoveCardUseCase moveCardUseCase) {
        this.createBoardUseCase = createBoardUseCase;
        this.getBoardUseCase    = getBoardUseCase;
        this.moveCardUseCase    = moveCardUseCase;
    }

    /**
     * Loads a board with its full columns and cards.
     *
     * @param boardId the board identifier
     * @return 200 OK with the full board, or 404 if not found
     */
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable UUID boardId) {
        Board board = getBoardUseCase.getBoard(new BoardId(boardId));
        return ResponseEntity.ok(RestMapper.toResponse(board));
    }

    /**
     * Creates a new board inside a workspace.
     *
     * @return 201 Created with the full board representation
     */
    @PostMapping
    public ResponseEntity<BoardResponse> createBoard(@RequestBody BoardRequest request) {
        Board board = createBoardUseCase.createBoard(RestMapper.toCommand(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RestMapper.toResponse(board));
    }

    /**
     * Moves a card from one column to another within a board.
     *
     * @return 200 OK with the updated board representation
     */
    @PatchMapping("/{boardId}/cards/{cardId}/move")
    public ResponseEntity<BoardResponse> moveCard(
            @PathVariable UUID boardId,
            @PathVariable UUID cardId,
            @RequestBody MoveCardRequest request) {
        Board board = moveCardUseCase.moveCard(
                RestMapper.toCommand(boardId, cardId, request));
        return ResponseEntity.ok(RestMapper.toResponse(board));
    }
}

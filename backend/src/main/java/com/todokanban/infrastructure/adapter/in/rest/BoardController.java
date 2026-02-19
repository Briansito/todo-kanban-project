package com.todokanban.infrastructure.adapter.in.rest;

import com.todokanban.application.ports.input.CreateBoardUseCase;
import com.todokanban.application.ports.input.MoveCardUseCase;
import com.todokanban.domain.model.Board;
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
 * <p>Responsibilities of this controller:</p>
 * <ul>
 *   <li>Parse and validate HTTP input (headers, path vars, body)</li>
 *   <li>Delegate to use-case ports via {@link RestMapper}-produced commands</li>
 *   <li>Map domain results to HTTP responses</li>
 * </ul>
 *
 * <p>No business logic lives here — it belongs in the domain.</p>
 */
@RestController
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final CreateBoardUseCase createBoardUseCase;
    private final MoveCardUseCase moveCardUseCase;

    public BoardController(CreateBoardUseCase createBoardUseCase,
                           MoveCardUseCase moveCardUseCase) {
        this.createBoardUseCase = createBoardUseCase;
        this.moveCardUseCase = moveCardUseCase;
    }

    /**
     * Creates a new board inside a workspace.
     *
     * <p>POST /api/v1/boards</p>
     *
     * @param request the board creation payload
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
     * <p>PATCH /api/v1/boards/{boardId}/cards/{cardId}/move</p>
     *
     * @param boardId the board containing both columns
     * @param cardId  the card to move
     * @param request body with sourceColumnId and targetColumnId
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

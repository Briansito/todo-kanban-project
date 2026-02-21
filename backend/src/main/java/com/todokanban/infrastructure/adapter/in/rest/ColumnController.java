package com.todokanban.infrastructure.adapter.in.rest;

import com.todokanban.application.ports.input.CreateColumnUseCase;
import com.todokanban.domain.model.Board;
import com.todokanban.infrastructure.adapter.in.rest.dto.BoardResponse;
import com.todokanban.infrastructure.adapter.in.rest.dto.CreateColumnRequest;
import com.todokanban.infrastructure.adapter.in.rest.mapper.RestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST input adapter for Column operations.
 *
 * <pre>
 * POST /api/v1/boards/{boardId}/columns → 201 Created + BoardResponse (full board)
 * </pre>
 */
@RestController
@RequestMapping("/api/v1/boards/{boardId}/columns")
public class ColumnController {

    private final CreateColumnUseCase createColumnUseCase;

    public ColumnController(CreateColumnUseCase createColumnUseCase) {
        this.createColumnUseCase = createColumnUseCase;
    }

    /**
     * Adds a new column to the specified board.
     *
     * @param boardId the board to add the column to
     * @param request the column creation payload
     * @return 201 Created with the full updated board
     */
    @PostMapping
    public ResponseEntity<BoardResponse> createColumn(
            @PathVariable UUID boardId,
            @RequestBody CreateColumnRequest request) {
        Board board = createColumnUseCase
                .createColumn(RestMapper.toCommand(boardId, request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RestMapper.toResponse(board));
    }
}

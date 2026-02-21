package com.todokanban.infrastructure.adapter.in.rest;

import com.todokanban.application.ports.input.CreateCardUseCase;
import com.todokanban.application.ports.input.DeleteCardUseCase;
import com.todokanban.application.ports.input.UpdateCardUseCase;
import com.todokanban.domain.model.Board;
import com.todokanban.infrastructure.adapter.in.rest.dto.BoardResponse;
import com.todokanban.infrastructure.adapter.in.rest.dto.CreateCardRequest;
import com.todokanban.infrastructure.adapter.in.rest.dto.UpdateCardRequest;
import com.todokanban.infrastructure.adapter.in.rest.mapper.RestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST input adapter for Card lifecycle operations.
 *
 * <pre>
 * POST   /api/v1/boards/{boardId}/columns/{columnId}/cards             → 201 Created + BoardResponse
 * PATCH  /api/v1/boards/{boardId}/columns/{columnId}/cards/{cardId}   → 200 OK      + BoardResponse
 * DELETE /api/v1/boards/{boardId}/columns/{columnId}/cards/{cardId}   → 204 No Content
 * </pre>
 */
@RestController
@RequestMapping("/api/v1/boards/{boardId}/columns/{columnId}/cards")
public class CardController {

    private final CreateCardUseCase createCardUseCase;
    private final UpdateCardUseCase updateCardUseCase;
    private final DeleteCardUseCase deleteCardUseCase;

    public CardController(CreateCardUseCase createCardUseCase,
                          UpdateCardUseCase updateCardUseCase,
                          DeleteCardUseCase deleteCardUseCase) {
        this.createCardUseCase = createCardUseCase;
        this.updateCardUseCase = updateCardUseCase;
        this.deleteCardUseCase = deleteCardUseCase;
    }

    /**
     * Creates a new card inside the specified column.
     *
     * @return 201 Created with the full updated board
     */
    @PostMapping
    public ResponseEntity<BoardResponse> createCard(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @RequestBody CreateCardRequest request) {
        Board board = createCardUseCase
                .createCard(RestMapper.toCreateCardCommand(boardId, columnId, request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RestMapper.toResponse(board));
    }

    /**
     * Updates a card's title and/or description (partial update).
     *
     * @return 200 OK with the full updated board
     */
    @PatchMapping("/{cardId}")
    public ResponseEntity<BoardResponse> updateCard(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @PathVariable UUID cardId,
            @RequestBody UpdateCardRequest request) {
        Board board = updateCardUseCase
                .updateCard(RestMapper.toUpdateCardCommand(boardId, columnId, cardId, request));
        return ResponseEntity.ok(RestMapper.toResponse(board));
    }

    /**
     * Deletes a card from the column.
     *
     * @return 204 No Content
     */
    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable UUID boardId,
            @PathVariable UUID columnId,
            @PathVariable UUID cardId) {
        deleteCardUseCase.deleteCard(
                RestMapper.toDeleteCardCommand(boardId, columnId, cardId));
        return ResponseEntity.noContent().build();
    }
}

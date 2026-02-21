package com.todokanban.infrastructure.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todokanban.application.ports.input.CreateBoardUseCase;
import com.todokanban.application.ports.input.MoveCardUseCase;
import com.todokanban.domain.model.*;
import com.todokanban.infrastructure.config.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
// TestJsonConverter provides a Jackson converter with JavaTimeModule registered
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link BoardController} using standalone MockMvc
 * (no Spring context needed — zero startup overhead).
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BoardController")
class BoardControllerTest {

    MockMvc mockMvc;
    @Mock CreateBoardUseCase createBoardUseCase;
    @Mock MoveCardUseCase   moveCardUseCase;

    private static final UUID WORKSPACE_ID = UUID.randomUUID();
    private static final UUID BOARD_ID     = UUID.randomUUID();
    private static final UUID CARD_ID      = UUID.randomUUID();
    private static final UUID COL_TODO     = UUID.randomUUID();
    private static final UUID COL_DONE     = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new BoardController(createBoardUseCase, moveCardUseCase))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(TestJsonConverter.create())
                .build();
    }

    private Board stubBoard() {
        return Board.reconstitute(
                new BoardId(BOARD_ID), new WorkspaceId(WORKSPACE_ID),
                "Sprint Board", "desc", List.of(),
                Instant.now(), Instant.now());
    }

    @Test
    @DisplayName("POST /api/v1/boards → 201 Created")
    void createBoard_returns201() throws Exception {
        given(createBoardUseCase.createBoard(any())).willReturn(stubBoard());

        mockMvc.perform(post("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"workspaceId":"%s","name":"Sprint Board","description":"desc"}
                            """.formatted(WORKSPACE_ID)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(BOARD_ID.toString()))
                .andExpect(jsonPath("$.name").value("Sprint Board"));
    }

    @Test
    @DisplayName("POST /api/v1/boards with blank name → 400 Bad Request")
    void createBoard_blankName_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"workspaceId":"%s","name":"  "}
                            """.formatted(WORKSPACE_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /{boardId}/cards/{cardId}/move → 200 OK with body")
    void moveCard_returns200WithBody() throws Exception {
        given(moveCardUseCase.moveCard(any())).willReturn(stubBoard());

        mockMvc.perform(patch("/api/v1/boards/{bid}/cards/{cid}/move", BOARD_ID, CARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"sourceColumnId":"%s","targetColumnId":"%s"}
                            """.formatted(COL_TODO, COL_DONE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOARD_ID.toString()));
    }

    @Test
    @DisplayName("PATCH move with same source and target → 400 Bad Request")
    void moveCard_sameColumns_returns400() throws Exception {
        mockMvc.perform(patch("/api/v1/boards/{bid}/cards/{cid}/move", BOARD_ID, CARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"sourceColumnId":"%s","targetColumnId":"%s"}
                            """.formatted(COL_TODO, COL_TODO)))
                .andExpect(status().isBadRequest());
    }
}

package com.todokanban.infrastructure.adapter.in.rest;

import com.todokanban.application.ports.input.*;
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
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CardController")
class CardControllerTest {

    MockMvc mockMvc;
    @Mock CreateCardUseCase createCardUseCase;
    @Mock UpdateCardUseCase updateCardUseCase;
    @Mock DeleteCardUseCase deleteCardUseCase;

    private static final UUID BOARD_ID = UUID.randomUUID();
    private static final UUID COL_ID   = UUID.randomUUID();
    private static final UUID CARD_ID  = UUID.randomUUID();
    private static final UUID WS_ID    = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new CardController(createCardUseCase, updateCardUseCase, deleteCardUseCase))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(TestJsonConverter.create())
                .build();
    }

    private Board stubBoard() {
        return Board.reconstitute(
                new BoardId(BOARD_ID), new WorkspaceId(WS_ID),
                "Sprint Board", "desc", List.of(),
                Instant.now(), Instant.now());
    }

    @Test
    @DisplayName("POST /boards/{bid}/columns/{cid}/cards → 201 Created")
    void createCard_returns201() throws Exception {
        given(createCardUseCase.createCard(any())).willReturn(stubBoard());

        mockMvc.perform(post("/api/v1/boards/{bid}/columns/{cid}/cards", BOARD_ID, COL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"title":"New Feature","description":"Build it"}
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(BOARD_ID.toString()));
    }

    @Test
    @DisplayName("POST with blank card title → 400 Bad Request")
    void createCard_blankTitle_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/boards/{bid}/columns/{cid}/cards", BOARD_ID, COL_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"title":""}
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /boards/{bid}/columns/{cid}/cards/{card} → 200 OK")
    void updateCard_returns200() throws Exception {
        given(updateCardUseCase.updateCard(any())).willReturn(stubBoard());

        mockMvc.perform(patch("/api/v1/boards/{bid}/columns/{cid}/cards/{card}",
                        BOARD_ID, COL_ID, CARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"title":"Updated Title","description":"New desc"}
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(BOARD_ID.toString()));
    }

    @Test
    @DisplayName("DELETE /boards/{bid}/columns/{cid}/cards/{card} → 204 No Content")
    void deleteCard_returns204() throws Exception {
        doNothing().when(deleteCardUseCase).deleteCard(any());

        mockMvc.perform(delete("/api/v1/boards/{bid}/columns/{cid}/cards/{card}",
                        BOARD_ID, COL_ID, CARD_ID))
                .andExpect(status().isNoContent());
    }
}

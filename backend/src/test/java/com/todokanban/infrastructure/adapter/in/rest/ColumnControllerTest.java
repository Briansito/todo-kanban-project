package com.todokanban.infrastructure.adapter.in.rest;

import com.todokanban.application.ports.input.CreateColumnUseCase;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ColumnController")
class ColumnControllerTest {

    MockMvc mockMvc;
    @Mock CreateColumnUseCase createColumnUseCase;

    private static final UUID WORKSPACE_ID = UUID.randomUUID();
    private static final UUID BOARD_ID     = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ColumnController(createColumnUseCase))
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
    @DisplayName("POST /api/v1/boards/{boardId}/columns → 201 Created")
    void createColumn_returns201() throws Exception {
        given(createColumnUseCase.createColumn(any())).willReturn(stubBoard());

        mockMvc.perform(post("/api/v1/boards/{boardId}/columns", BOARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"name":"To Do","position":0}
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(BOARD_ID.toString()));
    }

    @Test
    @DisplayName("POST with blank column name → 400 Bad Request")
    void createColumn_blankName_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/boards/{boardId}/columns", BOARD_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"name":"","position":0}
                            """))
                .andExpect(status().isBadRequest());
    }
}

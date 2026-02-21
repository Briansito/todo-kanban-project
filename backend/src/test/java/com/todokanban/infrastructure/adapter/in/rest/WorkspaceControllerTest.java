package com.todokanban.infrastructure.adapter.in.rest;

import com.todokanban.application.ports.input.CreateWorkspaceUseCase;
import com.todokanban.domain.model.Workspace;
import com.todokanban.domain.model.WorkspaceId;
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
@DisplayName("WorkspaceController")
class WorkspaceControllerTest {

    MockMvc mockMvc;
    @Mock CreateWorkspaceUseCase createWorkspaceUseCase;

    private static final UUID WORKSPACE_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new WorkspaceController(createWorkspaceUseCase))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(TestJsonConverter.create())
                .build();
    }

    private Workspace stubWorkspace() {
        return Workspace.reconstitute(
                new WorkspaceId(WORKSPACE_ID), "My Workspace", "desc",
                List.of(), Instant.now(), Instant.now());
    }

    @Test
    @DisplayName("POST /api/v1/workspaces → 201 Created")
    void createWorkspace_returns201() throws Exception {
        given(createWorkspaceUseCase.createWorkspace(any())).willReturn(stubWorkspace());

        mockMvc.perform(post("/api/v1/workspaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"name":"My Workspace","description":"desc"}
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(WORKSPACE_ID.toString()))
                .andExpect(jsonPath("$.name").value("My Workspace"));
    }

    @Test
    @DisplayName("POST /api/v1/workspaces with blank name → 400 Bad Request")
    void createWorkspace_blankName_returns400() throws Exception {
        mockMvc.perform(post("/api/v1/workspaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"name":""}
                            """))
                .andExpect(status().isBadRequest());
    }
}

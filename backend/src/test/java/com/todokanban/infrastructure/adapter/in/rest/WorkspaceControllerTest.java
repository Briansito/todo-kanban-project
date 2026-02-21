package com.todokanban.infrastructure.adapter.in.rest;

import com.todokanban.application.ports.input.CreateWorkspaceUseCase;
import com.todokanban.application.ports.input.GetWorkspacesUseCase;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link WorkspaceController} using standalone MockMvc.
 * Security filter chain NOT active in standalone mode.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("WorkspaceController")
class WorkspaceControllerTest {

    MockMvc mockMvc;
    @Mock CreateWorkspaceUseCase createWorkspaceUseCase;
    @Mock GetWorkspacesUseCase   getWorkspacesUseCase;

    private static final UUID WORKSPACE_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new WorkspaceController(createWorkspaceUseCase, getWorkspacesUseCase))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(TestJsonConverter.create())
                .build();
    }

    private Workspace stubWorkspace() {
        return Workspace.reconstitute(
                new WorkspaceId(WORKSPACE_ID), "My Workspace", "desc",
                List.of(), Instant.now(), Instant.now());
    }

    // ── GET ───────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/v1/workspaces → 200 OK with list")
    void getWorkspaces_returns200() throws Exception {
        given(getWorkspacesUseCase.getWorkspaces()).willReturn(List.of(stubWorkspace()));

        mockMvc.perform(get("/api/v1/workspaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(WORKSPACE_ID.toString()))
                .andExpect(jsonPath("$[0].name").value("My Workspace"));
    }

    @Test
    @DisplayName("GET /api/v1/workspaces with empty DB → 200 OK empty array")
    void getWorkspaces_empty_returns200EmptyArray() throws Exception {
        given(getWorkspacesUseCase.getWorkspaces()).willReturn(List.of());

        mockMvc.perform(get("/api/v1/workspaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ── POST ──────────────────────────────────────────────────────────────────

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

package com.todokanban.infrastructure.adapter.in.rest;

import com.todokanban.application.ports.input.CreateWorkspaceUseCase;
import com.todokanban.application.ports.input.GetWorkspacesUseCase;
import com.todokanban.domain.model.Workspace;
import com.todokanban.infrastructure.adapter.in.rest.dto.WorkspaceRequest;
import com.todokanban.infrastructure.adapter.in.rest.dto.WorkspaceResponse;
import com.todokanban.infrastructure.adapter.in.rest.mapper.RestMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST input adapter for Workspace operations.
 *
 * <pre>
 * GET  /api/v1/workspaces  → 200 OK   + List&lt;WorkspaceResponse&gt;
 * POST /api/v1/workspaces  → 201 Created + WorkspaceResponse
 * </pre>
 */
@RestController
@RequestMapping("/api/v1/workspaces")
public class WorkspaceController {

    private final CreateWorkspaceUseCase createWorkspaceUseCase;
    private final GetWorkspacesUseCase   getWorkspacesUseCase;

    public WorkspaceController(CreateWorkspaceUseCase createWorkspaceUseCase,
                               GetWorkspacesUseCase getWorkspacesUseCase) {
        this.createWorkspaceUseCase = createWorkspaceUseCase;
        this.getWorkspacesUseCase   = getWorkspacesUseCase;
    }

    /**
     * Lists all workspaces.
     *
     * @return 200 OK with the ordered list of workspaces (may be empty)
     */
    @GetMapping
    public ResponseEntity<List<WorkspaceResponse>> getWorkspaces() {
        List<WorkspaceResponse> body = getWorkspacesUseCase.getWorkspaces()
                .stream()
                .map(RestMapper::toResponse)
                .toList();
        return ResponseEntity.ok(body);
    }

    /**
     * Creates a new workspace.
     *
     * @param request the workspace creation payload
     * @return 201 Created with the created workspace
     */
    @PostMapping
    public ResponseEntity<WorkspaceResponse> createWorkspace(
            @RequestBody WorkspaceRequest request) {
        Workspace workspace = createWorkspaceUseCase
                .createWorkspace(RestMapper.toCommand(request));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RestMapper.toResponse(workspace));
    }
}

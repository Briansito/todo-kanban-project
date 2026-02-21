package com.todokanban.application.usecase;

import com.todokanban.application.ports.input.GetWorkspacesUseCase;
import com.todokanban.domain.model.Workspace;
import com.todokanban.domain.ports.output.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class GetWorkspacesService implements GetWorkspacesUseCase {

    private final WorkspaceRepository workspaceRepository;

    public GetWorkspacesService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public List<Workspace> getWorkspaces() {
        return workspaceRepository.findAll();
    }
}

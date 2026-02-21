package com.todokanban.application.usecase;

import com.todokanban.application.ports.input.CreateWorkspaceCommand;
import com.todokanban.application.ports.input.CreateWorkspaceUseCase;
import com.todokanban.domain.model.Workspace;
import com.todokanban.domain.ports.output.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateWorkspaceService implements CreateWorkspaceUseCase {

    private final WorkspaceRepository workspaceRepository;

    public CreateWorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public Workspace createWorkspace(CreateWorkspaceCommand command) {
        Workspace workspace = Workspace.create(command.name(), command.description());
        return workspaceRepository.save(workspace);
    }
}

package com.todokanban.infrastructure.adapter.out.persistence.mapper;

import com.todokanban.domain.model.Workspace;
import com.todokanban.domain.model.WorkspaceId;
import com.todokanban.infrastructure.adapter.out.persistence.entity.WorkspaceEntity;

/**
 * Manual mapper for Workspace ↔ WorkspaceEntity.
 * Static utility, no Spring injection needed.
 */
public final class WorkspaceMapper {

    private WorkspaceMapper() {}

    public static Workspace toDomain(WorkspaceEntity entity) {
        return Workspace.reconstitute(
                new WorkspaceId(entity.getId()),
                entity.getName(),
                entity.getDescription(),
                // boards are loaded separately via BoardRepository to avoid eager loading
                java.util.List.of(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static WorkspaceEntity toEntity(Workspace workspace) {
        return new WorkspaceEntity(
                workspace.getId().value(),
                workspace.getName(),
                workspace.getDescription(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt()
        );
    }
}

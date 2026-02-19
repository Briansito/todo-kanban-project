package com.todokanban.infrastructure.adapter.out.persistence;

import com.todokanban.infrastructure.adapter.out.persistence.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Spring Data JPA repository for {@link BoardEntity}.
 * Infrastructure concern only.
 */
public interface BoardJpaRepository extends JpaRepository<BoardEntity, UUID> {

    /**
     * Finds all boards belonging to a given workspace.
     *
     * @param workspaceId the UUID of the workspace
     * @return list of board entities, possibly empty
     */
    List<BoardEntity> findByWorkspaceId(UUID workspaceId);
}

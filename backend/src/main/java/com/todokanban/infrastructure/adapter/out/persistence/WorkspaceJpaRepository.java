package com.todokanban.infrastructure.adapter.out.persistence;

import com.todokanban.infrastructure.adapter.out.persistence.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA repository for {@link WorkspaceEntity}.
 * Infrastructure concern only.
 */
public interface WorkspaceJpaRepository extends JpaRepository<WorkspaceEntity, UUID> {
}

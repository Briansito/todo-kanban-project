package com.todokanban.infrastructure.adapter.out.persistence;

import com.todokanban.domain.model.Workspace;
import com.todokanban.domain.model.WorkspaceId;
import com.todokanban.domain.ports.output.WorkspaceRepository;
import com.todokanban.infrastructure.adapter.out.persistence.mapper.WorkspaceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Output adapter implementing the domain {@link WorkspaceRepository} port
 * using Spring Data JPA.
 */
@Repository
public class WorkspacePersistenceAdapter implements WorkspaceRepository {

    private final WorkspaceJpaRepository jpaRepository;

    public WorkspacePersistenceAdapter(WorkspaceJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Workspace save(Workspace workspace) {
        var entity = WorkspaceMapper.toEntity(workspace);
        var saved = jpaRepository.save(entity);
        return WorkspaceMapper.toDomain(saved);
    }

    @Override
    public Optional<Workspace> findById(WorkspaceId id) {
        return jpaRepository.findById(id.value())
                .map(WorkspaceMapper::toDomain);
    }

    @Override
    public List<Workspace> findAll() {
        return jpaRepository.findAll().stream()
                .map(WorkspaceMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(WorkspaceId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(WorkspaceId id) {
        jpaRepository.deleteById(id.value());
    }
}

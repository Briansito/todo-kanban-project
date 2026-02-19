package com.todokanban.infrastructure.adapter.out.persistence;

import com.todokanban.domain.model.Board;
import com.todokanban.domain.model.BoardId;
import com.todokanban.domain.model.WorkspaceId;
import com.todokanban.domain.ports.output.BoardRepository;
import com.todokanban.infrastructure.adapter.out.persistence.entity.BoardEntity;
import com.todokanban.infrastructure.adapter.out.persistence.entity.WorkspaceEntity;
import com.todokanban.infrastructure.adapter.out.persistence.mapper.BoardMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Output adapter that implements the domain {@link BoardRepository} port
 * using Spring Data JPA.
 *
 * <p>This is the only class allowed to use Spring's {@code @Repository}
 * for the Board aggregate. It translates between domain model and
 * JPA entities via {@link BoardMapper}.</p>
 */
@Repository
public class BoardPersistenceAdapter implements BoardRepository {

    private final BoardJpaRepository boardJpaRepository;
    private final WorkspaceJpaRepository workspaceJpaRepository;

    public BoardPersistenceAdapter(BoardJpaRepository boardJpaRepository,
                                   WorkspaceJpaRepository workspaceJpaRepository) {
        this.boardJpaRepository = boardJpaRepository;
        this.workspaceJpaRepository = workspaceJpaRepository;
    }

    @Override
    public Board save(Board board) {
        WorkspaceEntity workspaceEntity = workspaceJpaRepository
                .findById(board.getWorkspaceId().value())
                .orElseThrow(() -> new IllegalStateException(
                        "Workspace '%s' not found during board persistence"
                                .formatted(board.getWorkspaceId())));

        BoardEntity entity = BoardMapper.toEntity(board, workspaceEntity);
        BoardEntity saved = boardJpaRepository.save(entity);
        return BoardMapper.toDomain(saved);
    }

    @Override
    public Optional<Board> findById(BoardId id) {
        return boardJpaRepository.findById(id.value())
                .map(BoardMapper::toDomain);
    }

    @Override
    public List<Board> findByWorkspaceId(WorkspaceId workspaceId) {
        return boardJpaRepository.findByWorkspaceId(workspaceId.value())
                .stream()
                .map(BoardMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsById(BoardId id) {
        return boardJpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(BoardId id) {
        boardJpaRepository.deleteById(id.value());
    }
}

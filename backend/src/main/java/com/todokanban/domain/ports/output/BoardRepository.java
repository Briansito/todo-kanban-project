package com.todokanban.domain.ports.output;

import com.todokanban.domain.model.Board;
import com.todokanban.domain.model.BoardId;
import com.todokanban.domain.model.WorkspaceId;

import java.util.List;
import java.util.Optional;

/**
 * Output port (secondary port) for Board persistence operations.
 *
 * <p>Defines how the domain interacts with the persistence layer.
 * Implementations live in the infrastructure layer (e.g. {@code BoardJpaAdapter}).
 * Pure Java 21 – no JPA or Spring annotations.</p>
 */
public interface BoardRepository {

    /**
     * Persists a new board or updates an existing one.
     *
     * @param board the board aggregate to persist
     * @return the persisted board (may contain generated/updated metadata)
     */
    Board save(Board board);

    /**
     * Finds a board by its unique identifier.
     *
     * @param id the board identifier
     * @return an {@link Optional} containing the board if found, or empty
     */
    Optional<Board> findById(BoardId id);

    /**
     * Returns all boards belonging to a given workspace.
     *
     * @param workspaceId the workspace identifier
     * @return list of boards, possibly empty
     */
    List<Board> findByWorkspaceId(WorkspaceId workspaceId);

    /**
     * Checks whether a board with the given identifier exists.
     *
     * @param id the board identifier
     * @return {@code true} if a board with this id exists
     */
    boolean existsById(BoardId id);

    /**
     * Removes a board by its identifier.
     *
     * @param id the board identifier
     */
    void deleteById(BoardId id);
}

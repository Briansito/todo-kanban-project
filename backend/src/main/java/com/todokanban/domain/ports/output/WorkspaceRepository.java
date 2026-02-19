package com.todokanban.domain.ports.output;

import com.todokanban.domain.model.Workspace;
import com.todokanban.domain.model.WorkspaceId;

import java.util.List;
import java.util.Optional;

/**
 * Output port (secondary port) for Workspace persistence operations.
 *
 * <p>Defines how the domain interacts with the persistence layer.
 * Implementations live in the infrastructure layer.
 * Pure Java 21 – no JPA or Spring annotations.</p>
 */
public interface WorkspaceRepository {

    /**
     * Persists a new workspace or updates an existing one.
     *
     * @param workspace the workspace to persist
     * @return the persisted workspace
     */
    Workspace save(Workspace workspace);

    /**
     * Finds a workspace by its unique identifier.
     *
     * @param id the workspace identifier
     * @return an {@link Optional} containing the workspace if found, or empty
     */
    Optional<Workspace> findById(WorkspaceId id);

    /**
     * Returns all workspaces in the system.
     *
     * @return list of all workspaces, possibly empty
     */
    List<Workspace> findAll();

    /**
     * Checks whether a workspace with the given identifier exists.
     *
     * @param id the workspace identifier
     * @return {@code true} if a workspace with this id exists
     */
    boolean existsById(WorkspaceId id);

    /**
     * Removes a workspace by its identifier.
     *
     * @param id the workspace identifier
     */
    void deleteById(WorkspaceId id);
}

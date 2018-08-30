package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.ProjectHistoryDTO;
import java.util.List;

/**
 * Service Interface for managing ProjectHistory.
 */
public interface ProjectHistoryService {

    /**
     * Save a projectHistory.
     *
     * @param projectHistoryDTO the entity to save
     * @return the persisted entity
     */
    ProjectHistoryDTO save(ProjectHistoryDTO projectHistoryDTO);

    /**
     * Get all the projectHistories.
     *
     * @return the list of entities
     */
    List<ProjectHistoryDTO> findAll();

    /**
     * Get the "id" projectHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ProjectHistoryDTO findOne(Long id);

    /**
     * Delete the "id" projectHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

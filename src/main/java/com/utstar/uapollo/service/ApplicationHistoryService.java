package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.ApplicationHistoryDTO;
import java.util.List;

/**
 * Service Interface for managing ApplicationHistory.
 */
public interface ApplicationHistoryService {

    /**
     * Save a applicationHistory.
     *
     * @param applicationHistoryDTO the entity to save
     * @return the persisted entity
     */
    ApplicationHistoryDTO save(ApplicationHistoryDTO applicationHistoryDTO);

    /**
     * Get all the applicationHistories.
     *
     * @return the list of entities
     */
    List<ApplicationHistoryDTO> findAll();

    /**
     * Get the "id" applicationHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ApplicationHistoryDTO findOne(Long id);

    /**
     * Delete the "id" applicationHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

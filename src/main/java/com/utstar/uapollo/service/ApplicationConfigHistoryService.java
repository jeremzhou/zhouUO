package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.ApplicationConfigHistoryDTO;
import java.util.List;

/**
 * Service Interface for managing ApplicationConfigHistory.
 */
public interface ApplicationConfigHistoryService {

    /**
     * Save a applicationConfigHistory.
     *
     * @param applicationConfigHistoryDTO the entity to save
     * @return the persisted entity
     */
    ApplicationConfigHistoryDTO save(ApplicationConfigHistoryDTO applicationConfigHistoryDTO);

    /**
     * Get all the applicationConfigHistories.
     *
     * @return the list of entities
     */
    List<ApplicationConfigHistoryDTO> findAll();

    /**
     * Get the "id" applicationConfigHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ApplicationConfigHistoryDTO findOne(Long id);

    /**
     * Delete the "id" applicationConfigHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

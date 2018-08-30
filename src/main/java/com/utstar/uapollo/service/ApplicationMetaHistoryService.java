package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.ApplicationMetaHistoryDTO;
import java.util.List;

/**
 * Service Interface for managing ApplicationMetaHistory.
 */
public interface ApplicationMetaHistoryService {

    /**
     * Save a applicationMetaHistory.
     *
     * @param applicationMetaHistoryDTO the entity to save
     * @return the persisted entity
     */
    ApplicationMetaHistoryDTO save(ApplicationMetaHistoryDTO applicationMetaHistoryDTO);

    /**
     * Get all the applicationMetaHistories.
     *
     * @return the list of entities
     */
    List<ApplicationMetaHistoryDTO> findAll();

    /**
     * Get the "id" applicationMetaHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ApplicationMetaHistoryDTO findOne(Long id);

    /**
     * Delete the "id" applicationMetaHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

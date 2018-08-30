package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.PrivateConfigHistoryDTO;
import java.util.List;

/**
 * Service Interface for managing PrivateConfigHistory.
 */
public interface PrivateConfigHistoryService {

    /**
     * Save a privateConfigHistory.
     *
     * @param privateConfigHistoryDTO the entity to save
     * @return the persisted entity
     */
    PrivateConfigHistoryDTO save(PrivateConfigHistoryDTO privateConfigHistoryDTO);

    /**
     * Get all the privateConfigHistories.
     *
     * @return the list of entities
     */
    List<PrivateConfigHistoryDTO> findAll();

    /**
     * Get the "id" privateConfigHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    PrivateConfigHistoryDTO findOne(Long id);

    /**
     * Delete the "id" privateConfigHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

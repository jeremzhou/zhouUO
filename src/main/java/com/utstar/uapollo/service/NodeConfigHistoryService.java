package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.NodeConfigHistoryDTO;
import java.util.List;

/**
 * Service Interface for managing NodeConfigHistory.
 */
public interface NodeConfigHistoryService {

    /**
     * Save a nodeConfigHistory.
     *
     * @param nodeConfigHistoryDTO the entity to save
     * @return the persisted entity
     */
    NodeConfigHistoryDTO save(NodeConfigHistoryDTO nodeConfigHistoryDTO);

    /**
     * Get all the nodeConfigHistories.
     *
     * @return the list of entities
     */
    List<NodeConfigHistoryDTO> findAll();

    /**
     * Get the "id" nodeConfigHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    NodeConfigHistoryDTO findOne(Long id);

    /**
     * Delete the "id" nodeConfigHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

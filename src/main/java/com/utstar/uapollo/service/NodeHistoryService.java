package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.NodeHistoryDTO;
import java.util.List;

/**
 * Service Interface for managing NodeHistory.
 */
public interface NodeHistoryService {

    /**
     * Save a nodeHistory.
     *
     * @param nodeHistoryDTO the entity to save
     * @return the persisted entity
     */
    NodeHistoryDTO save(NodeHistoryDTO nodeHistoryDTO);

    /**
     * Get all the nodeHistories.
     *
     * @return the list of entities
     */
    List<NodeHistoryDTO> findAll();

    /**
     * Get the "id" nodeHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    NodeHistoryDTO findOne(Long id);

    /**
     * Delete the "id" nodeHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.ServerHistoryDTO;
import java.util.List;

/**
 * Service Interface for managing ServerHistory.
 */
public interface ServerHistoryService {

    /**
     * Save a serverHistory.
     *
     * @param serverHistoryDTO the entity to save
     * @return the persisted entity
     */
    ServerHistoryDTO save(ServerHistoryDTO serverHistoryDTO);

    /**
     * Get all the serverHistories.
     *
     * @return the list of entities
     */
    List<ServerHistoryDTO> findAll();

    /**
     * Get the "id" serverHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ServerHistoryDTO findOne(Long id);

    /**
     * Delete the "id" serverHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

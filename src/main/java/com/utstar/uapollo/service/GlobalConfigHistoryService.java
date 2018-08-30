package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.GlobalConfigHistoryDTO;
import java.util.List;

/**
 * Service Interface for managing GlobalConfigHistory.
 */
public interface GlobalConfigHistoryService {

    /**
     * Save a globalConfigHistory.
     *
     * @param globalConfigHistoryDTO the entity to save
     * @return the persisted entity
     */
    GlobalConfigHistoryDTO save(GlobalConfigHistoryDTO globalConfigHistoryDTO);

    /**
     * Get all the globalConfigHistories.
     *
     * @return the list of entities
     */
    List<GlobalConfigHistoryDTO> findAll();

    /**
     * Get the "id" globalConfigHistory.
     *
     * @param id the id of the entity
     * @return the entity
     */
    GlobalConfigHistoryDTO findOne(Long id);

    /**
     * Delete the "id" globalConfigHistory.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.NodeConfigDTO;
import java.util.List;

/**
 * Service Interface for managing NodeConfig.
 */
public interface NodeConfigService {

    /**
     * Save a nodeConfig.
     *
     * @param nodeConfigDTO the entity to save
     * @return the persisted entity
     */
    NodeConfigDTO save(NodeConfigDTO nodeConfigDTO);

    /**
     * Get all the nodeConfigs.
     *
     * @return the list of entities
     */
    List<NodeConfigDTO> findAll();

    /**
     * Get the "id" nodeConfig.
     *
     * @param id the id of the entity
     * @return the entity
     */
    NodeConfigDTO findOne(Long id);

    /**
     * Delete the "id" nodeConfig.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

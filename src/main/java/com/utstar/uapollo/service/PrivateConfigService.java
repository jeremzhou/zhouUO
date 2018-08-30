package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.PrivateConfigDTO;
import java.util.List;

/**
 * Service Interface for managing PrivateConfig.
 */
public interface PrivateConfigService {

    /**
     * Save a privateConfig.
     *
     * @param privateConfigDTO the entity to save
     * @return the persisted entity
     */
    PrivateConfigDTO save(PrivateConfigDTO privateConfigDTO);

    /**
     * Get all the privateConfigs.
     *
     * @return the list of entities
     */
    List<PrivateConfigDTO> findAll();

    /**
     * Get the "id" privateConfig.
     *
     * @param id the id of the entity
     * @return the entity
     */
    PrivateConfigDTO findOne(Long id);

    /**
     * Delete the "id" privateConfig.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

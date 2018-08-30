package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.GlobalConfigDTO;
import java.util.List;

/**
 * Service Interface for managing GlobalConfig.
 */
public interface GlobalConfigService {

    /**
     * Save a globalConfig.
     *
     * @param globalConfigDTO the entity to save
     * @return the persisted entity
     */
    GlobalConfigDTO save(GlobalConfigDTO globalConfigDTO);

    /**
     * Get all the globalConfigs.
     *
     * @return the list of entities
     */
    List<GlobalConfigDTO> findAll();

    /**
     * Get the "id" globalConfig.
     *
     * @param id the id of the entity
     * @return the entity
     */
    GlobalConfigDTO findOne(Long id);

    /**
     * Delete the "id" globalConfig.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

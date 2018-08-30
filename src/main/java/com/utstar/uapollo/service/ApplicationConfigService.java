package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.ApplicationConfigDTO;
import java.util.List;

/**
 * Service Interface for managing ApplicationConfig.
 */
public interface ApplicationConfigService {

    /**
     * Save a applicationConfig.
     *
     * @param applicationConfigDTO the entity to save
     * @return the persisted entity
     */
    ApplicationConfigDTO save(ApplicationConfigDTO applicationConfigDTO);

    /**
     * Get all the applicationConfigs.
     *
     * @return the list of entities
     */
    List<ApplicationConfigDTO> findAll();

    /**
     * Get the "id" applicationConfig.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ApplicationConfigDTO findOne(Long id);

    /**
     * Delete the "id" applicationConfig.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

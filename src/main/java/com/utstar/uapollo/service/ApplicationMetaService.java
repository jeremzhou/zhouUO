package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.ApplicationMetaDTO;
import java.util.List;

/**
 * Service Interface for managing ApplicationMeta.
 */
public interface ApplicationMetaService {

    /**
     * Save a applicationMeta.
     *
     * @param applicationMetaDTO the entity to save
     * @return the persisted entity
     */
    ApplicationMetaDTO save(ApplicationMetaDTO applicationMetaDTO);

    /**
     * Get all the applicationMetas.
     *
     * @return the list of entities
     */
    List<ApplicationMetaDTO> findAll();

    /**
     * Get the "id" applicationMeta.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ApplicationMetaDTO findOne(Long id);

    /**
     * Delete the "id" applicationMeta.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

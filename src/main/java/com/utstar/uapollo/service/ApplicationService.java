package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.ApplicationDTO;
import java.util.List;

/**
 * Service Interface for managing Application.
 */
public interface ApplicationService {

    /**
     * Save a application.
     *
     * @param applicationDTO the entity to save
     * @return the persisted entity
     */
    ApplicationDTO save(ApplicationDTO applicationDTO);

    /**
     * Get all the applications.
     *
     * @return the list of entities
     */
    List<ApplicationDTO> findAll();

    /**
     * Get the "id" application.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ApplicationDTO findOne(Long id);

    /**
     * Delete the "id" application.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

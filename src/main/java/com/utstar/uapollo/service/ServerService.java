package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.ServerDTO;
import java.util.List;

/**
 * Service Interface for managing Server.
 */
public interface ServerService {

    /**
     * Save a server.
     *
     * @param serverDTO the entity to save
     * @return the persisted entity
     */
    ServerDTO save(ServerDTO serverDTO);

    /**
     * Get all the servers.
     *
     * @return the list of entities
     */
    List<ServerDTO> findAll();

    /**
     * Get the "id" server.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ServerDTO findOne(Long id);

    /**
     * Delete the "id" server.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

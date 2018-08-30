package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.NodeDTO;
import java.util.List;

/**
 * Service Interface for managing Node.
 */
public interface NodeService {

    /**
     * Save a node.
     *
     * @param nodeDTO the entity to save
     * @return the persisted entity
     */
    NodeDTO save(NodeDTO nodeDTO);

    /**
     * Get all the nodes.
     *
     * @return the list of entities
     */
    List<NodeDTO> findAll();

    /**
     * Get the "id" node.
     *
     * @param id the id of the entity
     * @return the entity
     */
    NodeDTO findOne(Long id);

    /**
     * Delete the "id" node.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

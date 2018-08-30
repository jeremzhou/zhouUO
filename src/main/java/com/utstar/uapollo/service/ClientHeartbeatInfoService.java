package com.utstar.uapollo.service;

import com.utstar.uapollo.service.dto.ClientHeartbeatInfoDTO;
import java.util.List;

/**
 * Service Interface for managing ClientHeartbeatInfo.
 */
public interface ClientHeartbeatInfoService {

    /**
     * Save a clientHeartbeatInfo.
     *
     * @param clientHeartbeatInfoDTO the entity to save
     * @return the persisted entity
     */
    ClientHeartbeatInfoDTO save(ClientHeartbeatInfoDTO clientHeartbeatInfoDTO);

    /**
     * Get all the clientHeartbeatInfos.
     *
     * @return the list of entities
     */
    List<ClientHeartbeatInfoDTO> findAll();

    /**
     * Get the "id" clientHeartbeatInfo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    ClientHeartbeatInfoDTO findOne(Long id);

    /**
     * Delete the "id" clientHeartbeatInfo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}

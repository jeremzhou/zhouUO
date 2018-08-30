package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.ServerService;
import com.utstar.uapollo.domain.Server;
import com.utstar.uapollo.repository.ServerRepository;
import com.utstar.uapollo.service.dto.ServerDTO;
import com.utstar.uapollo.service.mapper.ServerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Server.
 */
@Service
@Transactional
public class ServerServiceImpl implements ServerService {

    private final Logger log = LoggerFactory.getLogger(ServerServiceImpl.class);

    private final ServerRepository serverRepository;

    private final ServerMapper serverMapper;

    public ServerServiceImpl(ServerRepository serverRepository, ServerMapper serverMapper) {
        this.serverRepository = serverRepository;
        this.serverMapper = serverMapper;
    }

    /**
     * Save a server.
     *
     * @param serverDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ServerDTO save(ServerDTO serverDTO) {
        log.debug("Request to save Server : {}", serverDTO);
        Server server = serverMapper.toEntity(serverDTO);
        server = serverRepository.save(server);
        return serverMapper.toDto(server);
    }

    /**
     * Get all the servers.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServerDTO> findAll() {
        log.debug("Request to get all Servers");
        return serverRepository.findAll().stream()
            .map(serverMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one server by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ServerDTO findOne(Long id) {
        log.debug("Request to get Server : {}", id);
        Server server = serverRepository.findOne(id);
        return serverMapper.toDto(server);
    }

    /**
     * Delete the server by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Server : {}", id);
        serverRepository.delete(id);
    }
}

package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.ServerHistoryService;
import com.utstar.uapollo.domain.ServerHistory;
import com.utstar.uapollo.repository.ServerHistoryRepository;
import com.utstar.uapollo.service.dto.ServerHistoryDTO;
import com.utstar.uapollo.service.mapper.ServerHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ServerHistory.
 */
@Service
@Transactional
public class ServerHistoryServiceImpl implements ServerHistoryService {

    private final Logger log = LoggerFactory.getLogger(ServerHistoryServiceImpl.class);

    private final ServerHistoryRepository serverHistoryRepository;

    private final ServerHistoryMapper serverHistoryMapper;

    public ServerHistoryServiceImpl(ServerHistoryRepository serverHistoryRepository, ServerHistoryMapper serverHistoryMapper) {
        this.serverHistoryRepository = serverHistoryRepository;
        this.serverHistoryMapper = serverHistoryMapper;
    }

    /**
     * Save a serverHistory.
     *
     * @param serverHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ServerHistoryDTO save(ServerHistoryDTO serverHistoryDTO) {
        log.debug("Request to save ServerHistory : {}", serverHistoryDTO);
        ServerHistory serverHistory = serverHistoryMapper.toEntity(serverHistoryDTO);
        serverHistory = serverHistoryRepository.save(serverHistory);
        return serverHistoryMapper.toDto(serverHistory);
    }

    /**
     * Get all the serverHistories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServerHistoryDTO> findAll() {
        log.debug("Request to get all ServerHistories");
        return serverHistoryRepository.findAll().stream()
            .map(serverHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one serverHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ServerHistoryDTO findOne(Long id) {
        log.debug("Request to get ServerHistory : {}", id);
        ServerHistory serverHistory = serverHistoryRepository.findOne(id);
        return serverHistoryMapper.toDto(serverHistory);
    }

    /**
     * Delete the serverHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServerHistory : {}", id);
        serverHistoryRepository.delete(id);
    }
}

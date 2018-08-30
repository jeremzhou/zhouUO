package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.NodeHistoryService;
import com.utstar.uapollo.domain.NodeHistory;
import com.utstar.uapollo.repository.NodeHistoryRepository;
import com.utstar.uapollo.service.dto.NodeHistoryDTO;
import com.utstar.uapollo.service.mapper.NodeHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing NodeHistory.
 */
@Service
@Transactional
public class NodeHistoryServiceImpl implements NodeHistoryService {

    private final Logger log = LoggerFactory.getLogger(NodeHistoryServiceImpl.class);

    private final NodeHistoryRepository nodeHistoryRepository;

    private final NodeHistoryMapper nodeHistoryMapper;

    public NodeHistoryServiceImpl(NodeHistoryRepository nodeHistoryRepository, NodeHistoryMapper nodeHistoryMapper) {
        this.nodeHistoryRepository = nodeHistoryRepository;
        this.nodeHistoryMapper = nodeHistoryMapper;
    }

    /**
     * Save a nodeHistory.
     *
     * @param nodeHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public NodeHistoryDTO save(NodeHistoryDTO nodeHistoryDTO) {
        log.debug("Request to save NodeHistory : {}", nodeHistoryDTO);
        NodeHistory nodeHistory = nodeHistoryMapper.toEntity(nodeHistoryDTO);
        nodeHistory = nodeHistoryRepository.save(nodeHistory);
        return nodeHistoryMapper.toDto(nodeHistory);
    }

    /**
     * Get all the nodeHistories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NodeHistoryDTO> findAll() {
        log.debug("Request to get all NodeHistories");
        return nodeHistoryRepository.findAll().stream()
            .map(nodeHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one nodeHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NodeHistoryDTO findOne(Long id) {
        log.debug("Request to get NodeHistory : {}", id);
        NodeHistory nodeHistory = nodeHistoryRepository.findOne(id);
        return nodeHistoryMapper.toDto(nodeHistory);
    }

    /**
     * Delete the nodeHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NodeHistory : {}", id);
        nodeHistoryRepository.delete(id);
    }
}

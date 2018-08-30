package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.NodeConfigHistoryService;
import com.utstar.uapollo.domain.NodeConfigHistory;
import com.utstar.uapollo.repository.NodeConfigHistoryRepository;
import com.utstar.uapollo.service.dto.NodeConfigHistoryDTO;
import com.utstar.uapollo.service.mapper.NodeConfigHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing NodeConfigHistory.
 */
@Service
@Transactional
public class NodeConfigHistoryServiceImpl implements NodeConfigHistoryService {

    private final Logger log = LoggerFactory.getLogger(NodeConfigHistoryServiceImpl.class);

    private final NodeConfigHistoryRepository nodeConfigHistoryRepository;

    private final NodeConfigHistoryMapper nodeConfigHistoryMapper;

    public NodeConfigHistoryServiceImpl(NodeConfigHistoryRepository nodeConfigHistoryRepository, NodeConfigHistoryMapper nodeConfigHistoryMapper) {
        this.nodeConfigHistoryRepository = nodeConfigHistoryRepository;
        this.nodeConfigHistoryMapper = nodeConfigHistoryMapper;
    }

    /**
     * Save a nodeConfigHistory.
     *
     * @param nodeConfigHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public NodeConfigHistoryDTO save(NodeConfigHistoryDTO nodeConfigHistoryDTO) {
        log.debug("Request to save NodeConfigHistory : {}", nodeConfigHistoryDTO);
        NodeConfigHistory nodeConfigHistory = nodeConfigHistoryMapper.toEntity(nodeConfigHistoryDTO);
        nodeConfigHistory = nodeConfigHistoryRepository.save(nodeConfigHistory);
        return nodeConfigHistoryMapper.toDto(nodeConfigHistory);
    }

    /**
     * Get all the nodeConfigHistories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NodeConfigHistoryDTO> findAll() {
        log.debug("Request to get all NodeConfigHistories");
        return nodeConfigHistoryRepository.findAll().stream()
            .map(nodeConfigHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one nodeConfigHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NodeConfigHistoryDTO findOne(Long id) {
        log.debug("Request to get NodeConfigHistory : {}", id);
        NodeConfigHistory nodeConfigHistory = nodeConfigHistoryRepository.findOne(id);
        return nodeConfigHistoryMapper.toDto(nodeConfigHistory);
    }

    /**
     * Delete the nodeConfigHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NodeConfigHistory : {}", id);
        nodeConfigHistoryRepository.delete(id);
    }
}

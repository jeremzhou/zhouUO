package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.NodeConfigService;
import com.utstar.uapollo.domain.NodeConfig;
import com.utstar.uapollo.repository.NodeConfigRepository;
import com.utstar.uapollo.service.dto.NodeConfigDTO;
import com.utstar.uapollo.service.mapper.NodeConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing NodeConfig.
 */
@Service
@Transactional
public class NodeConfigServiceImpl implements NodeConfigService {

    private final Logger log = LoggerFactory.getLogger(NodeConfigServiceImpl.class);

    private final NodeConfigRepository nodeConfigRepository;

    private final NodeConfigMapper nodeConfigMapper;

    public NodeConfigServiceImpl(NodeConfigRepository nodeConfigRepository, NodeConfigMapper nodeConfigMapper) {
        this.nodeConfigRepository = nodeConfigRepository;
        this.nodeConfigMapper = nodeConfigMapper;
    }

    /**
     * Save a nodeConfig.
     *
     * @param nodeConfigDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public NodeConfigDTO save(NodeConfigDTO nodeConfigDTO) {
        log.debug("Request to save NodeConfig : {}", nodeConfigDTO);
        NodeConfig nodeConfig = nodeConfigMapper.toEntity(nodeConfigDTO);
        nodeConfig = nodeConfigRepository.save(nodeConfig);
        return nodeConfigMapper.toDto(nodeConfig);
    }

    /**
     * Get all the nodeConfigs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NodeConfigDTO> findAll() {
        log.debug("Request to get all NodeConfigs");
        return nodeConfigRepository.findAll().stream()
            .map(nodeConfigMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one nodeConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NodeConfigDTO findOne(Long id) {
        log.debug("Request to get NodeConfig : {}", id);
        NodeConfig nodeConfig = nodeConfigRepository.findOne(id);
        return nodeConfigMapper.toDto(nodeConfig);
    }

    /**
     * Delete the nodeConfig by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete NodeConfig : {}", id);
        nodeConfigRepository.delete(id);
    }
}

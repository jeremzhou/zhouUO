package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.NodeService;
import com.utstar.uapollo.domain.Node;
import com.utstar.uapollo.repository.NodeRepository;
import com.utstar.uapollo.service.dto.NodeDTO;
import com.utstar.uapollo.service.mapper.NodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Node.
 */
@Service
@Transactional
public class NodeServiceImpl implements NodeService {

    private final Logger log = LoggerFactory.getLogger(NodeServiceImpl.class);

    private final NodeRepository nodeRepository;

    private final NodeMapper nodeMapper;

    public NodeServiceImpl(NodeRepository nodeRepository, NodeMapper nodeMapper) {
        this.nodeRepository = nodeRepository;
        this.nodeMapper = nodeMapper;
    }

    /**
     * Save a node.
     *
     * @param nodeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public NodeDTO save(NodeDTO nodeDTO) {
        log.debug("Request to save Node : {}", nodeDTO);
        Node node = nodeMapper.toEntity(nodeDTO);
        node = nodeRepository.save(node);
        return nodeMapper.toDto(node);
    }

    /**
     * Get all the nodes.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<NodeDTO> findAll() {
        log.debug("Request to get all Nodes");
        return nodeRepository.findAll().stream()
            .map(nodeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one node by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public NodeDTO findOne(Long id) {
        log.debug("Request to get Node : {}", id);
        Node node = nodeRepository.findOne(id);
        return nodeMapper.toDto(node);
    }

    /**
     * Delete the node by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Node : {}", id);
        nodeRepository.delete(id);
    }
}

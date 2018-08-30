/**
 * created on 2018年4月23日 下午3:04:30
 */
package com.utstar.uapollo.custom.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utstar.uapollo.custom.repositry.NodeConfigCustomRepository;
import com.utstar.uapollo.custom.service.NodeConfigCustomService;
import com.utstar.uapollo.domain.NodeConfig;
import com.utstar.uapollo.service.dto.NodeConfigDTO;
import com.utstar.uapollo.service.impl.NodeConfigServiceImpl;
import com.utstar.uapollo.service.mapper.NodeConfigMapper;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
@Service
@Transactional
public class NodeConfigCustomServiceImpl implements NodeConfigCustomService {

    private final Logger log = LoggerFactory.getLogger(NodeConfigServiceImpl.class);

    private final NodeConfigCustomRepository nodeConfigCustomRepository;

    private final NodeConfigMapper nodeConfigMapper;

    public NodeConfigCustomServiceImpl(NodeConfigCustomRepository nodeConfigCustomRepository,
            NodeConfigMapper nodeConfigMapper) {
        this.nodeConfigCustomRepository = nodeConfigCustomRepository;
        this.nodeConfigMapper = nodeConfigMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<NodeConfigDTO> findByApplicationMetaId(Long applicationMetaId) {

        log.debug("Request to get all NodeConfigs by applicationMetaId: {}", applicationMetaId);
        return nodeConfigCustomRepository.findByApplicationMetaId(applicationMetaId).stream()
                .map(nodeConfigMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NodeConfigDTO> findByApplicationMetaIdAndNodeId(Long applicationMetaId,
            Long nodeId) {

        log.debug("Request to get all NodeConfigs by applicationMetaId: {} and nodeId: {}",
                applicationMetaId, nodeId);
        return nodeConfigCustomRepository
                .findByApplicationMetaIdAndNodeId(applicationMetaId, nodeId).stream()
                .map(nodeConfigMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NodeConfigDTO> findByApplicationMetaIdAndApplicationId(Long applicationMetaId,
            Long applicationId) {

        log.debug("Request to get all NodeConfigs by applicationMetaId: {} and applicationId: {}",
                applicationMetaId, applicationId);
        return nodeConfigCustomRepository
                .findByApplicationMetaIdAndApplicationId(applicationMetaId, applicationId).stream()
                .map(nodeConfigMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NodeConfigDTO> findByApplicationMetaIdAndNodeIdAndKey(Long applicationMetaId,
            Long nodeId, String key) {

        log.debug("Request to get the NodeConfig by applicationMetaId: {} nodeId: {} and key: {} ",
                applicationMetaId, nodeId, key);
        Optional<NodeConfig> existingNodeConfig = nodeConfigCustomRepository
                .findByApplicationMetaIdAndNodeIdAndKey(applicationMetaId, nodeId, key);
        return existingNodeConfig.map((value) -> nodeConfigMapper.toDto(value));
    }
}

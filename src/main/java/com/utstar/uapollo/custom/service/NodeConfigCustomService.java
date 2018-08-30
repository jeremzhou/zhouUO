package com.utstar.uapollo.custom.service;

import java.util.List;
import java.util.Optional;

import com.utstar.uapollo.service.dto.NodeConfigDTO;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
public interface NodeConfigCustomService {

    List<NodeConfigDTO> findByApplicationMetaId(Long applicationMetaId);

    List<NodeConfigDTO> findByApplicationMetaIdAndNodeId(Long applicationMetaId, Long nodeId);
    
    List<NodeConfigDTO> findByApplicationMetaIdAndApplicationId(Long applicationMetaId, Long applicationId);

    Optional<NodeConfigDTO> findByApplicationMetaIdAndNodeIdAndKey(Long applicationMetaId,
            Long nodeId, String key);
}

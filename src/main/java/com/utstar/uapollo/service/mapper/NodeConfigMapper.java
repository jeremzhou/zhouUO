package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.NodeConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity NodeConfig and its DTO NodeConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {ApplicationMetaMapper.class, NodeMapper.class})
public interface NodeConfigMapper extends EntityMapper<NodeConfigDTO, NodeConfig> {

    @Mapping(source = "applicationMeta.id", target = "applicationMetaId")
    @Mapping(source = "node.id", target = "nodeId")
    NodeConfigDTO toDto(NodeConfig nodeConfig);

    @Mapping(source = "applicationMetaId", target = "applicationMeta")
    @Mapping(source = "nodeId", target = "node")
    NodeConfig toEntity(NodeConfigDTO nodeConfigDTO);

    default NodeConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        NodeConfig nodeConfig = new NodeConfig();
        nodeConfig.setId(id);
        return nodeConfig;
    }
}

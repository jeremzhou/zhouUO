package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.NodeConfigHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity NodeConfigHistory and its DTO NodeConfigHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NodeConfigHistoryMapper extends EntityMapper<NodeConfigHistoryDTO, NodeConfigHistory> {



    default NodeConfigHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        NodeConfigHistory nodeConfigHistory = new NodeConfigHistory();
        nodeConfigHistory.setId(id);
        return nodeConfigHistory;
    }
}

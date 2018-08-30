package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.NodeHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity NodeHistory and its DTO NodeHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NodeHistoryMapper extends EntityMapper<NodeHistoryDTO, NodeHistory> {



    default NodeHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        NodeHistory nodeHistory = new NodeHistory();
        nodeHistory.setId(id);
        return nodeHistory;
    }
}

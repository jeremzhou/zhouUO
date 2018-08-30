package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.ServerHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ServerHistory and its DTO ServerHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServerHistoryMapper extends EntityMapper<ServerHistoryDTO, ServerHistory> {



    default ServerHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        ServerHistory serverHistory = new ServerHistory();
        serverHistory.setId(id);
        return serverHistory;
    }
}

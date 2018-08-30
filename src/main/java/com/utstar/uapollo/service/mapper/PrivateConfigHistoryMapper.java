package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.PrivateConfigHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PrivateConfigHistory and its DTO PrivateConfigHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PrivateConfigHistoryMapper extends EntityMapper<PrivateConfigHistoryDTO, PrivateConfigHistory> {



    default PrivateConfigHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        PrivateConfigHistory privateConfigHistory = new PrivateConfigHistory();
        privateConfigHistory.setId(id);
        return privateConfigHistory;
    }
}

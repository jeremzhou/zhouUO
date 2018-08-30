package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.GlobalConfigHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GlobalConfigHistory and its DTO GlobalConfigHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GlobalConfigHistoryMapper extends EntityMapper<GlobalConfigHistoryDTO, GlobalConfigHistory> {



    default GlobalConfigHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        GlobalConfigHistory globalConfigHistory = new GlobalConfigHistory();
        globalConfigHistory.setId(id);
        return globalConfigHistory;
    }
}

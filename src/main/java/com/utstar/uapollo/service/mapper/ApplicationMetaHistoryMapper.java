package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.ApplicationMetaHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ApplicationMetaHistory and its DTO ApplicationMetaHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicationMetaHistoryMapper extends EntityMapper<ApplicationMetaHistoryDTO, ApplicationMetaHistory> {



    default ApplicationMetaHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApplicationMetaHistory applicationMetaHistory = new ApplicationMetaHistory();
        applicationMetaHistory.setId(id);
        return applicationMetaHistory;
    }
}

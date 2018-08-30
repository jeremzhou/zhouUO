package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.ApplicationHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ApplicationHistory and its DTO ApplicationHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicationHistoryMapper extends EntityMapper<ApplicationHistoryDTO, ApplicationHistory> {



    default ApplicationHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApplicationHistory applicationHistory = new ApplicationHistory();
        applicationHistory.setId(id);
        return applicationHistory;
    }
}

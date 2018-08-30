package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.ApplicationConfigHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ApplicationConfigHistory and its DTO ApplicationConfigHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApplicationConfigHistoryMapper extends EntityMapper<ApplicationConfigHistoryDTO, ApplicationConfigHistory> {



    default ApplicationConfigHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApplicationConfigHistory applicationConfigHistory = new ApplicationConfigHistory();
        applicationConfigHistory.setId(id);
        return applicationConfigHistory;
    }
}

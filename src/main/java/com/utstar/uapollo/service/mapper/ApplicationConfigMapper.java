package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.ApplicationConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ApplicationConfig and its DTO ApplicationConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {ApplicationMapper.class})
public interface ApplicationConfigMapper extends EntityMapper<ApplicationConfigDTO, ApplicationConfig> {

    @Mapping(source = "application.id", target = "applicationId")
    ApplicationConfigDTO toDto(ApplicationConfig applicationConfig);

    @Mapping(source = "applicationId", target = "application")
    ApplicationConfig toEntity(ApplicationConfigDTO applicationConfigDTO);

    default ApplicationConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setId(id);
        return applicationConfig;
    }
}

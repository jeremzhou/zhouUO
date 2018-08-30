package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.ApplicationMetaDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ApplicationMeta and its DTO ApplicationMetaDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class})
public interface ApplicationMetaMapper extends EntityMapper<ApplicationMetaDTO, ApplicationMeta> {

    @Mapping(source = "project.id", target = "projectId")
    ApplicationMetaDTO toDto(ApplicationMeta applicationMeta);

    @Mapping(source = "projectId", target = "project")
    ApplicationMeta toEntity(ApplicationMetaDTO applicationMetaDTO);

    default ApplicationMeta fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApplicationMeta applicationMeta = new ApplicationMeta();
        applicationMeta.setId(id);
        return applicationMeta;
    }
}

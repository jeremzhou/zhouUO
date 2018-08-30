package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.ApplicationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Application and its DTO ApplicationDTO.
 */
@Mapper(componentModel = "spring", uses = {ApplicationMetaMapper.class, ServerMapper.class})
public interface ApplicationMapper extends EntityMapper<ApplicationDTO, Application> {

    @Mapping(source = "applicationMeta.id", target = "applicationMetaId")
    @Mapping(source = "server.id", target = "serverId")
    ApplicationDTO toDto(Application application);

    @Mapping(source = "applicationMetaId", target = "applicationMeta")
    @Mapping(source = "serverId", target = "server")
    Application toEntity(ApplicationDTO applicationDTO);

    default Application fromId(Long id) {
        if (id == null) {
            return null;
        }
        Application application = new Application();
        application.setId(id);
        return application;
    }
}

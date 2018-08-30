package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.PrivateConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PrivateConfig and its DTO PrivateConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {ApplicationMapper.class})
public interface PrivateConfigMapper extends EntityMapper<PrivateConfigDTO, PrivateConfig> {

    @Mapping(source = "application.id", target = "applicationId")
    PrivateConfigDTO toDto(PrivateConfig privateConfig);

    @Mapping(source = "applicationId", target = "application")
    PrivateConfig toEntity(PrivateConfigDTO privateConfigDTO);

    default PrivateConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        PrivateConfig privateConfig = new PrivateConfig();
        privateConfig.setId(id);
        return privateConfig;
    }
}

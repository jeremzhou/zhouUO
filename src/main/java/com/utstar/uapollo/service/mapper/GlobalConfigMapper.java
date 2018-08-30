package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.GlobalConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity GlobalConfig and its DTO GlobalConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {ApplicationMetaMapper.class})
public interface GlobalConfigMapper extends EntityMapper<GlobalConfigDTO, GlobalConfig> {

    @Mapping(source = "applicationMeta.id", target = "applicationMetaId")
    GlobalConfigDTO toDto(GlobalConfig globalConfig);

    @Mapping(source = "applicationMetaId", target = "applicationMeta")
    GlobalConfig toEntity(GlobalConfigDTO globalConfigDTO);

    default GlobalConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setId(id);
        return globalConfig;
    }
}

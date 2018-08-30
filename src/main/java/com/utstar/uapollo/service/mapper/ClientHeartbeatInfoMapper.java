package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.ClientHeartbeatInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ClientHeartbeatInfo and its DTO ClientHeartbeatInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientHeartbeatInfoMapper extends EntityMapper<ClientHeartbeatInfoDTO, ClientHeartbeatInfo> {



    default ClientHeartbeatInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        ClientHeartbeatInfo clientHeartbeatInfo = new ClientHeartbeatInfo();
        clientHeartbeatInfo.setId(id);
        return clientHeartbeatInfo;
    }
}

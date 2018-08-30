package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.ServerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Server and its DTO ServerDTO.
 */
@Mapper(componentModel = "spring", uses = {NodeMapper.class})
public interface ServerMapper extends EntityMapper<ServerDTO, Server> {

    @Mapping(source = "node.id", target = "nodeId")
    ServerDTO toDto(Server server);

    @Mapping(source = "nodeId", target = "node")
    Server toEntity(ServerDTO serverDTO);

    default Server fromId(Long id) {
        if (id == null) {
            return null;
        }
        Server server = new Server();
        server.setId(id);
        return server;
    }
}

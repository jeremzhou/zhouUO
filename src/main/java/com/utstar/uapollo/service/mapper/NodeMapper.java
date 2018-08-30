package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.NodeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Node and its DTO NodeDTO.
 */
@Mapper(componentModel = "spring", uses = {ProjectMapper.class})
public interface NodeMapper extends EntityMapper<NodeDTO, Node> {

    @Mapping(source = "project.id", target = "projectId")
    NodeDTO toDto(Node node);

    @Mapping(source = "projectId", target = "project")
    Node toEntity(NodeDTO nodeDTO);

    default Node fromId(Long id) {
        if (id == null) {
            return null;
        }
        Node node = new Node();
        node.setId(id);
        return node;
    }
}

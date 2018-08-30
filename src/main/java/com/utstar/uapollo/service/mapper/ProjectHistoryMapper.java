package com.utstar.uapollo.service.mapper;

import com.utstar.uapollo.domain.*;
import com.utstar.uapollo.service.dto.ProjectHistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProjectHistory and its DTO ProjectHistoryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProjectHistoryMapper extends EntityMapper<ProjectHistoryDTO, ProjectHistory> {



    default ProjectHistory fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProjectHistory projectHistory = new ProjectHistory();
        projectHistory.setId(id);
        return projectHistory;
    }
}

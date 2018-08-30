package com.utstar.uapollo.custom.service;

import java.util.List;
import java.util.Optional;

import com.utstar.uapollo.service.dto.ProjectDTO;

/**
 * @author UTSC0167
 * @date 2018年4月16日
 *
 */
public interface ProjectCustomService {

    List<ProjectDTO> findByUserId(Integer userId);

    Optional<ProjectDTO> findByName(String name);
}

/**
 * created on 2018年4月16日 下午5:13:13
 */
package com.utstar.uapollo.custom.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utstar.uapollo.custom.repositry.ProjectCustomRepository;
import com.utstar.uapollo.custom.service.ProjectCustomService;
import com.utstar.uapollo.domain.Project;
import com.utstar.uapollo.service.dto.ProjectDTO;
import com.utstar.uapollo.service.impl.ProjectServiceImpl;
import com.utstar.uapollo.service.mapper.ProjectMapper;

/**
 * @author UTSC0167
 * @date 2018年4月16日
 *
 */
@Service
@Transactional
public class ProjectCustomServiceImpl implements ProjectCustomService {

    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final ProjectCustomRepository projectCustomRepository;

    private final ProjectMapper projectMapper;

    public ProjectCustomServiceImpl(ProjectCustomRepository projectCustomRepository,
            ProjectMapper projectMapper) {
        this.projectCustomRepository = projectCustomRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> findByUserId(Integer userId) {

        log.debug("Request to get all Projects by userId: {}", userId);
        return projectCustomRepository.findByUserId(userId).stream().map(projectMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProjectDTO> findByName(String name) {

        log.debug("Request to get Project by name: {}", name);
        Optional<Project> existingProject = projectCustomRepository.findByNameIgnoreCase(name);
        return existingProject.map((value) -> (projectMapper.toDto(value)));
    }
}

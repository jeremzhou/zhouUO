package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.ProjectHistoryService;
import com.utstar.uapollo.domain.ProjectHistory;
import com.utstar.uapollo.repository.ProjectHistoryRepository;
import com.utstar.uapollo.service.dto.ProjectHistoryDTO;
import com.utstar.uapollo.service.mapper.ProjectHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ProjectHistory.
 */
@Service
@Transactional
public class ProjectHistoryServiceImpl implements ProjectHistoryService {

    private final Logger log = LoggerFactory.getLogger(ProjectHistoryServiceImpl.class);

    private final ProjectHistoryRepository projectHistoryRepository;

    private final ProjectHistoryMapper projectHistoryMapper;

    public ProjectHistoryServiceImpl(ProjectHistoryRepository projectHistoryRepository, ProjectHistoryMapper projectHistoryMapper) {
        this.projectHistoryRepository = projectHistoryRepository;
        this.projectHistoryMapper = projectHistoryMapper;
    }

    /**
     * Save a projectHistory.
     *
     * @param projectHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ProjectHistoryDTO save(ProjectHistoryDTO projectHistoryDTO) {
        log.debug("Request to save ProjectHistory : {}", projectHistoryDTO);
        ProjectHistory projectHistory = projectHistoryMapper.toEntity(projectHistoryDTO);
        projectHistory = projectHistoryRepository.save(projectHistory);
        return projectHistoryMapper.toDto(projectHistory);
    }

    /**
     * Get all the projectHistories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProjectHistoryDTO> findAll() {
        log.debug("Request to get all ProjectHistories");
        return projectHistoryRepository.findAll().stream()
            .map(projectHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one projectHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ProjectHistoryDTO findOne(Long id) {
        log.debug("Request to get ProjectHistory : {}", id);
        ProjectHistory projectHistory = projectHistoryRepository.findOne(id);
        return projectHistoryMapper.toDto(projectHistory);
    }

    /**
     * Delete the projectHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProjectHistory : {}", id);
        projectHistoryRepository.delete(id);
    }
}

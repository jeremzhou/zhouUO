package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.ApplicationHistoryService;
import com.utstar.uapollo.domain.ApplicationHistory;
import com.utstar.uapollo.repository.ApplicationHistoryRepository;
import com.utstar.uapollo.service.dto.ApplicationHistoryDTO;
import com.utstar.uapollo.service.mapper.ApplicationHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ApplicationHistory.
 */
@Service
@Transactional
public class ApplicationHistoryServiceImpl implements ApplicationHistoryService {

    private final Logger log = LoggerFactory.getLogger(ApplicationHistoryServiceImpl.class);

    private final ApplicationHistoryRepository applicationHistoryRepository;

    private final ApplicationHistoryMapper applicationHistoryMapper;

    public ApplicationHistoryServiceImpl(ApplicationHistoryRepository applicationHistoryRepository, ApplicationHistoryMapper applicationHistoryMapper) {
        this.applicationHistoryRepository = applicationHistoryRepository;
        this.applicationHistoryMapper = applicationHistoryMapper;
    }

    /**
     * Save a applicationHistory.
     *
     * @param applicationHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ApplicationHistoryDTO save(ApplicationHistoryDTO applicationHistoryDTO) {
        log.debug("Request to save ApplicationHistory : {}", applicationHistoryDTO);
        ApplicationHistory applicationHistory = applicationHistoryMapper.toEntity(applicationHistoryDTO);
        applicationHistory = applicationHistoryRepository.save(applicationHistory);
        return applicationHistoryMapper.toDto(applicationHistory);
    }

    /**
     * Get all the applicationHistories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationHistoryDTO> findAll() {
        log.debug("Request to get all ApplicationHistories");
        return applicationHistoryRepository.findAll().stream()
            .map(applicationHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one applicationHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ApplicationHistoryDTO findOne(Long id) {
        log.debug("Request to get ApplicationHistory : {}", id);
        ApplicationHistory applicationHistory = applicationHistoryRepository.findOne(id);
        return applicationHistoryMapper.toDto(applicationHistory);
    }

    /**
     * Delete the applicationHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationHistory : {}", id);
        applicationHistoryRepository.delete(id);
    }
}

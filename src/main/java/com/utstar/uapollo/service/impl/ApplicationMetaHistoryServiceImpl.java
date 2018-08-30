package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.ApplicationMetaHistoryService;
import com.utstar.uapollo.domain.ApplicationMetaHistory;
import com.utstar.uapollo.repository.ApplicationMetaHistoryRepository;
import com.utstar.uapollo.service.dto.ApplicationMetaHistoryDTO;
import com.utstar.uapollo.service.mapper.ApplicationMetaHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ApplicationMetaHistory.
 */
@Service
@Transactional
public class ApplicationMetaHistoryServiceImpl implements ApplicationMetaHistoryService {

    private final Logger log = LoggerFactory.getLogger(ApplicationMetaHistoryServiceImpl.class);

    private final ApplicationMetaHistoryRepository applicationMetaHistoryRepository;

    private final ApplicationMetaHistoryMapper applicationMetaHistoryMapper;

    public ApplicationMetaHistoryServiceImpl(ApplicationMetaHistoryRepository applicationMetaHistoryRepository, ApplicationMetaHistoryMapper applicationMetaHistoryMapper) {
        this.applicationMetaHistoryRepository = applicationMetaHistoryRepository;
        this.applicationMetaHistoryMapper = applicationMetaHistoryMapper;
    }

    /**
     * Save a applicationMetaHistory.
     *
     * @param applicationMetaHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ApplicationMetaHistoryDTO save(ApplicationMetaHistoryDTO applicationMetaHistoryDTO) {
        log.debug("Request to save ApplicationMetaHistory : {}", applicationMetaHistoryDTO);
        ApplicationMetaHistory applicationMetaHistory = applicationMetaHistoryMapper.toEntity(applicationMetaHistoryDTO);
        applicationMetaHistory = applicationMetaHistoryRepository.save(applicationMetaHistory);
        return applicationMetaHistoryMapper.toDto(applicationMetaHistory);
    }

    /**
     * Get all the applicationMetaHistories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationMetaHistoryDTO> findAll() {
        log.debug("Request to get all ApplicationMetaHistories");
        return applicationMetaHistoryRepository.findAll().stream()
            .map(applicationMetaHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one applicationMetaHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ApplicationMetaHistoryDTO findOne(Long id) {
        log.debug("Request to get ApplicationMetaHistory : {}", id);
        ApplicationMetaHistory applicationMetaHistory = applicationMetaHistoryRepository.findOne(id);
        return applicationMetaHistoryMapper.toDto(applicationMetaHistory);
    }

    /**
     * Delete the applicationMetaHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationMetaHistory : {}", id);
        applicationMetaHistoryRepository.delete(id);
    }
}

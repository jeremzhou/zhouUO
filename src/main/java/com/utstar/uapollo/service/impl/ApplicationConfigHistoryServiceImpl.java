package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.ApplicationConfigHistoryService;
import com.utstar.uapollo.domain.ApplicationConfigHistory;
import com.utstar.uapollo.repository.ApplicationConfigHistoryRepository;
import com.utstar.uapollo.service.dto.ApplicationConfigHistoryDTO;
import com.utstar.uapollo.service.mapper.ApplicationConfigHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ApplicationConfigHistory.
 */
@Service
@Transactional
public class ApplicationConfigHistoryServiceImpl implements ApplicationConfigHistoryService {

    private final Logger log = LoggerFactory.getLogger(ApplicationConfigHistoryServiceImpl.class);

    private final ApplicationConfigHistoryRepository applicationConfigHistoryRepository;

    private final ApplicationConfigHistoryMapper applicationConfigHistoryMapper;

    public ApplicationConfigHistoryServiceImpl(ApplicationConfigHistoryRepository applicationConfigHistoryRepository, ApplicationConfigHistoryMapper applicationConfigHistoryMapper) {
        this.applicationConfigHistoryRepository = applicationConfigHistoryRepository;
        this.applicationConfigHistoryMapper = applicationConfigHistoryMapper;
    }

    /**
     * Save a applicationConfigHistory.
     *
     * @param applicationConfigHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ApplicationConfigHistoryDTO save(ApplicationConfigHistoryDTO applicationConfigHistoryDTO) {
        log.debug("Request to save ApplicationConfigHistory : {}", applicationConfigHistoryDTO);
        ApplicationConfigHistory applicationConfigHistory = applicationConfigHistoryMapper.toEntity(applicationConfigHistoryDTO);
        applicationConfigHistory = applicationConfigHistoryRepository.save(applicationConfigHistory);
        return applicationConfigHistoryMapper.toDto(applicationConfigHistory);
    }

    /**
     * Get all the applicationConfigHistories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationConfigHistoryDTO> findAll() {
        log.debug("Request to get all ApplicationConfigHistories");
        return applicationConfigHistoryRepository.findAll().stream()
            .map(applicationConfigHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one applicationConfigHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ApplicationConfigHistoryDTO findOne(Long id) {
        log.debug("Request to get ApplicationConfigHistory : {}", id);
        ApplicationConfigHistory applicationConfigHistory = applicationConfigHistoryRepository.findOne(id);
        return applicationConfigHistoryMapper.toDto(applicationConfigHistory);
    }

    /**
     * Delete the applicationConfigHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationConfigHistory : {}", id);
        applicationConfigHistoryRepository.delete(id);
    }
}

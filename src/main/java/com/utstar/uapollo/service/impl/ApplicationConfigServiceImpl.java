package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.ApplicationConfigService;
import com.utstar.uapollo.domain.ApplicationConfig;
import com.utstar.uapollo.repository.ApplicationConfigRepository;
import com.utstar.uapollo.service.dto.ApplicationConfigDTO;
import com.utstar.uapollo.service.mapper.ApplicationConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ApplicationConfig.
 */
@Service
@Transactional
public class ApplicationConfigServiceImpl implements ApplicationConfigService {

    private final Logger log = LoggerFactory.getLogger(ApplicationConfigServiceImpl.class);

    private final ApplicationConfigRepository applicationConfigRepository;

    private final ApplicationConfigMapper applicationConfigMapper;

    public ApplicationConfigServiceImpl(ApplicationConfigRepository applicationConfigRepository, ApplicationConfigMapper applicationConfigMapper) {
        this.applicationConfigRepository = applicationConfigRepository;
        this.applicationConfigMapper = applicationConfigMapper;
    }

    /**
     * Save a applicationConfig.
     *
     * @param applicationConfigDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ApplicationConfigDTO save(ApplicationConfigDTO applicationConfigDTO) {
        log.debug("Request to save ApplicationConfig : {}", applicationConfigDTO);
        ApplicationConfig applicationConfig = applicationConfigMapper.toEntity(applicationConfigDTO);
        applicationConfig = applicationConfigRepository.save(applicationConfig);
        return applicationConfigMapper.toDto(applicationConfig);
    }

    /**
     * Get all the applicationConfigs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationConfigDTO> findAll() {
        log.debug("Request to get all ApplicationConfigs");
        return applicationConfigRepository.findAll().stream()
            .map(applicationConfigMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one applicationConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ApplicationConfigDTO findOne(Long id) {
        log.debug("Request to get ApplicationConfig : {}", id);
        ApplicationConfig applicationConfig = applicationConfigRepository.findOne(id);
        return applicationConfigMapper.toDto(applicationConfig);
    }

    /**
     * Delete the applicationConfig by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationConfig : {}", id);
        applicationConfigRepository.delete(id);
    }
}

package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.ApplicationMetaService;
import com.utstar.uapollo.domain.ApplicationMeta;
import com.utstar.uapollo.repository.ApplicationMetaRepository;
import com.utstar.uapollo.service.dto.ApplicationMetaDTO;
import com.utstar.uapollo.service.mapper.ApplicationMetaMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ApplicationMeta.
 */
@Service
@Transactional
public class ApplicationMetaServiceImpl implements ApplicationMetaService {

    private final Logger log = LoggerFactory.getLogger(ApplicationMetaServiceImpl.class);

    private final ApplicationMetaRepository applicationMetaRepository;

    private final ApplicationMetaMapper applicationMetaMapper;

    public ApplicationMetaServiceImpl(ApplicationMetaRepository applicationMetaRepository, ApplicationMetaMapper applicationMetaMapper) {
        this.applicationMetaRepository = applicationMetaRepository;
        this.applicationMetaMapper = applicationMetaMapper;
    }

    /**
     * Save a applicationMeta.
     *
     * @param applicationMetaDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ApplicationMetaDTO save(ApplicationMetaDTO applicationMetaDTO) {
        log.debug("Request to save ApplicationMeta : {}", applicationMetaDTO);
        ApplicationMeta applicationMeta = applicationMetaMapper.toEntity(applicationMetaDTO);
        applicationMeta = applicationMetaRepository.save(applicationMeta);
        return applicationMetaMapper.toDto(applicationMeta);
    }

    /**
     * Get all the applicationMetas.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ApplicationMetaDTO> findAll() {
        log.debug("Request to get all ApplicationMetas");
        return applicationMetaRepository.findAll().stream()
            .map(applicationMetaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one applicationMeta by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ApplicationMetaDTO findOne(Long id) {
        log.debug("Request to get ApplicationMeta : {}", id);
        ApplicationMeta applicationMeta = applicationMetaRepository.findOne(id);
        return applicationMetaMapper.toDto(applicationMeta);
    }

    /**
     * Delete the applicationMeta by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ApplicationMeta : {}", id);
        applicationMetaRepository.delete(id);
    }
}

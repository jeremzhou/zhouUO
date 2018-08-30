package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.PrivateConfigService;
import com.utstar.uapollo.domain.PrivateConfig;
import com.utstar.uapollo.repository.PrivateConfigRepository;
import com.utstar.uapollo.service.dto.PrivateConfigDTO;
import com.utstar.uapollo.service.mapper.PrivateConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PrivateConfig.
 */
@Service
@Transactional
public class PrivateConfigServiceImpl implements PrivateConfigService {

    private final Logger log = LoggerFactory.getLogger(PrivateConfigServiceImpl.class);

    private final PrivateConfigRepository privateConfigRepository;

    private final PrivateConfigMapper privateConfigMapper;

    public PrivateConfigServiceImpl(PrivateConfigRepository privateConfigRepository, PrivateConfigMapper privateConfigMapper) {
        this.privateConfigRepository = privateConfigRepository;
        this.privateConfigMapper = privateConfigMapper;
    }

    /**
     * Save a privateConfig.
     *
     * @param privateConfigDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PrivateConfigDTO save(PrivateConfigDTO privateConfigDTO) {
        log.debug("Request to save PrivateConfig : {}", privateConfigDTO);
        PrivateConfig privateConfig = privateConfigMapper.toEntity(privateConfigDTO);
        privateConfig = privateConfigRepository.save(privateConfig);
        return privateConfigMapper.toDto(privateConfig);
    }

    /**
     * Get all the privateConfigs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PrivateConfigDTO> findAll() {
        log.debug("Request to get all PrivateConfigs");
        return privateConfigRepository.findAll().stream()
            .map(privateConfigMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one privateConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PrivateConfigDTO findOne(Long id) {
        log.debug("Request to get PrivateConfig : {}", id);
        PrivateConfig privateConfig = privateConfigRepository.findOne(id);
        return privateConfigMapper.toDto(privateConfig);
    }

    /**
     * Delete the privateConfig by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrivateConfig : {}", id);
        privateConfigRepository.delete(id);
    }
}

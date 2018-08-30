package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.GlobalConfigService;
import com.utstar.uapollo.domain.GlobalConfig;
import com.utstar.uapollo.repository.GlobalConfigRepository;
import com.utstar.uapollo.service.dto.GlobalConfigDTO;
import com.utstar.uapollo.service.mapper.GlobalConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing GlobalConfig.
 */
@Service
@Transactional
public class GlobalConfigServiceImpl implements GlobalConfigService {

    private final Logger log = LoggerFactory.getLogger(GlobalConfigServiceImpl.class);

    private final GlobalConfigRepository globalConfigRepository;

    private final GlobalConfigMapper globalConfigMapper;

    public GlobalConfigServiceImpl(GlobalConfigRepository globalConfigRepository, GlobalConfigMapper globalConfigMapper) {
        this.globalConfigRepository = globalConfigRepository;
        this.globalConfigMapper = globalConfigMapper;
    }

    /**
     * Save a globalConfig.
     *
     * @param globalConfigDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GlobalConfigDTO save(GlobalConfigDTO globalConfigDTO) {
        log.debug("Request to save GlobalConfig : {}", globalConfigDTO);
        GlobalConfig globalConfig = globalConfigMapper.toEntity(globalConfigDTO);
        globalConfig = globalConfigRepository.save(globalConfig);
        return globalConfigMapper.toDto(globalConfig);
    }

    /**
     * Get all the globalConfigs.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GlobalConfigDTO> findAll() {
        log.debug("Request to get all GlobalConfigs");
        return globalConfigRepository.findAll().stream()
            .map(globalConfigMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one globalConfig by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GlobalConfigDTO findOne(Long id) {
        log.debug("Request to get GlobalConfig : {}", id);
        GlobalConfig globalConfig = globalConfigRepository.findOne(id);
        return globalConfigMapper.toDto(globalConfig);
    }

    /**
     * Delete the globalConfig by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GlobalConfig : {}", id);
        globalConfigRepository.delete(id);
    }
}

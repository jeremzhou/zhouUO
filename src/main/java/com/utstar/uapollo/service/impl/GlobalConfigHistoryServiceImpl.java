package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.GlobalConfigHistoryService;
import com.utstar.uapollo.domain.GlobalConfigHistory;
import com.utstar.uapollo.repository.GlobalConfigHistoryRepository;
import com.utstar.uapollo.service.dto.GlobalConfigHistoryDTO;
import com.utstar.uapollo.service.mapper.GlobalConfigHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing GlobalConfigHistory.
 */
@Service
@Transactional
public class GlobalConfigHistoryServiceImpl implements GlobalConfigHistoryService {

    private final Logger log = LoggerFactory.getLogger(GlobalConfigHistoryServiceImpl.class);

    private final GlobalConfigHistoryRepository globalConfigHistoryRepository;

    private final GlobalConfigHistoryMapper globalConfigHistoryMapper;

    public GlobalConfigHistoryServiceImpl(GlobalConfigHistoryRepository globalConfigHistoryRepository, GlobalConfigHistoryMapper globalConfigHistoryMapper) {
        this.globalConfigHistoryRepository = globalConfigHistoryRepository;
        this.globalConfigHistoryMapper = globalConfigHistoryMapper;
    }

    /**
     * Save a globalConfigHistory.
     *
     * @param globalConfigHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GlobalConfigHistoryDTO save(GlobalConfigHistoryDTO globalConfigHistoryDTO) {
        log.debug("Request to save GlobalConfigHistory : {}", globalConfigHistoryDTO);
        GlobalConfigHistory globalConfigHistory = globalConfigHistoryMapper.toEntity(globalConfigHistoryDTO);
        globalConfigHistory = globalConfigHistoryRepository.save(globalConfigHistory);
        return globalConfigHistoryMapper.toDto(globalConfigHistory);
    }

    /**
     * Get all the globalConfigHistories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<GlobalConfigHistoryDTO> findAll() {
        log.debug("Request to get all GlobalConfigHistories");
        return globalConfigHistoryRepository.findAll().stream()
            .map(globalConfigHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one globalConfigHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public GlobalConfigHistoryDTO findOne(Long id) {
        log.debug("Request to get GlobalConfigHistory : {}", id);
        GlobalConfigHistory globalConfigHistory = globalConfigHistoryRepository.findOne(id);
        return globalConfigHistoryMapper.toDto(globalConfigHistory);
    }

    /**
     * Delete the globalConfigHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete GlobalConfigHistory : {}", id);
        globalConfigHistoryRepository.delete(id);
    }
}

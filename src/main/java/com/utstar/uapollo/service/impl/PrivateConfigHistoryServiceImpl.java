package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.PrivateConfigHistoryService;
import com.utstar.uapollo.domain.PrivateConfigHistory;
import com.utstar.uapollo.repository.PrivateConfigHistoryRepository;
import com.utstar.uapollo.service.dto.PrivateConfigHistoryDTO;
import com.utstar.uapollo.service.mapper.PrivateConfigHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing PrivateConfigHistory.
 */
@Service
@Transactional
public class PrivateConfigHistoryServiceImpl implements PrivateConfigHistoryService {

    private final Logger log = LoggerFactory.getLogger(PrivateConfigHistoryServiceImpl.class);

    private final PrivateConfigHistoryRepository privateConfigHistoryRepository;

    private final PrivateConfigHistoryMapper privateConfigHistoryMapper;

    public PrivateConfigHistoryServiceImpl(PrivateConfigHistoryRepository privateConfigHistoryRepository, PrivateConfigHistoryMapper privateConfigHistoryMapper) {
        this.privateConfigHistoryRepository = privateConfigHistoryRepository;
        this.privateConfigHistoryMapper = privateConfigHistoryMapper;
    }

    /**
     * Save a privateConfigHistory.
     *
     * @param privateConfigHistoryDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PrivateConfigHistoryDTO save(PrivateConfigHistoryDTO privateConfigHistoryDTO) {
        log.debug("Request to save PrivateConfigHistory : {}", privateConfigHistoryDTO);
        PrivateConfigHistory privateConfigHistory = privateConfigHistoryMapper.toEntity(privateConfigHistoryDTO);
        privateConfigHistory = privateConfigHistoryRepository.save(privateConfigHistory);
        return privateConfigHistoryMapper.toDto(privateConfigHistory);
    }

    /**
     * Get all the privateConfigHistories.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<PrivateConfigHistoryDTO> findAll() {
        log.debug("Request to get all PrivateConfigHistories");
        return privateConfigHistoryRepository.findAll().stream()
            .map(privateConfigHistoryMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one privateConfigHistory by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PrivateConfigHistoryDTO findOne(Long id) {
        log.debug("Request to get PrivateConfigHistory : {}", id);
        PrivateConfigHistory privateConfigHistory = privateConfigHistoryRepository.findOne(id);
        return privateConfigHistoryMapper.toDto(privateConfigHistory);
    }

    /**
     * Delete the privateConfigHistory by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PrivateConfigHistory : {}", id);
        privateConfigHistoryRepository.delete(id);
    }
}

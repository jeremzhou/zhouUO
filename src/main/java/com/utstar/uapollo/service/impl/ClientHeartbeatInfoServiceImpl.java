package com.utstar.uapollo.service.impl;

import com.utstar.uapollo.service.ClientHeartbeatInfoService;
import com.utstar.uapollo.domain.ClientHeartbeatInfo;
import com.utstar.uapollo.repository.ClientHeartbeatInfoRepository;
import com.utstar.uapollo.service.dto.ClientHeartbeatInfoDTO;
import com.utstar.uapollo.service.mapper.ClientHeartbeatInfoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ClientHeartbeatInfo.
 */
@Service
@Transactional
public class ClientHeartbeatInfoServiceImpl implements ClientHeartbeatInfoService {

    private final Logger log = LoggerFactory.getLogger(ClientHeartbeatInfoServiceImpl.class);

    private final ClientHeartbeatInfoRepository clientHeartbeatInfoRepository;

    private final ClientHeartbeatInfoMapper clientHeartbeatInfoMapper;

    public ClientHeartbeatInfoServiceImpl(ClientHeartbeatInfoRepository clientHeartbeatInfoRepository, ClientHeartbeatInfoMapper clientHeartbeatInfoMapper) {
        this.clientHeartbeatInfoRepository = clientHeartbeatInfoRepository;
        this.clientHeartbeatInfoMapper = clientHeartbeatInfoMapper;
    }

    /**
     * Save a clientHeartbeatInfo.
     *
     * @param clientHeartbeatInfoDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ClientHeartbeatInfoDTO save(ClientHeartbeatInfoDTO clientHeartbeatInfoDTO) {
        log.debug("Request to save ClientHeartbeatInfo : {}", clientHeartbeatInfoDTO);
        ClientHeartbeatInfo clientHeartbeatInfo = clientHeartbeatInfoMapper.toEntity(clientHeartbeatInfoDTO);
        clientHeartbeatInfo = clientHeartbeatInfoRepository.save(clientHeartbeatInfo);
        return clientHeartbeatInfoMapper.toDto(clientHeartbeatInfo);
    }

    /**
     * Get all the clientHeartbeatInfos.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClientHeartbeatInfoDTO> findAll() {
        log.debug("Request to get all ClientHeartbeatInfos");
        return clientHeartbeatInfoRepository.findAll().stream()
            .map(clientHeartbeatInfoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one clientHeartbeatInfo by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ClientHeartbeatInfoDTO findOne(Long id) {
        log.debug("Request to get ClientHeartbeatInfo : {}", id);
        ClientHeartbeatInfo clientHeartbeatInfo = clientHeartbeatInfoRepository.findOne(id);
        return clientHeartbeatInfoMapper.toDto(clientHeartbeatInfo);
    }

    /**
     * Delete the clientHeartbeatInfo by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ClientHeartbeatInfo : {}", id);
        clientHeartbeatInfoRepository.delete(id);
    }
}

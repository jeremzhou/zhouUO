/**
 * created on 2018年4月23日 下午3:16:11
 */
package com.utstar.uapollo.custom.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utstar.uapollo.custom.repositry.PrivateConfigCustomRepository;
import com.utstar.uapollo.custom.service.PrivateConfigCustomService;
import com.utstar.uapollo.domain.PrivateConfig;
import com.utstar.uapollo.service.dto.PrivateConfigDTO;
import com.utstar.uapollo.service.impl.PrivateConfigServiceImpl;
import com.utstar.uapollo.service.mapper.PrivateConfigMapper;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
@Service
@Transactional
public class PrivateConfigCustomServiceImpl implements PrivateConfigCustomService {

    private final Logger log = LoggerFactory.getLogger(PrivateConfigServiceImpl.class);

    private final PrivateConfigCustomRepository privateConfigCustomRepository;

    private final PrivateConfigMapper privateConfigMapper;

    public PrivateConfigCustomServiceImpl(
            PrivateConfigCustomRepository privateConfigCustomRepository,
            PrivateConfigMapper privateConfigMapper) {
        this.privateConfigCustomRepository = privateConfigCustomRepository;
        this.privateConfigMapper = privateConfigMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrivateConfigDTO> findByApplicationId(Long applicationId) {

        log.debug("Request to get all PrivateConfigs by applicationId: {}", applicationId);
        return privateConfigCustomRepository.findByApplicationId(applicationId).stream()
                .map(privateConfigMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PrivateConfigDTO> findByApplicationIdAndKey(Long applicationId, String key) {

        log.debug("Request to get the PrivateConfig by applicationId: {} and key: {} ",
                applicationId, key);
        Optional<PrivateConfig> existingPrivateConfig = privateConfigCustomRepository
                .findByApplicationIdAndKey(applicationId, key);
        return existingPrivateConfig.map((value) -> privateConfigMapper.toDto(value));
    }

}

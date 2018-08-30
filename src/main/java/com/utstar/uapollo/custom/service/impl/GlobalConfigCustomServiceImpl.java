/**
 * created on 2018年4月23日 下午2:24:51
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

import com.utstar.uapollo.custom.repositry.GlobalConfigCustomRepository;
import com.utstar.uapollo.custom.service.GlobalConfigCustomService;
import com.utstar.uapollo.domain.GlobalConfig;
import com.utstar.uapollo.service.dto.GlobalConfigDTO;
import com.utstar.uapollo.service.mapper.GlobalConfigMapper;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
@Service
@Transactional
public class GlobalConfigCustomServiceImpl implements GlobalConfigCustomService {

    private final Logger log = LoggerFactory.getLogger(GlobalConfigCustomServiceImpl.class);

    private final GlobalConfigCustomRepository globalConfigCustomRepository;

    private final GlobalConfigMapper globalConfigMapper;

    public GlobalConfigCustomServiceImpl(GlobalConfigCustomRepository globalConfigCustomRepository,
            GlobalConfigMapper globalConfigMapper) {
        this.globalConfigCustomRepository = globalConfigCustomRepository;
        this.globalConfigMapper = globalConfigMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GlobalConfigDTO> findByApplicationMetaId(Long applicationMetaId) {

        log.debug("Request to get all GlobalConfigs by applicationMetaId: {}",
                applicationMetaId);
        return globalConfigCustomRepository.findByApplicationMetaId(applicationMetaId).stream()
                .map(globalConfigMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GlobalConfigDTO> findByApplicationMetaIdAndKey(Long applicationMetaId,
            String key) {

        log.debug("Request to get the GlobalConfig by applicationMetaId: {} and key: {} ",
                applicationMetaId, key);
        Optional<GlobalConfig> existingGlobalConfig = globalConfigCustomRepository
                .findByApplicationMetaIdAndKey(applicationMetaId, key);
        return existingGlobalConfig.map((value) -> globalConfigMapper.toDto(value));

    }

}

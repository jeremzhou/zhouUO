/**
 * created on 2018年5月24日 上午11:06:02
 */
package com.utstar.uapollo.custom.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utstar.uapollo.custom.repositry.ApplicationConfigCustomRepository;
import com.utstar.uapollo.custom.service.ApplicationConfigCustomService;
import com.utstar.uapollo.domain.ApplicationConfig;
import com.utstar.uapollo.service.dto.ApplicationConfigDTO;
import com.utstar.uapollo.service.mapper.ApplicationConfigMapper;

/**
 * @author UTSC0167
 * @date 2018年5月24日
 *
 */
@Service
@Transactional
public class ApplicationConfigCustomServieImpl implements ApplicationConfigCustomService {

    private final Logger log = LoggerFactory.getLogger(ApplicationConfigCustomServieImpl.class);

    private final ApplicationConfigCustomRepository applicationConfigCustomRepository;

    private final ApplicationConfigMapper applicationConfigMapper;

    public ApplicationConfigCustomServieImpl(
            ApplicationConfigCustomRepository applicationConfigCustomRepository,
            ApplicationConfigMapper applicationConfigMapper) {
        super();
        this.applicationConfigCustomRepository = applicationConfigCustomRepository;
        this.applicationConfigMapper = applicationConfigMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ApplicationConfigDTO> findByApplicationIdAndCreateTimeMax(Long applicationId) {

        log.debug("Request to get the ApplicationConfig by applicationId: {} and max create time", applicationId);
        Optional<ApplicationConfig> existingApplicationConfig = applicationConfigCustomRepository
                .findByApplicationIdAndCreateTimeMax(applicationId);
        return existingApplicationConfig.map((value) -> applicationConfigMapper.toDto(value));
    }
}
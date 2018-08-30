/**
 * created on 2018年8月17日 下午1:20:16
 */
package com.utstar.uapollo.custom.service.impl;

import java.util.LinkedHashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.utstar.uapollo.custom.config.UapolloCache;
import com.utstar.uapollo.custom.dto.UapolloClientId;
import com.utstar.uapollo.custom.service.ApplicationConfigCustomService;
import com.utstar.uapollo.custom.service.ApplicationConfigRelaseService;
import com.utstar.uapollo.custom.service.ApplicationCustomService;
import com.utstar.uapollo.custom.service.ApplicationMetaCustomService;
import com.utstar.uapollo.custom.service.ServerCustomService;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.custom.web.vo.UapolloConfig;
import com.utstar.uapollo.service.dto.ApplicationConfigDTO;
import com.utstar.uapollo.service.dto.ApplicationDTO;
import com.utstar.uapollo.service.dto.ApplicationMetaDTO;
import com.utstar.uapollo.service.dto.ServerDTO;

/**
 * @author UTSC0167
 * @date 2018年8月17日
 *
 */
@Service
public class ApplicationConfigRelaseServiceImpl implements ApplicationConfigRelaseService {

    private final Logger log = LoggerFactory.getLogger(ApplicationConfigRelaseServiceImpl.class);

    private static final Splitter X_FORWARDED_FOR_SPLITTER = Splitter.on(",").omitEmptyStrings()
            .trimResults();

    private final ServerCustomService serverCustomService;

    private final ApplicationMetaCustomService applicationMetaCustomService;

    private final ApplicationCustomService applicationCustomService;

    private final ApplicationConfigCustomService applicationConfigCustomService;

    public ApplicationConfigRelaseServiceImpl(ServerCustomService serverCustomService,
            ApplicationMetaCustomService applicationMetaCustomService,
            ApplicationCustomService applicationCustomService,
            ApplicationConfigCustomService applicationConfigCustomService) {
        super();
        this.serverCustomService = serverCustomService;
        this.applicationMetaCustomService = applicationMetaCustomService;
        this.applicationCustomService = applicationCustomService;
        this.applicationConfigCustomService = applicationConfigCustomService;
    }

    @Override
    public String tryGetClientIp(HttpServletRequest request) {

        String forwardedFor = request.getHeader("X-FORWARDED-FOR");
        if (!Strings.isNullOrEmpty(forwardedFor))
            return X_FORWARDED_FOR_SPLITTER.splitToList(forwardedFor).get(0);
        return request.getRemoteAddr();
    }

    @Override
    public Optional<UapolloConfig> updateReleasedApplicationConfigCache(String ip,
            String applicationName) {

        Optional<ApplicationConfigDTO> existingApplicationConfigDTO = getApplicationConfig(ip,
                applicationName);
        if (!existingApplicationConfigDTO.isPresent()) {
            log.info(
                    "getApplicationConfig for ip: {} applicationName: {} get applicationConfig is null, return null.",
                    ip, applicationName);
            return Optional.empty();
        }

        return updateReleasedApplicationConfigCache(ip, applicationName,
                existingApplicationConfigDTO.get());
    }

    @Override
    public Optional<UapolloConfig> updateReleasedApplicationConfigCache(String ip,
            String applicationName, ApplicationConfigDTO applicationConfigDTO) {

        final UapolloClientId clientId = new UapolloClientId(ip, applicationName.toLowerCase());
        final String configContent = applicationConfigDTO.getConfigContent();
        final String releaseKey = UapolloUtil.getSha256(configContent);
        Optional<LinkedHashMap<String, String>> existingConfigContentMap = convertConfigContent(
                clientId, configContent);
        if (!existingConfigContentMap.isPresent()) {
            log.info("updateReleasedApplicationConfigCache for clientId: {} convert configContent: {} is null.",clientId,configContent);
            return Optional.empty();
        }
        final LinkedHashMap<String, String> configContentMap = existingConfigContentMap.get();

        Optional<UapolloConfig> existingUapolloConfig = equalsWithCahedApplicationConfig(clientId,
                releaseKey, applicationConfigDTO,configContentMap);
        if (existingUapolloConfig.isPresent()) {
            log.info(
                    "updateReleasedApplicationConfigCache for clientId: {} is up-to-date, do nothing and return.",
                    clientId);
            return existingUapolloConfig;
        }

        UapolloConfig uapolloConfig = new UapolloConfig(clientId.getApplicationName(), releaseKey);
        uapolloConfig.setConfigurations(existingConfigContentMap.get());
        uapolloConfig.setNotificationId(applicationConfigDTO.getId());

        UapolloCache.RELEASED_APPLICATIONS_CACHE.put(clientId, uapolloConfig);
        log.info("updateReleasedApplicationConfigCache for clientId: {} put uapolloConfig: {}",
                clientId, uapolloConfig);
        return Optional.of(uapolloConfig);

    }

    private final Optional<ApplicationConfigDTO> getApplicationConfig(String ip,
            String applicationName) {

        Optional<ServerDTO> existingServerDto = serverCustomService.findByIp(ip);
        if (!existingServerDto.isPresent()) {
            log.info("getApplicationConfig for ip: {} to get server is null, return null.", ip);
            return Optional.empty();
        }
        final Long serverId = existingServerDto.get().getId();

        Optional<ApplicationMetaDTO> existingApplicationMetaDto = applicationMetaCustomService
                .findByName(applicationName);
        if (!existingApplicationMetaDto.isPresent()) {
            log.info(
                    "getApplicationConfig for applicationName: {} to get applicationMeta is null, return null.",
                    applicationName);
            return Optional.empty();
        }
        final Long applicationMetaId = existingApplicationMetaDto.get().getId();

        Optional<ApplicationDTO> existingApplicationDto = applicationCustomService
                .findByApplicationMetaIdAndServerId(applicationMetaId, serverId);
        if (!existingApplicationDto.isPresent()) {
            log.info(
                    "getApplicationConfig for applicationMetaId: {} serverId: {} to get applicationDto is null, return null.",
                    applicationMetaId, serverId);
            return Optional.empty();
        }
        final long applicationId = existingApplicationDto.get().getId();

        log.info("getApplicationConfig for ip: {} applicationName: {} get the applicationId: {}",
                ip, applicationName, applicationId);
        return applicationConfigCustomService.findByApplicationIdAndCreateTimeMax(applicationId);
    }

    private final Optional<UapolloConfig> equalsWithCahedApplicationConfig(UapolloClientId clientId,
            String releaseKey, ApplicationConfigDTO applicationConfigDTO,LinkedHashMap<String, String> configContentMap) {

        UapolloConfig uapolloConfig = UapolloCache.RELEASED_APPLICATIONS_CACHE.get(clientId);
        if (uapolloConfig == null) {
            log.info(
                    "isEqualWithCahedApplicationConfig for clientId: {} don't exist in RELEASED_APPLICATIONS_CACHE, return false.",
                    clientId);
            return Optional.empty();
        }

        log.info(
                "isEqualWithCahedApplicationConfig for clientId: {} the releasing releaseKey: {} notificationId: {} configContent: {}",
                clientId, releaseKey, applicationConfigDTO.getId(),
                applicationConfigDTO.getConfigContent());
        log.info(
                "isEqualWithCahedApplicationConfig for clientId: {} the cached releaseKey: {} notificationId: {} configContent: {}",
                clientId, uapolloConfig.getReleaseKey(), uapolloConfig.getNotificationId(),
                uapolloConfig.getConfigurations());
        if (releaseKey.equals(uapolloConfig.getReleaseKey())
                && applicationConfigDTO.getId().equals(uapolloConfig.getNotificationId())
                && configContentMap
                        .equals(uapolloConfig.getConfigurations())) {
            log.info(
                    "isEqualWithCahedApplicationConfig for clientId: {} is up-to-date and return true.",
                    clientId);
            return Optional.ofNullable(uapolloConfig);
        }

        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    private final Optional<LinkedHashMap<String, String>> convertConfigContent(
            UapolloClientId clientId, String configContent) {

        Optional<Object> existingMap = UapolloUtil.jsonToObject(configContent,
                new TypeReference<LinkedHashMap<String, String>>() {
                });
        if (!existingMap.isPresent()) {
            return Optional.empty();
        }
        return Optional.of((LinkedHashMap<String, String>) existingMap.get());
    }
}

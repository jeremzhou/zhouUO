/**
 * created on 2018年5月22日 上午11:13:16
 */
package com.utstar.uapollo.custom.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.utstar.uapollo.custom.config.UapolloCache;
import com.utstar.uapollo.custom.config.UapolloCustomConfiguration;
import com.utstar.uapollo.custom.dto.ReleasingApplication;
import com.utstar.uapollo.custom.dto.ReleasingResult;
import com.utstar.uapollo.custom.service.ApplicationCustomService;
import com.utstar.uapollo.custom.service.GlobalConfigCustomService;
import com.utstar.uapollo.custom.service.NodeConfigCustomService;
import com.utstar.uapollo.custom.service.NodeCustomService;
import com.utstar.uapollo.custom.service.ReleaseConfigService;
import com.utstar.uapollo.custom.service.ServerCustomService;
import com.utstar.uapollo.custom.service.enumeration.ReleasingStep;
import com.utstar.uapollo.custom.service.enumeration.ReleasingStepResult;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.service.ApplicationMetaService;
import com.utstar.uapollo.service.ApplicationService;
import com.utstar.uapollo.service.dto.ApplicationDTO;
import com.utstar.uapollo.service.dto.ApplicationMetaDTO;
import com.utstar.uapollo.service.dto.GlobalConfigDTO;
import com.utstar.uapollo.service.dto.NodeConfigDTO;
import com.utstar.uapollo.service.dto.NodeDTO;
import com.utstar.uapollo.service.dto.ServerDTO;

/**
 * @author UTSC0167
 * @date 2018年5月22日
 *
 */
@Component
public class ReleaseConfigServiceImpl implements ReleaseConfigService {

    private final Logger log = LoggerFactory.getLogger(ReleaseConfigServiceImpl.class);

    private final UapolloCustomConfiguration uapolloCustomConfiguration;

    private final ApplicationMetaService applicationMetaService;

    private final GlobalConfigCustomService globalConfigCustomServiceImpl;

    private final NodeCustomService nodeCustomService;

    private final NodeConfigCustomService nodeConfigCustomService;

    private final ServerCustomService serverCustomService;

    private final ApplicationService applicationService;

    private final ApplicationCustomService applicationCustomService;

    public ReleaseConfigServiceImpl(UapolloCustomConfiguration uapolloCustomConfiguration,
            ApplicationMetaService applicationMetaService,
            GlobalConfigCustomService globalConfigCustomServiceImpl,
            NodeCustomService nodeCustomService, NodeConfigCustomService nodeConfigCustomService,
            ServerCustomService serverCustomService, ApplicationService applicationService,
            ApplicationCustomService applicationCustomService) {
        super();
        this.uapolloCustomConfiguration = uapolloCustomConfiguration;
        this.applicationMetaService = applicationMetaService;
        this.globalConfigCustomServiceImpl = globalConfigCustomServiceImpl;
        this.nodeCustomService = nodeCustomService;
        this.nodeConfigCustomService = nodeConfigCustomService;
        this.serverCustomService = serverCustomService;
        this.applicationService = applicationService;
        this.applicationCustomService = applicationCustomService;
    }

    @Override
    public List<ReleasingResult> releaseGlobalConfig(Long applicationMetaId) {

        log.info("releaseGlobalConfig begin for applicationMetaId: {}", applicationMetaId);
        List<ReleasingResult> releasingResults = handleCommonStep(applicationMetaId);
        if (!releasingResults.get(0).getReleasingStepResult().equals(ReleasingStepResult.SUCCESS))
            return releasingResults;

        releasingResults.addAll(getNodeConfigByApplicationMetaId(applicationMetaId));

        releasingResults.addAll(releasingApplicationByApplicationMetaId(applicationMetaId));

        log.info("releaseGlobalConfig end for applicationMetaId: {}", applicationMetaId);
        return releasingResults;
    }

    @Override
    public List<ReleasingResult> releaseNodeConfig(Long applicationMetaId, Long nodeId) {

        log.info("releaseNodeConfig begin for applicationMetaId: {} nodeId: {}", applicationMetaId,
                nodeId);
        List<ReleasingResult> releasingResults = handleCommonStep(applicationMetaId);
        if (!releasingResults.get(0).getReleasingStepResult().equals(ReleasingStepResult.SUCCESS))
            return releasingResults;

        releasingResults
                .addAll(getNodeConfigByApplicationMetaIdAndNodeId(applicationMetaId, nodeId));

        releasingResults.addAll(
                releasingApplicationByApplicationMetaIdAndNodeId(applicationMetaId, nodeId));

        log.info("releaseNodeConfig end for applicationMetaId: {} nodeId: {}", applicationMetaId,
                nodeId);
        return releasingResults;
    }

    @Override
    public List<ReleasingResult> releaseApplicationConfig(Long applicationId) {

        log.info("releaseApplicationConfig begin for applicationId: {}", applicationId);
        ApplicationDTO applicationDTO = applicationService.findOne(applicationId);
        if (applicationDTO == null) {
            LinkedList<ReleasingResult> nullReleasingResults = new LinkedList<>();
            nullReleasingResults
                    .add(new ReleasingResult(ReleasingStep.RELEASING_APPLICATION, applicationId)
                            .setReleasingStepResult(ReleasingStepResult.NULL)
                            .setDetail("-1_-1_" + applicationId));
            log.info(
                    "releaseApplicationConfig for applicationId: {} get applicationDTO is null, return null.",
                    applicationId);
            return nullReleasingResults;
        }

        long applicationMetaId = applicationDTO.getApplicationMetaId();
        List<ReleasingResult> releasingResults = handleCommonStep(applicationMetaId);
        if (!releasingResults.get(0).getReleasingStepResult().equals(ReleasingStepResult.SUCCESS))
            return releasingResults;

        releasingResults.addAll(
                getNodeConfigByApplicationMetaIdAndApplicationId(applicationMetaId, applicationId));

        releasingResults.addAll(releasingApplicationByApplicationMetaIdAndApplicationId(
                applicationMetaId, applicationId));

        log.info("releaseApplicationConfig end for applicationId: {}", applicationId);
        return releasingResults;
    }

    private final List<ReleasingResult> handleCommonStep(final Long applicationMetaId) {

        List<ReleasingResult> releasingResults = new LinkedList<>();
        ReleasingResult releasingResult = getApplicationMetaConfig(applicationMetaId);
        releasingResults.add(releasingResult);
        if (!releasingResult.getReleasingStepResult().equals(ReleasingStepResult.SUCCESS)) {
            log.info(
                    "handleCommonStep for applicationMetaId: {} getApplicationMetaConfig isn't success, return null.");
            return releasingResults;
        }

        releasingResults.add(getGolbalConfig(applicationMetaId));

        return releasingResults;
    }

    @SuppressWarnings("unchecked")
    private final ReleasingResult getApplicationMetaConfig(Long applicationMetaId) {

        log.info("getApplicationMetaConfig begin for applicationMetaId: {}", applicationMetaId);
        ReleasingResult releasingResult = new ReleasingResult(ReleasingStep.APPLICATION_META,
                applicationMetaId).setDetail(applicationMetaId);
        ApplicationMetaDTO aMetaDTO = applicationMetaService.findOne(applicationMetaId);
        if (aMetaDTO == null) {
            log.info(
                    "getApplicationMetaConfig for applicationMetaId: {} get applicationMeta is null, return null.",
                    applicationMetaId);
            return releasingResult.setReleasingStepResult(ReleasingStepResult.NULL);
        }

        Optional<Object> existingApplicationMetaConfig = UapolloUtil
                .jsonToObject(aMetaDTO.getConfigContent(), new TypeReference<TreeMap<String, String>>(){});
        if (existingApplicationMetaConfig.isPresent())
            UapolloCache.APPLICATION_META_CONFIG_MAP.put(applicationMetaId,
                    (TreeMap<String, String>) existingApplicationMetaConfig.get());
        else
            return releasingResult.setReleasingStepResult(ReleasingStepResult.EXCEPTION);

        log.info("getApplicationMetaConfig end for applicationMetaId: {}", applicationMetaId);
        return releasingResult.setReleasingStepResult(ReleasingStepResult.SUCCESS);
    }

    private final ReleasingResult getGolbalConfig(Long applicationMetaId) {

        log.info("getGolbalConfig begin for applicationMetaId: {}", applicationMetaId);
        ReleasingResult releasingResult = new ReleasingResult(ReleasingStep.GLOBAL_CONFIG,
                applicationMetaId).setDetail(applicationMetaId);
        List<GlobalConfigDTO> globalConfigDTOs = globalConfigCustomServiceImpl
                .findByApplicationMetaId(applicationMetaId);
        if (globalConfigDTOs.size() == 0) {
            log.info(
                    "getGolbalConfig for applicationMetaId: {} get globalConfigs is null, return null.",
                    applicationMetaId);
            return releasingResult.setReleasingStepResult(ReleasingStepResult.NULL);
        }

        final TreeMap<String, String> globalConfigMap = globalConfigDTOs.stream()
                .collect(Collectors.toMap(GlobalConfigDTO::getKey, GlobalConfigDTO::getValue,
                        (x, y) -> x, TreeMap::new));
        UapolloCache.GLOBAL_CONFIG_MAP.put(applicationMetaId, globalConfigMap);

        log.info("getGolbalConfig end for applicationMetaId: {}", applicationMetaId);
        return releasingResult.setReleasingStepResult(ReleasingStepResult.SUCCESS);
    }

    private final List<ReleasingResult> getNodeConfigByApplicationMetaId(Long applicationMetaId) {

        log.info("getNodeConfigByApplicationMetaId begin for applicationMetaId: {}",
                applicationMetaId);
        List<ReleasingResult> releasingResults = new LinkedList<>();
        List<NodeConfigDTO> nodeConfigDTOs = nodeConfigCustomService
                .findByApplicationMetaId(applicationMetaId);
        if (nodeConfigDTOs.size() == 0) {
            log.info(
                    "getNodeConfigByApplicationMetaId for applicationMetaId: {} get nodeConfigs is null, return null.",
                    applicationMetaId);
            releasingResults.add(new ReleasingResult(ReleasingStep.NODE_CONFIG, -1L)
                    .setReleasingStepResult(ReleasingStepResult.NULL).setDetail(applicationMetaId));
            return releasingResults;
        }

        addNodeConfigToCache(applicationMetaId, nodeConfigDTOs, releasingResults);
        log.info("getNodeConfigByApplicationMetaId end for applicationMetaId: {}",
                applicationMetaId);
        return releasingResults;
    }

    private final List<ReleasingResult> getNodeConfigByApplicationMetaIdAndNodeId(
            Long applicationMetaId, Long nodeId) {

        log.info(
                "getNodeConfigByApplicationMetaIdAndNodeId begin for applicationMetaId: {} nodeId: {}",
                applicationMetaId, nodeId);
        List<ReleasingResult> releasingResults = new LinkedList<>();
        List<NodeConfigDTO> nodeConfigDTOs = nodeConfigCustomService
                .findByApplicationMetaIdAndNodeId(applicationMetaId, nodeId);
        if (nodeConfigDTOs.size() == 0) {
            log.info(
                    "getNodeConfigByApplicationMetaIdAndNodeId for applicationMetaId: {} nodeId: {} get nodeConfigs is null, return null.",
                    applicationMetaId, nodeId);
            releasingResults.add(new ReleasingResult(ReleasingStep.NODE_CONFIG, nodeId)
                    .setReleasingStepResult(ReleasingStepResult.NULL)
                    .setDetail(applicationMetaId + "_" + nodeId));
            return releasingResults;
        }

        addNodeConfigToCache(applicationMetaId, nodeConfigDTOs, releasingResults);
        log.info(
                "getNodeConfigByApplicationMetaIdAndNodeId end for applicationMetaId: {} nodeId: {}",
                applicationMetaId, nodeId);
        return releasingResults;
    }

    private final List<ReleasingResult> getNodeConfigByApplicationMetaIdAndApplicationId(
            Long applicationMetaId, Long applicationId) {

        log.info(
                "getNodeConfigByApplicationMetaIdAndApplicationId begin for applicationMetaId: {} applicationId: {}",
                applicationMetaId, applicationId);
        List<ReleasingResult> releasingResults = new LinkedList<>();
        List<NodeConfigDTO> nodeConfigDTOs = nodeConfigCustomService
                .findByApplicationMetaIdAndApplicationId(applicationMetaId, applicationId);
        if (nodeConfigDTOs.size() == 0) {
            log.info(
                    "getNodeConfigByApplicationMetaIdAndApplicationId for applicationMetaId: {} applicationId: {} get nodeConfigs is null, return null.",
                    applicationMetaId, applicationId);
            releasingResults.add(new ReleasingResult(ReleasingStep.NODE_CONFIG, -1L)
                    .setReleasingStepResult(ReleasingStepResult.NULL)
                    .setDetail(applicationMetaId + "_-1_" + applicationId));
            return releasingResults;
        }

        addNodeConfigToCache(applicationMetaId, nodeConfigDTOs, releasingResults);
        log.info(
                "getNodeConfigByApplicationMetaIdAndApplicationId end for applicationMetaId: {} applicationId: {}",
                applicationMetaId, applicationId);
        return releasingResults;
    }

    private final void addNodeConfigToCache(final Long applicationMetaId,
            final List<NodeConfigDTO> nodeConfigDTOs,
            final List<ReleasingResult> releasingResults) {

        final Map<Long, TreeMap<String, String>> nodeConfigMap = new ConcurrentHashMap<>();
        nodeConfigDTOs.forEach(ncd -> {

            TreeMap<String, String> itemMap = nodeConfigMap.get(ncd.getNodeId());
            if (itemMap == null) {
                itemMap = new TreeMap<>();
                nodeConfigMap.put(ncd.getNodeId(), itemMap);
            }
            itemMap.put(ncd.getKey(), ncd.getValue());
        });

        final Map<Long, TreeMap<String, String>> cacheNodeConfigMap = UapolloCache.NODE_CONFIG_MAP
                .get(applicationMetaId);
        if (cacheNodeConfigMap == null)
            UapolloCache.NODE_CONFIG_MAP.put(applicationMetaId, nodeConfigMap);
        else
            nodeConfigMap.forEach((k, v) -> {
                cacheNodeConfigMap.put(k, v);
                releasingResults.add(new ReleasingResult(ReleasingStep.NODE_CONFIG, k)
                        .setReleasingStepResult(ReleasingStepResult.SUCCESS)
                        .setDetail(applicationMetaId));
            });
    }

    private final List<ReleasingResult> releasingApplicationByApplicationMetaId(
            final Long applicationMetaId) {

        log.info("getReleasingApplicationListByApplicationMetaId begin for applicationMetaId: {}",
                applicationMetaId);
        List<ReleasingResult> releasingResults = new LinkedList<>();
        List<ApplicationDTO> applicationDTOs = applicationCustomService
                .findByApplicationMetaId(applicationMetaId);
        if (applicationDTOs.size() == 0) {
            log.info(
                    "getReleasingApplicationListByApplicationMetaId for applicationMetaId: {} get releasingApplicationS is null, do nothing.",
                    applicationMetaId);
            releasingResults.add(new ReleasingResult(ReleasingStep.RELEASING_APPLICATION, -1L)
                    .setReleasingStepResult(ReleasingStepResult.NULL).setDetail(applicationMetaId));
            return releasingResults;
        }

        addApplicationToQueue(applicationMetaId, applicationDTOs, releasingResults);

        log.info("getReleasingApplicationListByApplicationMetaId end for applicationMetaId: {}",
                applicationMetaId);
        return releasingResults;
    }

    private final List<ReleasingResult> releasingApplicationByApplicationMetaIdAndNodeId(
            final Long applicationMetaId, final Long nodeId) {

        log.info(
                "releasingApplicationByApplicationMetaIdAndNodeId begin for applicationMetaId: {} nodeId: {}",
                applicationMetaId, nodeId);
        List<ReleasingResult> releasingResults = new LinkedList<>();
        List<ApplicationDTO> applicationDTOs = applicationCustomService
                .findByApplicationMetaIdAndNodeId(applicationMetaId, nodeId);
        if (applicationDTOs.size() == 0) {
            log.info(
                    "releasingApplicationByApplicationMetaIdAndNodeId for applicationMetaId: {} nodeId: {} get releasingApplicationS is null, do nothing.",
                    applicationMetaId, nodeId);
            releasingResults.add(new ReleasingResult(ReleasingStep.RELEASING_APPLICATION, -1L)
                    .setReleasingStepResult(ReleasingStepResult.NULL)
                    .setDetail(applicationMetaId + "_" + nodeId));
            return releasingResults;
        }

        addApplicationToQueue(applicationMetaId, applicationDTOs, releasingResults);

        log.info(
                "releasingApplicationByApplicationMetaIdAndNodeId end for applicationMetaId: {} nodeId: {}",
                applicationMetaId, nodeId);
        return releasingResults;
    }

    private final List<ReleasingResult> releasingApplicationByApplicationMetaIdAndApplicationId(
            final Long applicationMetaId, final Long applicationId) {

        log.info(
                "releasingApplicationByApplicationMetaIdAndApplicationId begin for applicationMetaId: {} applicationId: {}",
                applicationMetaId, applicationId);
        List<ReleasingResult> releasingResults = new LinkedList<>();
        ApplicationDTO applicationDTO = applicationService.findOne(applicationId);
        if (applicationDTO == null) {
            log.info(
                    "releasingApplicationByApplicationMetaIdAndApplicationId for applicationMetaId: {} applicationId: {} get releasingApplicationS is null, do nothing.",
                    applicationMetaId, applicationId);
            releasingResults.add(new ReleasingResult(ReleasingStep.RELEASING_APPLICATION, -1L)
                    .setReleasingStepResult(ReleasingStepResult.NULL)
                    .setDetail(applicationMetaId + "_-1_" + applicationId));
            return releasingResults;
        }

        List<ApplicationDTO> applicationDTOs = new LinkedList<>();
        applicationDTOs.add(applicationDTO);
        addApplicationToQueue(applicationMetaId, applicationDTOs, releasingResults);

        log.info(
                "releasingApplicationByApplicationMetaIdAndApplicationId end for applicationMetaId: {} applicationId: {}",
                applicationMetaId, applicationId);
        return releasingResults;
    }

    private final List<ReleasingResult> addApplicationToQueue(final Long applicationMetaId,
            List<ApplicationDTO> applicationDTOs, List<ReleasingResult> releasingResults) {

        applicationDTOs.forEach(app -> {

            final Long appId = app.getId();
            ApplicationMetaDTO applicationMetaDTO = applicationMetaService
                    .findOne(applicationMetaId);
            Optional<NodeDTO> existingNodeDTO = nodeCustomService.findByApplicationId(appId);
            Optional<ServerDTO> existingServerDTO = serverCustomService.findByApplicationId(appId);
            if (existingNodeDTO.isPresent() && existingServerDTO.isPresent()) {
                ReleasingApplication releasingApplication = new ReleasingApplication(
                        applicationMetaId, applicationMetaDTO.getName(),
                        existingNodeDTO.get().getId(), existingNodeDTO.get().getName(),
                        existingServerDTO.get().getIp(), appId);
                int id = new Long((appId % uapolloCustomConfiguration.getReleaseThreadNums()))
                        .intValue();
                UapolloCache.RELEASING_APPLICATIONS_MAP.get(id).add(releasingApplication);
                log.info(
                        "getReleasingApplicationListByApplicationMetaId for applicationMetaId: {} add releasingApplication: {}",
                        applicationMetaId, releasingApplication);
                releasingResults.add(new ReleasingResult(ReleasingStep.RELEASING_APPLICATION, appId)
                        .setReleasingStepResult(ReleasingStepResult.SUCCESS)
                        .setDetail(releasingApplication));
            } else {
                releasingResults.add(new ReleasingResult(ReleasingStep.RELEASING_APPLICATION, appId)
                        .setReleasingStepResult(ReleasingStepResult.NULL)
                        .setDetail(applicationMetaId + "_-1_" + appId));
            }
        });

        return releasingResults;
    }
}

/**
 * created on 2018年5月23日 下午5:15:06
 */
package com.utstar.uapollo.custom.task;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.utstar.uapollo.custom.config.UapolloCache;
import com.utstar.uapollo.custom.config.UapolloCustomConfiguration;
import com.utstar.uapollo.custom.dto.ReleasingApplication;
import com.utstar.uapollo.custom.service.ApplicationConfigCustomService;
import com.utstar.uapollo.custom.service.ApplicationConfigRelaseService;
import com.utstar.uapollo.custom.service.PrivateConfigCustomService;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.service.ApplicationConfigService;
import com.utstar.uapollo.service.dto.ApplicationConfigDTO;
import com.utstar.uapollo.service.dto.PrivateConfigDTO;

/**
 * @author UTSC0167
 * @date 2018年5月23日
 *
 */
@Component
public final class ApplicationConfigRelaseTask {

    private final Logger log = LoggerFactory.getLogger(ApplicationConfigRelaseTask.class);

    private final UapolloCustomConfiguration customConfiguration;

    private final PrivateConfigCustomService privateConfigCustomService;

    private final ApplicationConfigService applicationConfigService;

    private final ApplicationConfigCustomService applicationConfigCustomService;

    private final ApplicationConfigRelaseService applicationConfigRelaseService;

    public ApplicationConfigRelaseTask(UapolloCustomConfiguration customConfiguration,
            PrivateConfigCustomService privateConfigCustomService,
            ApplicationConfigService applicationConfigService,
            ApplicationConfigCustomService applicationConfigCustomService,
            ApplicationConfigRelaseService applicationConfigRelaseService) {
        super();
        this.customConfiguration = customConfiguration;
        this.privateConfigCustomService = privateConfigCustomService;
        this.applicationConfigService = applicationConfigService;
        this.applicationConfigCustomService = applicationConfigCustomService;
        this.applicationConfigRelaseService = applicationConfigRelaseService;
    }

    @PostConstruct
    public final void initRelease() {

        log.info("initRelease begin to run.");
        initReleasingCache();
        initRleaseTask();
        log.info("initRelease end to run.");
    }

    private final void initReleasingCache() {

        final int releaseThreadNums = customConfiguration.getReleaseThreadNums();
        log.info("initReleasingCache begin to run. the release thread nums: {}", releaseThreadNums);
        for (int i = 0; i < releaseThreadNums; ++i) {
            UapolloCache.RELEASING_APPLICATIONS_MAP.put(i, new LinkedBlockingQueue<>());
        }
        log.info(
                "initReleasingCache begin to run. UapolloCache.RELEASING_APPLICATIONS_MAP size: {}",
                UapolloCache.RELEASING_APPLICATIONS_MAP.size());
    }

    private final void initRleaseTask() {

        final int releaseThreadNums = customConfiguration.getReleaseThreadNums();
        log.info("initRleaseTask begin to run.");
        for (int i = 0; i < releaseThreadNums; ++i) {
            new Thread(new ApplicationConfigRelaseThread(i), "ConfigReleaseThread-" + i).start();
        }
        log.info("initRleaseTask end to run.");

    }

    private final class ApplicationConfigRelaseThread implements Runnable {

        private final int sleepTime = 50;

        private final int id;

        public ApplicationConfigRelaseThread(int id) {
            super();
            this.id = id;
        }

        @Override
        public void run() {

            log.info("release thread {} bein to run.", id);
            while (true) {

                ReleasingApplication releasingApplication = null;
                try {
                    releasingApplication = UapolloCache.RELEASING_APPLICATIONS_MAP.get(id).take();
                } catch (InterruptedException e) {
                    log.info(
                            "run RELEASING_APPLICATIONS_MAP.get({}).take() get releasingApplication generate exception:",
                            id, e);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e1) {
                        log.info(
                                "after get releasingApplication generate exception to sleep: {} generate exception:",
                                sleepTime);
                    }
                }
                log.info("begin handle releasingApplication: {}", releasingApplication);

                Optional<String> existingCachingApplicationConfig = generateApplicationConfig(
                        releasingApplication);
                if (!existingCachingApplicationConfig.isPresent()) {
                    log.info(
                            "generateApplicationConfig for aplication: {} ip: {} id: {} the applicationConfig is null, do nothing and continue.",
                            releasingApplication.getApplicationName(), releasingApplication.getIp(),
                            releasingApplication.getApplicationId());
                    continue;
                }

                final Long applicationId = releasingApplication.getApplicationId();
                final String cachingApplicationConfig = existingCachingApplicationConfig.get();
                log.info(
                        "generateApplicationConfig for aplication: {} ip: {} id: {} the applicationConfig: {}",
                        releasingApplication.getApplicationName(), releasingApplication.getIp(),
                        releasingApplication.getApplicationId(), cachingApplicationConfig);
                final ApplicationConfigDTO applicationConfigDTO = saveApplicationConfig(
                        applicationId, cachingApplicationConfig);
                log.info(
                        "saveApplicationConfig for applicatonId: {} finished get applicationConfigId: {}",
                        applicationId, applicationConfigDTO.getId());
                applicationConfigRelaseService.updateReleasedApplicationConfigCache(
                        releasingApplication.getIp(), releasingApplication.getApplicationName(),
                        applicationConfigDTO);

                log.info("end handle releasingApplication for aplication: {} ip: {} id: {} ",
                        releasingApplication.getApplicationName(), releasingApplication.getIp(),
                        releasingApplication.getApplicationId());
            }

        }

        private final Optional<String> generateApplicationConfig(
                ReleasingApplication releasingApplication) {

            TreeMap<String, String> metaConfigMap = UapolloCache.APPLICATION_META_CONFIG_MAP
                    .get(releasingApplication.getApplicationMetaId());
            if (metaConfigMap == null) {
                log.info(
                        "generateApplicationConfig for application: {} metaId: {} node: {} ip: {} the get applicationMeta config is null, return null.",
                        releasingApplication.getApplicationName(),
                        releasingApplication.getApplicationMetaId(),
                        releasingApplication.getNodeName(), releasingApplication.getIp());
                return Optional.empty();
            }

            List<TreeMap<String, String>> configMapList = new LinkedList<>();
            configMapList.add(metaConfigMap);

            TreeMap<String, String> globalConfigMap = UapolloCache.GLOBAL_CONFIG_MAP
                    .get(releasingApplication.getApplicationMetaId());
            if (globalConfigMap != null)
                configMapList.add(globalConfigMap);

            Map<Long, TreeMap<String, String>> metaNodeConfigMap = UapolloCache.NODE_CONFIG_MAP
                    .get(releasingApplication.getApplicationMetaId());
            if (metaNodeConfigMap != null) {
                TreeMap<String, String> nodeConfigMap = metaNodeConfigMap
                        .get(releasingApplication.getNodeId());
                if (nodeConfigMap != null)
                    configMapList.add(nodeConfigMap);
            }

            TreeMap<String, String> privateConfigMap = getPrivateCofnig(
                    releasingApplication.getApplicationId());
            if (privateConfigMap != null) {
                configMapList.add(privateConfigMap);
            }

            TreeMap<String, String> configMap = configMapList.stream().map(Map::entrySet)
                    .flatMap(Set::stream).collect(Collectors.toMap(Map.Entry::getKey,
                            Map.Entry::getValue, (x, y) -> y, TreeMap::new));
            return UapolloUtil.objectToJson(configMap);
        }

        private final TreeMap<String, String> getPrivateCofnig(Long applicationId) {

            List<PrivateConfigDTO> privateConfigDTOs = privateConfigCustomService
                    .findByApplicationId(applicationId);
            if (privateConfigDTOs.size() == 0)
                return null;

            return privateConfigDTOs.stream().collect(Collectors.toMap(PrivateConfigDTO::getKey,
                    PrivateConfigDTO::getValue, (x, y) -> y, TreeMap::new));
        }

        private final ApplicationConfigDTO saveApplicationConfig(final Long applicationId,
                final String cacheApplicationConfig) {

            boolean saveFlag = true;
            ApplicationConfigDTO applicationConfigDTO = null;

            Optional<ApplicationConfigDTO> existingApplicationConfigDTO = applicationConfigCustomService
                    .findByApplicationIdAndCreateTimeMax(applicationId);
            if (existingApplicationConfigDTO.isPresent()) {
                ApplicationConfigDTO dbApplicationConfigDTO = existingApplicationConfigDTO.get();
                String dbContent = dbApplicationConfigDTO.getConfigContent();
                if (dbContent.equals(cacheApplicationConfig)) {
                    applicationConfigDTO = dbApplicationConfigDTO;
                    saveFlag = false;
                    log.info(
                            "saveApplicationConfig for applicationId: {} the applicationConfig no change, don't need to save.",
                            applicationId);
                }
            }

            if (saveFlag) {
                applicationConfigDTO = new ApplicationConfigDTO();
                applicationConfigDTO.setId(null);
                applicationConfigDTO.setApplicationId(applicationId);
                applicationConfigDTO.setConfigContent(cacheApplicationConfig);
                applicationConfigDTO.setModifyTime(UapolloUtil.getUnixTime());
                if (!existingApplicationConfigDTO.isPresent())
                    applicationConfigDTO.setCreateTime(UapolloUtil.getUnixTime());
                applicationConfigDTO = applicationConfigService.save(applicationConfigDTO);
            }

            return applicationConfigDTO;
        }
    }
}
/**
 * created on 2018年5月8日 上午11:03:56
 */
package com.utstar.uapollo.custom.task;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.utstar.uapollo.custom.config.UapolloCustomConfiguration;
import com.utstar.uapollo.custom.dto.ApplicationMetaReloadResult;
import com.utstar.uapollo.custom.service.ApplicationMetaCustomService;
import com.utstar.uapollo.custom.util.UapolloUtil;

/**
 * @author UTSC0167
 * @date 2018年5月8日
 *
 */
@Component
public class ApplicationMetaReloadTask {

    private final Logger log = LoggerFactory.getLogger(ApplicationMetaReloadTask.class);

    private final UapolloCustomConfiguration customConfiguration;

    private final ApplicationMetaCustomService applicationMetaCustomService;

    public ApplicationMetaReloadTask(UapolloCustomConfiguration customConfiguration,
            ApplicationMetaCustomService applicationMetaCustomService) {
        this.customConfiguration = customConfiguration;
        this.applicationMetaCustomService = applicationMetaCustomService;
    }

    @PostConstruct
    public final void initReload() {

        log.info("initReload begin to run.");
        runReloadTask();
        log.info("initReload end to run.");
    }

    @Scheduled(cron = "${uapollo.application.meta.reload.task.cron-expression:6 6 6 * *}")
    public void scheduledReload() {

        log.info("scheduledReload begin to run.");
        runReloadTask();
        log.info("scheduledReload end to run.");
    }

    private final void runReloadTask() {

        final String templateConfigurationLocation = customConfiguration
                .getTemplateConfigurationLocation();
        Optional<List<ApplicationMetaReloadResult>> existingApplicationMetaReloadResultList = applicationMetaCustomService
                .reload(templateConfigurationLocation);
        if (existingApplicationMetaReloadResultList.isPresent())
            log.info("runReloadTask for templateConfigurationLocation: {} get the result:\n{}",
                    templateConfigurationLocation,
                    UapolloUtil.objectToPrettyJson(existingApplicationMetaReloadResultList.get())
                            .orElse("convert json is null"));
        else
            log.info("runReloadTask for templateConfigurationLocation: {} get the result is null.",
                    templateConfigurationLocation);
    }
}

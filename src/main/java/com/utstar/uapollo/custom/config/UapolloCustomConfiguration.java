/**
 * created on 2018年4月17日 上午11:51:07
 */
package com.utstar.uapollo.custom.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author UTSC0167
 * @date 2018年4月17日
 *
 */
@Configuration
@Import(UapolloConfigRegistrar.class)
public class UapolloCustomConfiguration {

    private static final Logger log = LoggerFactory.getLogger(UapolloCustomConfiguration.class);

    private final String applicationMetaReloadTaskCronExpression;

    @Value("${spring.application.name:uapollo}")
    private String applicationName;

    @Value("${uapollo.application.meta.reload.try-lock-time:3}")
    private int applicationMetaReloadTryLockTime;

    @Value("${spring.application.version:1.0.0}")
    private String applicationVersion;

    @Value("${uapollo.task.scheduler.pool-size:10}")
    private int taskSchedulerPoolSize;

    @Value("${uapollo.template.configuration.location:./template/configuration}")
    private String templateConfigurationLocation;
    
    @Value("${uapollo.application.release.thread-nums:1}")
    private int releaseThreadNums;

    public UapolloCustomConfiguration(
            @Value("${uapollo.application.meta.reload.task.cron-expression:0 33 0/3 * * ?}") String applicationMetaReloadTaskCronExpression) {
        this.applicationMetaReloadTaskCronExpression = applicationMetaReloadTaskCronExpression;
    }

    @PostConstruct
    public void init() {
        log.info("init {}", toString());
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public int getApplicationMetaReloadTryLockTime() {
        return applicationMetaReloadTryLockTime;
    }

    public void setApplicationMetaReloadTryLockTime(int applicationMetaReloadTryLockTime) {
        this.applicationMetaReloadTryLockTime = applicationMetaReloadTryLockTime;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public int getTaskSchedulerPoolSize() {
        return taskSchedulerPoolSize;
    }

    public void setTaskSchedulerPoolSize(int taskSchedulerPoolSize) {
        this.taskSchedulerPoolSize = taskSchedulerPoolSize;
    }

    public String getTemplateConfigurationLocation() {
        return templateConfigurationLocation;
    }

    public void setTemplateConfigurationLocation(String templateConfigurationLocation) {
        this.templateConfigurationLocation = templateConfigurationLocation;
    }

    public int getReleaseThreadNums() {
        return releaseThreadNums;
    }

    public void setReleaseThreadNums(int releaseThreadNums) {
        this.releaseThreadNums = releaseThreadNums;
    }

    public String getApplicationMetaReloadTaskCronExpression() {
        return applicationMetaReloadTaskCronExpression;
    }

    @Override
    public String toString() {
        return "UapolloCustomConfiguration [applicationMetaReloadTaskCronExpression="
                + applicationMetaReloadTaskCronExpression + ", applicationName=" + applicationName
                + ", applicationMetaReloadTryLockTime=" + applicationMetaReloadTryLockTime
                + ", applicationVersion=" + applicationVersion + ", taskSchedulerPoolSize="
                + taskSchedulerPoolSize + ", templateConfigurationLocation="
                + templateConfigurationLocation + ", releaseThreadNums=" + releaseThreadNums + "]";
    }
}

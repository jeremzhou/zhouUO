/**
 * created on 2018年5月8日 下午2:28:31
 */
package com.utstar.uapollo.custom.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author UTSC0167
 * @date 2018年5月8日
 *
 */
@Configuration
public class AsyncTaskConfiguration {

    private final Logger log = LoggerFactory.getLogger(AsyncTaskConfiguration.class);

    private final UapolloCustomConfiguration customConfiguration;

    public AsyncTaskConfiguration(UapolloCustomConfiguration customConfiguration) {
        this.customConfiguration = customConfiguration;
    }

    @Bean
    public TaskScheduler geTaskScheduler() {

        final int poolSize = customConfiguration.getTaskSchedulerPoolSize();
        log.info("Creating Async TaskScheduler. poolSize: {}", poolSize);
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(poolSize);
        threadPoolTaskScheduler.setThreadNamePrefix("uapollo-task-");
        return threadPoolTaskScheduler;
    }
}

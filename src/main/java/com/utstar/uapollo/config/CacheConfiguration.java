package com.utstar.uapollo.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.utstar.uapollo.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.Project.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.Node.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.Server.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.ApplicationMeta.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.ApplicationMetaHistory.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.Application.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.ApplicationConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.PrivateConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.PrivateConfigHistory.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.NodeConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.NodeConfigHistory.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.GlobalConfig.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.GlobalConfigHistory.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.ProjectHistory.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.NodeHistory.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.ServerHistory.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.ApplicationHistory.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.ApplicationConfigHistory.class.getName(), jcacheConfiguration);
            cm.createCache(com.utstar.uapollo.domain.ClientHeartbeatInfo.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}

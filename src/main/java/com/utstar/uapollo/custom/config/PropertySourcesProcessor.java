package com.utstar.uapollo.custom.config;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.FileSystemResource;

import io.github.jhipster.config.JHipsterConstants;

public class PropertySourcesProcessor
        implements BeanFactoryPostProcessor, EnvironmentAware, PriorityOrdered {

    private final Logger log = LoggerFactory.getLogger(PropertySourcesProcessor.class);

    private ConfigurableEnvironment environment;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        initializePropertySources();
    }

    protected void initializePropertySources() {

        log.info("initializePropertySources before userDir: {} spring.property.path: {}",
                System.getProperty("user.dir"), System.getProperty("spring.property.path"));
        String userDir = System.getProperty("user.dir");
        String devBaseDir = "/src/main";
        String propertyPath = userDir + devBaseDir;
        File propertyPathFile = new File(propertyPath);
        if (!propertyPathFile.exists())
            propertyPath = userDir;
        log.info("initializePropertySources finally userDir: {} spring.property.path: {}",
                System.getProperty("user.dir"), System.getProperty("spring.property.path"));

        Collection<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        log.info("initializePropertySources Profiles: {}", activeProfiles);

        String configLocation = propertyPath + "/config/uapollo.yml";
        if (activeProfiles.contains(JHipsterConstants.SPRING_PROFILE_DEVELOPMENT))
            configLocation = propertyPath + "/config/uapollo-dev.yml";
        System.getProperties().setProperty("spring.property.path", propertyPath);
        System.getProperties().setProperty("spring.config.location", "file:" + configLocation);

        YamlPropertySourceLoader yamlLoader = new YamlPropertySourceLoader();
        FileSystemResource resource = new FileSystemResource(new File(configLocation));
        try {
            PropertySource<?> yamlPropertySource = yamlLoader.load("YAML", resource, null);
            environment.getPropertySources().addFirst(yamlPropertySource);
        } catch (IOException e) {
            log.error(
                    "initializePropertySources yamlLoader load yaml configlocation: {} generate exception",
                    configLocation);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        // it is safe enough to cast as all known environment is derived from
        // ConfigurableEnvironment
        this.environment = (ConfigurableEnvironment) environment;
    }

    @Override
    public int getOrder() {
        // make it as early as possible
        return Ordered.HIGHEST_PRECEDENCE;
    }
}

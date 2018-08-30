package com.utstar.uapollo.custom.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.type.AnnotationMetadata;

import com.utstar.uapollo.custom.util.BeanRegistrationUtil;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class UapolloConfigRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {

        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,
                PropertySourcesPlaceholderConfigurer.class.getName(),
                PropertySourcesPlaceholderConfigurer.class);

        BeanRegistrationUtil.registerBeanDefinitionIfNotExists(registry,
                PropertySourcesProcessor.class.getName(), PropertySourcesProcessor.class);
    }
}

/**
 * created on 2018年5月4日 下午1:20:47
 */
package com.utstar.uapollo.custom.template.impl;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.utstar.uapollo.custom.dto.DefaultApplicationConfigurationTeamplate;
import com.utstar.uapollo.custom.dto.DefaultProjectConfigurationTemplate;
import com.utstar.uapollo.custom.template.LocalFileSystem;
import com.utstar.uapollo.custom.util.UapolloUtil;

/**
 * @author UTSC0167
 * @date 2018年5月4日
 *
 */
@Component
public class LocalFileSystemImpl implements LocalFileSystem {

    private static final Logger log = LoggerFactory.getLogger(LocalFileSystemImpl.class);

    @Override
    public Optional<List<DefaultProjectConfigurationTemplate>> readDefaultConfigurationTemplate(
            String templateConfigurationLocation) {

        log.info("readDefaultConfigurationTemplate start for templateConfigurationLocation: {}",
                templateConfigurationLocation);
        Optional<List<DefaultProjectConfigurationTemplate>> existingDefaultProjectConfigurationList = Optional
                .empty();
        File baseDir = new File(templateConfigurationLocation);
        if (!baseDir.exists()) {
            log.info(
                    "readDefaultConfigurationTemplate the templateConfigurationLocation: {} don't exists. return null!",
                    templateConfigurationLocation);
            return existingDefaultProjectConfigurationList;
        }

        List<DefaultProjectConfigurationTemplate> defaultProjectConfigurationList = new LinkedList<>();
        for (File projectFile : baseDir.listFiles()) {

            if (projectFile.isFile()) {
                log.info(
                        "readDefaultConfigurationTemplate the projectFile: {} is file, do nothing and continue.",
                        projectFile);
                continue;
            }

            String projectName = FilenameUtils.getName(projectFile.toString());
            DefaultProjectConfigurationTemplate projectConfigurationTemplate = new DefaultProjectConfigurationTemplate(
                    projectName);

            log.info("readDefaultConfigurationTemplate for project: {} use project directory: {}",
                    projectName, projectFile);
            for (File applicationFile : projectFile.listFiles()) {

                if (applicationFile.isFile()) {
                    log.info(
                            "readDefaultConfigurationTemplate the applicationFile: {} is file, do nothing and continue.",
                            applicationFile);
                    continue;
                }

                String applicationName = FilenameUtils.getName(applicationFile.toString());
                log.info(
                        "readDefaultConfigurationTemplate for project: {} application: {} use application directory: {}",
                        projectName, applicationName, applicationFile);
                for (File configFile : applicationFile.listFiles()) {

                    if (configFile.isDirectory()) {
                        log.info(
                                "readDefaultConfigurationTemplate the configFile: {} is directory, do nothing and continue.",
                                configFile);
                        continue;
                    }

                    String configFileName = FilenameUtils.getName(configFile.toString());
                    log.info(
                            "readDefaultConfigurationTemplate for project: {} application: {} configFile: {} use configFile directory: {}",
                            projectName, applicationName, configFileName, configFile);
                    if (UapolloUtil.isDefaultConfigFile(applicationName, configFileName)) {

                        log.info(
                                "readDefaultConfigurationTemplate for the application: {} get defaultConfigFile: {}",
                                applicationName, configFileName);
                        Optional<String> existingConfigContent = Optional
                                .empty();
                        if (UapolloUtil.isYamlFile(configFileName)) {
                            existingConfigContent = UapolloUtil.loadYamlFile(configFile);
                        } else if (UapolloUtil.isPropertiesFile(configFileName)) {
                            existingConfigContent = UapolloUtil.loadPropertiesFile(configFile);
                        } else {
                            log.info(
                                    "readDefaultConfigurationTemplate for project: {} application: {} configFile: {} isn't default config file format, do nothing and continue.",
                                    projectName, applicationName, configFileName);
                            continue;
                        }

                        if (existingConfigContent.isPresent()) {
                            DefaultApplicationConfigurationTeamplate applicationConfigurationTeamplate = new DefaultApplicationConfigurationTeamplate(
                                    applicationName, configFileName,
                                    existingConfigContent.get());
                            projectConfigurationTemplate.getDefaultProjectConfigurationList()
                                    .add(applicationConfigurationTeamplate);
                        } else {
                            log.info(
                                    "readDefaultConfigurationTemplate for project: {} application: {} configFile: {} get config content is null, do nothing and continue.",
                                    projectName, applicationName, configFileName);
                            continue;
                        }
                    }
                }
            }

            if (projectConfigurationTemplate.getDefaultProjectConfigurationList().size() > 0) {
                log.info("readDefaultConfigurationTemplate for project: {} inlude application:",
                        projectName);
                projectConfigurationTemplate.getDefaultProjectConfigurationList().stream()
                        .forEach(config -> log.info(" {}", config.getApplicationName()));
                defaultProjectConfigurationList.add(projectConfigurationTemplate);
            } else {
                log.info(
                        "readDefaultConfigurationTemplate for project: {} the DefaultApplicationConfigurationTeamplate is null, do nothing.",
                        projectName, projectFile);
            }
        }

        return Optional.of(defaultProjectConfigurationList);
    }
}

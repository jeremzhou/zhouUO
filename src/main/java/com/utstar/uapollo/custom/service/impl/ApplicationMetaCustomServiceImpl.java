/**
 * created on 2018年4月17日 下午12:13:40
 */
package com.utstar.uapollo.custom.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.utstar.uapollo.custom.config.UapolloCustomConfiguration;
import com.utstar.uapollo.custom.dto.ApplicationMetaReloadResult;
import com.utstar.uapollo.custom.dto.DefaultApplicationConfigurationTeamplate;
import com.utstar.uapollo.custom.dto.DefaultProjectConfigurationTemplate;
import com.utstar.uapollo.custom.repositry.ApplicationMetaCustomRepository;
import com.utstar.uapollo.custom.service.ApplicationMetaCustomService;
import com.utstar.uapollo.custom.service.ProjectCustomService;
import com.utstar.uapollo.custom.service.enumeration.ReloadOperationResult;
import com.utstar.uapollo.custom.service.enumeration.ReloadType;
import com.utstar.uapollo.custom.template.LocalFileSystem;
import com.utstar.uapollo.domain.ApplicationMeta;
import com.utstar.uapollo.domain.enumeration.Operation;
import com.utstar.uapollo.service.ApplicationMetaService;
import com.utstar.uapollo.service.ProjectService;
import com.utstar.uapollo.service.dto.ApplicationMetaDTO;
import com.utstar.uapollo.service.dto.ProjectDTO;
import com.utstar.uapollo.service.mapper.ApplicationMetaMapper;

/**
 * @author UTSC0167
 * @date 2018年4月17日
 *
 */
@Service
@Transactional
public class ApplicationMetaCustomServiceImpl implements ApplicationMetaCustomService {

    private static final ReentrantLock LOCK = new ReentrantLock();

    private final Logger log = LoggerFactory.getLogger(ApplicationMetaCustomServiceImpl.class);

    private final ApplicationMetaCustomRepository applicationMetaCustomRepository;

    private final ApplicationMetaMapper applicationMetaMapper;

    private final LocalFileSystem localFileSystem;

    private final ProjectCustomService projectCustomService;

    private final ProjectService projectService;

    private final ApplicationMetaService applicationMetaService;

    private final UapolloCustomConfiguration customConfiguration;

    public ApplicationMetaCustomServiceImpl(
            ApplicationMetaCustomRepository applicationMetaCustomRepository,
            ApplicationMetaMapper applicationMetaMapper, LocalFileSystem localFileSystem,
            ProjectCustomService projectCustomService, ProjectService projectService,
            ApplicationMetaService applicationMetaService,
            UapolloCustomConfiguration customConfiguration) {
        this.applicationMetaCustomRepository = applicationMetaCustomRepository;
        this.applicationMetaMapper = applicationMetaMapper;
        this.localFileSystem = localFileSystem;
        this.projectCustomService = projectCustomService;
        this.projectService = projectService;
        this.applicationMetaService = applicationMetaService;
        this.customConfiguration = customConfiguration;
    }

    @Override
    @Transactional()
    public Optional<List<ApplicationMetaReloadResult>> reload(
            String templateConfigurationLocation) {

        final int tryLockTime = customConfiguration.getApplicationMetaReloadTryLockTime();
        log.info("reload start for templateConfigurationLocation: {} tryLockTime: {}",
                templateConfigurationLocation, tryLockTime);

        boolean result = false;
        ApplicationMetaReloadResult projectReloadResult = null;
        Optional<List<ApplicationMetaReloadResult>> existingApplicationMetaReloadResultList = Optional
                .empty();
        try {
            result = LOCK.tryLock(tryLockTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            projectReloadResult = new ApplicationMetaReloadResult(ReloadType.TRYLOCK, null,
                    ReloadType.TRYLOCK.toString(), null, ReloadOperationResult.EXCEPTION);
            log.info("reload for templateConfigurationLocation: {} generate exception:",
                    templateConfigurationLocation, e);
        }
        if (result) {
            log.info("reload for templateConfigurationLocation: {} get lock.",
                    templateConfigurationLocation);
            try {
                existingApplicationMetaReloadResultList = doReload(templateConfigurationLocation);
            } finally {
                LOCK.unlock();
            }
        } else {
            projectReloadResult = new ApplicationMetaReloadResult(ReloadType.TRYLOCK, null,
                    ReloadType.TRYLOCK.toString(), null, ReloadOperationResult.FAILURE);
            log.info(
                    "reload for templateConfigurationLocation: {} try lock timeout, do nothing and return.",
                    templateConfigurationLocation);
        }

        if (projectReloadResult != null && !existingApplicationMetaReloadResultList.isPresent()) {
            existingApplicationMetaReloadResultList = Optional
                    .of(new LinkedList<ApplicationMetaReloadResult>());
            existingApplicationMetaReloadResultList.get().add(projectReloadResult);
        }
        return existingApplicationMetaReloadResultList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationMetaDTO> findByProjectId(Long projectId) {

        log.debug("Request to get all ApplicationMetas by projectId: {}", projectId);
        return applicationMetaCustomRepository.findByProjectId(projectId).stream()
                .map(applicationMetaMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Optional<ApplicationMetaDTO> findByProjectIdAndName(Long projectId, String name) {

        log.debug("Request to get the ApplicationMeta by projectId: {} name: {}", projectId, name);
        Optional<ApplicationMeta> existingApplicationMeta = applicationMetaCustomRepository
                .findByProjectIdAndNameIgnoreCase(projectId, name);
        return existingApplicationMeta.map((value) -> (applicationMetaMapper.toDto(value)));
    }
    
    @Override
	public Optional<ApplicationMetaDTO> findByName(String name) {
    	
    	log.debug("Request to get the ApplicationMeta by name： {}", name);
    	Optional<ApplicationMeta> existingApplicationMeta = applicationMetaCustomRepository
    			.findByName(name);
		return existingApplicationMeta.map((value) -> (applicationMetaMapper.toDto(value)));
	}

    private final Optional<List<ApplicationMetaReloadResult>> doReload(
            String templateConfigurationLocation) {

        Optional<List<DefaultProjectConfigurationTemplate>> existingDefaultProjectConfigurationList = localFileSystem
                .readDefaultConfigurationTemplate(templateConfigurationLocation);
        if (!existingDefaultProjectConfigurationList.isPresent()) {
            log.info(
                    "reload for templateConfigurationLocation: {} localFileSystem readDefaultConfigurationTemplate is null, return null.",
                    templateConfigurationLocation);
            return Optional.empty();
        }
        log.info(
                "reload for templateConfigurationLocation: {} get defaultProjectConfigurationList size: {}",
                templateConfigurationLocation,
                existingDefaultProjectConfigurationList.get().size());

        List<ApplicationMetaReloadResult> applicationMetaReloadResultList = new LinkedList<>();
        for (DefaultProjectConfigurationTemplate projectConfigurationTemplate : existingDefaultProjectConfigurationList
                .get()) {

            String projectName = projectConfigurationTemplate.getProjectName();
            log.info("reload for project: {} begin.", projectName);
            ApplicationMetaReloadResult projectReloadResult = new ApplicationMetaReloadResult(
                    ReloadType.PROJECT, Operation.CREATE, projectName, null,
                    ReloadOperationResult.SUCCESS);
            Optional<ProjectDTO> existingProjectDTO = projectCustomService.findByName(projectName);
            Long projectId = null;
            if (existingProjectDTO.isPresent()) {
                projectReloadResult = new ApplicationMetaReloadResult(ReloadType.PROJECT, null,
                        projectName, null, ReloadOperationResult.EXISTS);
                projectId = existingProjectDTO.get().getId();
                log.info("reload for project: {} projectId: {} has exists.", projectName,
                        projectId);
            } else {
                log.info("reload for project: {} will be created.", projectName);
                ProjectDTO projectDTO = new ProjectDTO();
                projectDTO.setId(null);
                projectDTO.setName(projectName);
                projectDTO.setUserId(3);
                ProjectDTO dbProjectDTO = projectService.save(projectDTO);
                projectId = dbProjectDTO.getId();
                log.info("reload for project: {} create success. project detail: {}", projectName,
                        dbProjectDTO);
            }
            applicationMetaReloadResultList.add(projectReloadResult);

            List<DefaultApplicationConfigurationTeamplate> applicationConfigurationTeamplateList = projectConfigurationTemplate
                    .getDefaultProjectConfigurationList();
            log.info(
                    "reload for project: {} projectId: {} DefaultApplicationConfigurationTeamplateList size: {}",
                    projectName, projectId, applicationConfigurationTeamplateList.size());
            for (DefaultApplicationConfigurationTeamplate applicationConfigurationTeamplate : applicationConfigurationTeamplateList) {

                final String applicationName = applicationConfigurationTeamplate
                        .getApplicationName();
                final String configFile = applicationConfigurationTeamplate.getConfigFile();
                final String configContent = applicationConfigurationTeamplate.getConfigContent();
                log.info("reload for project: {} application: {} begin.", projectName,
                        applicationName);
                ApplicationMetaReloadResult applicationMetaReloadResult = new ApplicationMetaReloadResult(
                        ReloadType.APPLICATION, Operation.CREATE, applicationName, configFile,
                        ReloadOperationResult.SUCCESS);
                Optional<ApplicationMetaDTO> existingApplicationMetaDTO = findByProjectIdAndName(
                        projectId, applicationName);
                if (existingApplicationMetaDTO.isPresent()) {

                    ApplicationMetaDTO applicationMetaDTO = existingApplicationMetaDTO.get();
                    log.info(
                            "reload for project: {} application: {} has exists. the db configContent:\n{}",
                            projectName, applicationName, applicationMetaDTO.getConfigContent());
                    if (configContent.equals(applicationMetaDTO.getConfigContent())) {

                        applicationMetaReloadResult = new ApplicationMetaReloadResult(
                                ReloadType.APPLICATION, null, applicationName, configFile,
                                ReloadOperationResult.EXISTS);
                        log.info(
                                "reload for project: {} application: {} has exists and content equals, do nothing. the contnent:\n{}",
                                projectName, applicationName, configContent);

                    } else {
                        log.info(
                                "reload for project: {} application: {} the content will be update to:\n{}",
                                projectName, applicationName, configContent);
                        applicationMetaDTO.setConfigContent(configContent);
                        applicationMetaService.save(applicationMetaDTO);
                        applicationMetaReloadResult = new ApplicationMetaReloadResult(
                                ReloadType.APPLICATION, Operation.UPDATE, applicationName,
                                configFile, ReloadOperationResult.SUCCESS);
                    }
                } else {
                    log.info("reload for project: {} application: {} will be created.", projectName,
                            applicationName);
                    ApplicationMetaDTO applicationMetaDTO = new ApplicationMetaDTO();
                    applicationMetaDTO.setId(null);
                    applicationMetaDTO.setName(applicationName);
                    applicationMetaDTO.setConfigFile(configFile);
                    applicationMetaDTO.setConfigContent(configContent);
                    applicationMetaDTO.setProjectId(projectId);
                    ApplicationMetaDTO dbApplicationMetaDTO = applicationMetaService
                            .save(applicationMetaDTO);
                    log.info(
                            "reload for project: {} application: {} create success. the application detail: {}",
                            projectName, applicationName, dbApplicationMetaDTO);
                }
                applicationMetaReloadResultList.add(applicationMetaReloadResult);
            }

        }

        log.info(
                "reload end for templateConfigurationLocation: {} applicationMetaReloadResultList size: {}",
                templateConfigurationLocation, applicationMetaReloadResultList.size());
        return Optional.of(applicationMetaReloadResultList);
    }

}

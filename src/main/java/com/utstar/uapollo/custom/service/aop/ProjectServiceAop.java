/**
 * created on 2018年4月24日 上午10:32:27
 */
package com.utstar.uapollo.custom.service.aop;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.utstar.uapollo.custom.service.ProjectCustomService;
import com.utstar.uapollo.custom.service.enumeration.ResponseErrorKey;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.domain.enumeration.Operation;
import com.utstar.uapollo.service.ProjectHistoryService;
import com.utstar.uapollo.service.ProjectService;
import com.utstar.uapollo.service.dto.ProjectDTO;
import com.utstar.uapollo.service.dto.ProjectHistoryDTO;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;

/**
 * @author UTSC0167
 * @date 2018年4月24日
 *
 */
@Aspect
@Component
public class ProjectServiceAop {

    private final Logger log = LoggerFactory.getLogger(ProjectServiceAop.class);

    private static final String ENTITY_NAME = "project";

    private final ProjectService projectService;

    private final ProjectCustomService projectCustomService;

    private final ProjectHistoryService projectHistoryService;

    public ProjectServiceAop(ProjectService projectService,
            ProjectCustomService projectCustomService,
            ProjectHistoryService projectHistoryService) {
        this.projectService = projectService;
        this.projectCustomService = projectCustomService;
        this.projectHistoryService = projectHistoryService;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.ProjectServiceImpl.save(com.utstar.uapollo.service.dto.ProjectDTO)) "
            + "&& args(projectDTO)")
    public void saveProjectAop(ProjectDTO projectDTO) {
    };

    @Around("saveProjectAop(projectDTO)")
    public Object saveProjectAround(ProceedingJoinPoint pJoinPoint, ProjectDTO projectDTO)
            throws Throwable {

        Operation operation = Operation.CREATE;
        Long id = projectDTO.getId();
        String name = projectDTO.getName();
        log.info("@Around saveProjectAop begine for projectId: {} name: {}", id, name);

        Optional<ProjectDTO> existingProjectDTO = projectCustomService.findByName(name);
        long unixTime = UapolloUtil.getUnixTime();
        if (existingProjectDTO.isPresent()) {

            if (id != null)
                operation = Operation.UPDATE;

            log.info("@Around saveProjectAop the project name: {} has existed and can't {}",
                    name, operation);
            throw new BadRequestAlertException("The duplicate project can't " + operation,
                    ENTITY_NAME, ResponseErrorKey.DUPLICATE_ENTITY.toString());
        } else {

            projectDTO.setModifyTime(unixTime);
            if (id == null) {
                projectDTO.setCreateTime(unixTime);
            } else {

                ProjectDTO dbProjectDTO = projectService.findOne(id);
                operation = Operation.UPDATE;
                if (dbProjectDTO == null) {
                    log.info(
                            "@Around saveProjectAop the project id: {} name: {} don't exist and can't {}",
                            id, name, operation);
                    throw new BadRequestAlertException(
                            "The none-existent project can't " + operation, ENTITY_NAME,
                            ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
                } else {
                    projectDTO.setCreateTime(dbProjectDTO.getCreateTime());
                    logProject(dbProjectDTO, operation);
                }
            }
        }

        log.info("@Around saveProjectAop projectDTO: {}", projectDTO);
        Object object = pJoinPoint.proceed();

        if (operation.equals(Operation.CREATE)) {
            logProject((ProjectDTO) object, operation);
        }

        return object;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.ProjectServiceImpl.delete(java.lang.Long)) "
            + "&& args(id)")
    public void deleteProjectAop(Long id) {
    };

    @Around("deleteProjectAop(id)")
    public void deleteProjectAround(ProceedingJoinPoint pJoinPoint, Long id) throws Throwable {

        Operation operation = Operation.DELTEE;
        log.info("@Around deleteProjectAop begine for projectId: {}", id);
        ProjectDTO dbProjectDTO = projectService.findOne(id);
        if (dbProjectDTO == null) {
            log.info("@Around deleteProjectAop the project id: {} don't exist and can't {}", id,
                    operation);
            throw new BadRequestAlertException("The none-existent project can't " + operation,
                    ENTITY_NAME, ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
        }

        logProject(dbProjectDTO, operation);
        log.info("@Around deleteProjectAop projectDTO: {}", dbProjectDTO);
        pJoinPoint.proceed();
    }

    private final void logProject(ProjectDTO projectDTO, Operation operation) {

        Long id = projectDTO.getId();
        log.info("logProject for id: {} operation: {}", id, operation);
        ProjectHistoryDTO projectHistoryDTO = new ProjectHistoryDTO();
        projectHistoryDTO.setId(null);
        projectHistoryDTO.setProjectId(id);
        projectHistoryDTO.setName(projectDTO.getName());
        projectHistoryDTO.setUserId(projectDTO.getUserId());
        projectHistoryDTO.setOperation(operation);
        projectHistoryDTO.setCreateTime(UapolloUtil.getUnixTime());
        projectHistoryService.save(projectHistoryDTO);
    }
}

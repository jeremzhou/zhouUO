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

import com.utstar.uapollo.custom.service.ApplicationMetaCustomService;
import com.utstar.uapollo.custom.service.enumeration.ResponseErrorKey;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.domain.enumeration.Operation;
import com.utstar.uapollo.service.ApplicationMetaHistoryService;
import com.utstar.uapollo.service.ApplicationMetaService;
import com.utstar.uapollo.service.dto.ApplicationMetaDTO;
import com.utstar.uapollo.service.dto.ApplicationMetaHistoryDTO;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;

/**
 * @author UTSC0167
 * @date 2018年4月24日
 *
 */
@Aspect
@Component
public class ApplicationMetaServiceAop {

    private final Logger log = LoggerFactory.getLogger(ApplicationMetaServiceAop.class);

    private static final String ENTITY_NAME = "applicationMeta";

    private final ApplicationMetaService applicationMetaService;

    private final ApplicationMetaCustomService applicationMetaCustomService;

    private final ApplicationMetaHistoryService applicationMetaHistoryService;

    public ApplicationMetaServiceAop(ApplicationMetaService applicationMetaService,
            ApplicationMetaCustomService applicationMetaCustomService,
            ApplicationMetaHistoryService applicationMetaHistoryService) {
        this.applicationMetaService = applicationMetaService;
        this.applicationMetaCustomService = applicationMetaCustomService;
        this.applicationMetaHistoryService = applicationMetaHistoryService;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.ApplicationMetaServiceImpl.save(com.utstar.uapollo.service.dto.ApplicationMetaDTO)) "
            + "&& args(applicationMetaDTO)")
    public void saveApplicationMetaAop(ApplicationMetaDTO applicationMetaDTO) {
    };

    @Around("saveApplicationMetaAop(applicationMetaDTO)")
    public Object saveApplicationMetaAround(ProceedingJoinPoint pJoinPoint,
            ApplicationMetaDTO applicationMetaDTO) throws Throwable {

        Operation operation = Operation.CREATE;
        Long projectId = applicationMetaDTO.getProjectId();
        String name = applicationMetaDTO.getName();
        Long id = applicationMetaDTO.getId();
        String configContent = applicationMetaDTO.getConfigContent();
        log.info(
                "@Around saveApplicationMetaAop begine for applicationMetaId: {} projectId: {} name: {}",
                id, projectId, name);

        Optional<ApplicationMetaDTO> existingApplicationMetaDTO = applicationMetaCustomService
                .findByProjectIdAndName(projectId, name);
        long unixTime = UapolloUtil.getUnixTime();
        if (existingApplicationMetaDTO.isPresent()) {

            if (id == null) {

                log.info(
                        "@Around saveApplicationMetaAop the applicationMeta projectId: {} name: {} has existed and can't {}",
                        projectId, name, operation);
                throw new BadRequestAlertException(
                        "The duplicate applicationMeta can't " + operation, ENTITY_NAME,
                        ResponseErrorKey.DUPLICATE_ENTITY.toString());
            } else {

                operation = Operation.UPDATE;
                if (!id.equals(existingApplicationMetaDTO.get().getId())) {

                    log.info(
                            "@Around saveApplicationMetaAop the input applicationMeta id: {} mismatch and can't {}",
                            id, operation);
                    throw new BadRequestAlertException(
                            "The id mismatch applicationMeta can't " + operation, ENTITY_NAME,
                            ResponseErrorKey.ID_MISMATCH_ENTITY.toString());
                } else {

                    if (configContent.equals(existingApplicationMetaDTO.get().getConfigContent())) {

                        log.info(
                                "@Around saveApplicationMetaAop the applicationMeta projectId: {} name: {} hasn't be changed and can't {}",
                                projectId, name, operation);
                        throw new BadRequestAlertException(
                                "The non-changed applicationMeta can't " + operation, ENTITY_NAME,
                                ResponseErrorKey.NON_CHANGED_ENTITY.toString());
                    } else {
                        applicationMetaDTO.setModifyTime(unixTime);
                        logApplicationMeta(existingApplicationMetaDTO.get(), operation);
                    }
                }
            }
        } else {

            applicationMetaDTO.setModifyTime(unixTime);
            if (id == null) {
                applicationMetaDTO.setCreateTime(unixTime);
            } else {

                operation = Operation.UPDATE;
                log.info(
                        "@Around saveApplicationMetaAop the applicationMeta id: {} projectId: {} name: {} don't exist and can't {}",
                        id, projectId, name, operation);
                throw new BadRequestAlertException(
                        "The none-existent applicationMeta can't " + operation, ENTITY_NAME,
                        ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
            }
        }

        log.info("@Around saveApplicationMetaAop applicationMetaDTO: {}", applicationMetaDTO);
        Object object = pJoinPoint.proceed();
        if (operation.equals(Operation.CREATE))
            logApplicationMeta((ApplicationMetaDTO) object, operation);

        return object;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.ApplicationMetaServiceImpl.delete(java.lang.Long)) "
            + "&& args(id)")
    public void deleteApplicationMetaAop(Long id) {
    };

    @Around("deleteApplicationMetaAop(id)")
    public void deleteApplicationMetaAround(ProceedingJoinPoint pJoinPoint, Long id)
            throws Throwable {

        Operation operation = Operation.DELTEE;
        log.info("@Around deleteApplicationMetaAop begine for applicationMetaId: {}", id);
        ApplicationMetaDTO dbApplicationMetaDTO = applicationMetaService.findOne(id);
        if (dbApplicationMetaDTO == null) {
            log.info(
                    "@Around deleteApplicationMetaAop the applicationMeta id: {} don't exist and can't {}",
                    id, operation);
            throw new BadRequestAlertException(
                    "The none-existent applicationMeta can't " + operation, ENTITY_NAME,
                    ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
        }

        logApplicationMeta(dbApplicationMetaDTO, operation);
        log.info("@Around deleteApplicationMetaAop applicationMetaDTO: {}",
                dbApplicationMetaDTO);
        pJoinPoint.proceed();
    }

    private final void logApplicationMeta(ApplicationMetaDTO applicationMetaDTO,
            Operation operation) {

        Long id = applicationMetaDTO.getId();
        log.info("logApplicationMeta for id: {} operation: {}", id, operation);
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = new ApplicationMetaHistoryDTO();
        applicationMetaHistoryDTO.setId(null);
        applicationMetaHistoryDTO.setProjectId(applicationMetaDTO.getProjectId());
        applicationMetaHistoryDTO.setApplicationMetaId(id);
        applicationMetaHistoryDTO.setName(applicationMetaDTO.getName());
        applicationMetaHistoryDTO.setConfigFile(applicationMetaDTO.getConfigFile());
        applicationMetaHistoryDTO.setConfigContent(applicationMetaDTO.getConfigContent());
        applicationMetaHistoryDTO.setOperation(operation);
        applicationMetaHistoryDTO.setCreateTime(UapolloUtil.getUnixTime());
        applicationMetaHistoryService.save(applicationMetaHistoryDTO);

    }
}

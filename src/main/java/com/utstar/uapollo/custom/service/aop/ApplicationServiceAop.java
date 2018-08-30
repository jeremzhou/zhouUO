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

import com.utstar.uapollo.custom.service.ApplicationCustomService;
import com.utstar.uapollo.custom.service.enumeration.ResponseErrorKey;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.domain.enumeration.Operation;
import com.utstar.uapollo.service.ApplicationHistoryService;
import com.utstar.uapollo.service.ApplicationService;
import com.utstar.uapollo.service.dto.ApplicationDTO;
import com.utstar.uapollo.service.dto.ApplicationHistoryDTO;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;

/**
 * @author UTSC0167
 * @date 2018年4月24日
 *
 */
@Aspect
@Component
public class ApplicationServiceAop {

    private final Logger log = LoggerFactory.getLogger(ApplicationServiceAop.class);

    private static final String ENTITY_NAME = "application";

    private final ApplicationService applicationService;

    private final ApplicationCustomService applicationCustomService;

    private final ApplicationHistoryService applicationHistoryService;

    public ApplicationServiceAop(ApplicationService applicationService,
            ApplicationCustomService applicationCustomService,
            ApplicationHistoryService applicationHistoryService) {
        this.applicationService = applicationService;
        this.applicationCustomService = applicationCustomService;
        this.applicationHistoryService = applicationHistoryService;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.ApplicationServiceImpl.save(com.utstar.uapollo.service.dto.ApplicationDTO)) "
            + "&& args(applicationDTO)")
    public void saveApplicationAop(ApplicationDTO applicationDTO) {
    };

    @Around("saveApplicationAop(applicationDTO)")
    public Object saveApplicationAround(ProceedingJoinPoint pJoinPoint,
            ApplicationDTO applicationDTO) throws Throwable {

        Operation operation = Operation.CREATE;
        Long id = applicationDTO.getId();
        Long applicationMetaId = applicationDTO.getApplicationMetaId();
        Long serverId = applicationDTO.getServerId();
        log.info(
                "@Around saveApplicationAop begine for applicationId: {} applicationMetaId: {} serverId: {}",
                id, applicationMetaId, serverId);

        Optional<ApplicationDTO> existingApplicationDTO = applicationCustomService
                .findByApplicationMetaIdAndServerId(applicationMetaId, serverId);
        long unixTime = UapolloUtil.getUnixTime();
        if (existingApplicationDTO.isPresent()) {

            if (id != null)
                operation = Operation.UPDATE;

            log.info(
                    "@Around saveApplicationAop the application applicationMetaId: {} serverId: {} has existed and can't {}",
                    applicationMetaId, serverId, operation);
            throw new BadRequestAlertException("The duplicate application can't " + operation,
                    ENTITY_NAME, ResponseErrorKey.DUPLICATE_ENTITY.toString());
        } else {

            applicationDTO.setModifyTime(unixTime);
            if (id == null) {
                applicationDTO.setCreateTime(unixTime);
            } else {

                ApplicationDTO dbApplicationDTO = applicationService.findOne(id);
                operation = Operation.UPDATE;
                if (dbApplicationDTO == null) {
                    log.info(
                            "@Around saveApplicationAop the nodId: {} applicationMetaId: {} serverId: {} don't exist and can't {}",
                            id, applicationMetaId, serverId, operation);
                    throw new BadRequestAlertException(
                            "The none-existent application can't " + operation, ENTITY_NAME,
                            ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
                } else {
                    applicationDTO.setCreateTime(dbApplicationDTO.getCreateTime());
                    logApplication(dbApplicationDTO, operation);
                }
            }
        }

        log.info("@Around saveApplicationAop applicationDTO: {}", applicationDTO);
        Object object = pJoinPoint.proceed();

        if (operation.equals(Operation.CREATE)) {
            logApplication((ApplicationDTO) object, operation);
        }

        return object;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.ApplicationServiceImpl.delete(java.lang.Long)) "
            + "&& args(id)")
    public void deleteApplicationAop(Long id) {
    };

    @Around("deleteApplicationAop(id)")
    public void deleteApplicationAround(ProceedingJoinPoint pJoinPoint, Long id) throws Throwable {

        Operation operation = Operation.DELTEE;
        log.info("@Around deleteApplicationAop begine for applicationId: {}", id);
        ApplicationDTO dbApplicationDTO = applicationService.findOne(id);
        if (dbApplicationDTO == null) {
            log.info(
                    "@Around deleteApplicationAop the applicationId: {} don't exist and can't {}",
                    id, operation);
            throw new BadRequestAlertException("The none-existent application can't " + operation,
                    ENTITY_NAME, ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
        }

        logApplication(dbApplicationDTO, operation);
        log.info("@Around deleteApplicationAop applicationDTO: {}", dbApplicationDTO);
        pJoinPoint.proceed();
    }

    private final void logApplication(ApplicationDTO applicationDTO, Operation operation) {

        Long id = applicationDTO.getId();
        log.info("logApplication for id: {} operation: {}", id, operation);
        ApplicationHistoryDTO applicationHistoryDTO = new ApplicationHistoryDTO();
        applicationHistoryDTO.setId(null);
        applicationHistoryDTO.setApplicationMetaId(applicationDTO.getApplicationMetaId());
        applicationHistoryDTO.setApplicationId(id);
        applicationHistoryDTO.setServerId(applicationDTO.getServerId());
        applicationHistoryDTO.setOperation(operation);
        applicationHistoryDTO.setCreateTime(UapolloUtil.getUnixTime());
        applicationHistoryService.save(applicationHistoryDTO);
    }
}

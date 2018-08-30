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

import com.utstar.uapollo.custom.service.ApplicationConfigCustomService;
import com.utstar.uapollo.custom.service.enumeration.ResponseErrorKey;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.domain.enumeration.Operation;
import com.utstar.uapollo.service.ApplicationConfigHistoryService;
import com.utstar.uapollo.service.ApplicationConfigService;
import com.utstar.uapollo.service.dto.ApplicationConfigDTO;
import com.utstar.uapollo.service.dto.ApplicationConfigHistoryDTO;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;

/**
 * @author UTSC0167
 * @date 2018年4月24日
 *
 */
@Aspect
@Component
public class ApplicationConfigServiceAop {

    private final Logger log = LoggerFactory.getLogger(ApplicationConfigServiceAop.class);

    private static final String ENTITY_NAME = "applicationConfig";

    private final ApplicationConfigService applicationConfigService;

    private final ApplicationConfigCustomService applicationConfigCustomService;

    private final ApplicationConfigHistoryService applicationConfigHistoryService;

    public ApplicationConfigServiceAop(ApplicationConfigService applicationConfigService,
            ApplicationConfigCustomService applicationConfigCustomService,
            ApplicationConfigHistoryService applicationConfigHistoryService) {
        this.applicationConfigService = applicationConfigService;
        this.applicationConfigCustomService = applicationConfigCustomService;
        this.applicationConfigHistoryService = applicationConfigHistoryService;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.ApplicationConfigServiceImpl.save(com.utstar.uapollo.service.dto.ApplicationConfigDTO)) "
            + "&& args(applicationConfigDTO)")
    public void saveApplicationConfigAop(ApplicationConfigDTO applicationConfigDTO) {
    };

    @Around("saveApplicationConfigAop(applicationConfigDTO)")
    public Object saveApplicationConfigAround(ProceedingJoinPoint pJoinPoint,
            ApplicationConfigDTO applicationConfigDTO) throws Throwable {

        Operation operation = Operation.CREATE;
        Long applicationId = applicationConfigDTO.getApplicationId();
        Long id = applicationConfigDTO.getId();
        String configContent = applicationConfigDTO.getConfigContent();
        log.info(
                "@Around saveApplicationConfigAop begine for applicationConfigId: {} applicationId: {} and max create time",
                id, applicationId);

        Optional<ApplicationConfigDTO> existingApplicationConfigDTO = applicationConfigCustomService
                .findByApplicationIdAndCreateTimeMax(applicationId);
        long unixTime = UapolloUtil.getUnixTime();
        if (existingApplicationConfigDTO.isPresent()) {

            if (configContent.equals(existingApplicationConfigDTO.get().getConfigContent())) {

                log.info(
                        "@Around saveApplicationConfigAop the applicationConfig applicationId: {} and max create time hasn't be changed and can't {}",
                        applicationId, operation);
                return existingApplicationConfigDTO.get();
            }
        } else {

            if (id != null) {

                operation = Operation.UPDATE;
                log.info(
                        "@Around saveApplicationConfigAop the applicationConfig id: {} applicationId: {} and max create time don't exist and can't {}",
                        id, applicationId, operation);
                throw new BadRequestAlertException(
                        "The none-existent applicationConfig can't " + operation, ENTITY_NAME,
                        ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
            }
        }

        applicationConfigDTO.setModifyTime(unixTime);
        applicationConfigDTO.setCreateTime(unixTime);
        log.info("@Around saveApplicationConfigAop applicationConfigDTO: {}", applicationConfigDTO);
        Object object = pJoinPoint.proceed();
        logApplicationConfig((ApplicationConfigDTO) object, operation);

        return object;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.ApplicationConfigServiceImpl.delete(java.lang.Long)) "
            + "&& args(id)")
    public void deleteApplicationConfigAop(Long id) {
    };

    @Around("deleteApplicationConfigAop(id)")
    public void deleteApplicationConfigAround(ProceedingJoinPoint pJoinPoint, Long id)
            throws Throwable {

        Operation operation = Operation.DELTEE;
        log.info("@Around deleteApplicationConfigAop begine for applicationConfigId: {}", id);
        ApplicationConfigDTO dbApplicationConfigDTO = applicationConfigService.findOne(id);
        if (dbApplicationConfigDTO == null) {
            log.info(
                    "@Around deleteApplicationConfigAop the applicationConfig id: {} don't exist and can't {}",
                    id, operation);
            throw new BadRequestAlertException(
                    "The none-existent applicationConfig can't " + operation, ENTITY_NAME,
                    ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
        }

        logApplicationConfig(dbApplicationConfigDTO, operation);
        log.info("@Around deleteApplicationConfigAop applicationConfigDTO: {}",
                dbApplicationConfigDTO);
        pJoinPoint.proceed();
    }

    private final void logApplicationConfig(ApplicationConfigDTO applicationConfigDTO,
            Operation operation) {

        Long id = applicationConfigDTO.getId();
        log.info("logApplicationConfig for id: {} operation: {}", id, operation);
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = new ApplicationConfigHistoryDTO();
        applicationConfigHistoryDTO.setId(null);
        applicationConfigHistoryDTO.setApplicationId(applicationConfigDTO.getApplicationId());
        applicationConfigHistoryDTO.setApplicationConfigid(applicationConfigDTO.getId());
        applicationConfigHistoryDTO.setConfigContent(applicationConfigDTO.getConfigContent());
        applicationConfigHistoryDTO.setOperation(operation);
        applicationConfigHistoryDTO.setCreateTime(UapolloUtil.getUnixTime());
        applicationConfigHistoryService.save(applicationConfigHistoryDTO);

    }
}

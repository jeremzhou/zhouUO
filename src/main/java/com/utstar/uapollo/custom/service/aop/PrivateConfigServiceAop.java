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

import com.utstar.uapollo.custom.service.PrivateConfigCustomService;
import com.utstar.uapollo.custom.service.enumeration.ResponseErrorKey;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.domain.enumeration.Operation;
import com.utstar.uapollo.service.PrivateConfigHistoryService;
import com.utstar.uapollo.service.PrivateConfigService;
import com.utstar.uapollo.service.dto.PrivateConfigDTO;
import com.utstar.uapollo.service.dto.PrivateConfigHistoryDTO;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;

/**
 * @author UTSC0167
 * @date 2018年4月24日
 *
 */
@Aspect
@Component
public class PrivateConfigServiceAop {

    private final Logger log = LoggerFactory.getLogger(PrivateConfigServiceAop.class);

    private static final String ENTITY_NAME = "privateConfig";

    private final PrivateConfigService privateConfigService;

    private final PrivateConfigCustomService privateConfigCustomService;

    private final PrivateConfigHistoryService privateConfigHistoryService;

    public PrivateConfigServiceAop(PrivateConfigService privateConfigService,
            PrivateConfigCustomService privateConfigCustomService,
            PrivateConfigHistoryService privateConfigHistoryService) {
        this.privateConfigService = privateConfigService;
        this.privateConfigCustomService = privateConfigCustomService;
        this.privateConfigHistoryService = privateConfigHistoryService;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.PrivateConfigServiceImpl.save(com.utstar.uapollo.service.dto.PrivateConfigDTO)) "
            + "&& args(privateConfigDTO)")
    public void savePrivateConfigAop(PrivateConfigDTO privateConfigDTO) {
    };

    @Around("savePrivateConfigAop(privateConfigDTO)")
    public Object savePrivateConfigAround(ProceedingJoinPoint pJoinPoint,
            PrivateConfigDTO privateConfigDTO) throws Throwable {

        Operation operation = Operation.CREATE;
        Long id = privateConfigDTO.getId();
        Long applicationId = privateConfigDTO.getApplicationId();
        String key = privateConfigDTO.getKey();
        String value = privateConfigDTO.getValue();
        log.info(
                "@Around savePrivateConfigAop begine for privateConfigId: {} applicationId: {} key: {} value: {}",
                id, applicationId, key, value);

        Optional<PrivateConfigDTO> existingPrivateConfigDTO = privateConfigCustomService
                .findByApplicationIdAndKey(applicationId, key);
        long unixTime = UapolloUtil.getUnixTime();
        if (existingPrivateConfigDTO.isPresent()) {

            if (id == null) {

                log.info(
                        "@Around savePrivateConfigAop the privateConfig applicationId: {} key: {} has existed and can't {}",
                        applicationId, key, operation);
                throw new BadRequestAlertException("The duplicate privateConfig can't " + operation,
                        ENTITY_NAME, ResponseErrorKey.DUPLICATE_ENTITY.toString());
            } else {

                operation = Operation.UPDATE;
                if (!id.equals(existingPrivateConfigDTO.get().getId())) {

                    log.info(
                            "@Around savePrivateConfigAop the input privateConfig id: {} mismatch and can't {}",
                            id, operation);
                    throw new BadRequestAlertException(
                            "The id mismatch privateConfig can't " + operation, ENTITY_NAME,
                            ResponseErrorKey.ID_MISMATCH_ENTITY.toString());
                } else {

                    if (value.equals(existingPrivateConfigDTO.get().getValue())) {

                        log.info(
                                "@Around savePrivateConfigAop the privateConfig applicationId: {} key: {} value: {} hasn't be changed and can't {}",
                                applicationId, key, value, operation);
                        throw new BadRequestAlertException(
                                "The non-changed privateConfig can't " + operation, ENTITY_NAME,
                                ResponseErrorKey.NON_CHANGED_ENTITY.toString());
                    } else {
                        privateConfigDTO.setModifyTime(unixTime);
                        logPrivateConfig(existingPrivateConfigDTO.get(), operation);
                    }
                }
            }
        } else {

            privateConfigDTO.setModifyTime(unixTime);
            if (id == null) {
                privateConfigDTO.setCreateTime(unixTime);
            } else {

                operation = Operation.UPDATE;
                log.info(
                        "@Around savePrivateConfigAop the privateConfig id: {} applicationId: {} key: {} don't exist and can't {}",
                        id, applicationId, key, operation);
                throw new BadRequestAlertException(
                        "The none-existent privateConfig can't " + operation, ENTITY_NAME,
                        ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
            }
        }

        log.info("@Around savePrivateConfigAop privateConfigDTO: {}", privateConfigDTO);
        Object object = pJoinPoint.proceed();
        if (operation.equals(Operation.CREATE))
            logPrivateConfig((PrivateConfigDTO) object, operation);

        return object;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.PrivateConfigServiceImpl.delete(java.lang.Long)) "
            + "&& args(id)")
    public void deletePrivateConfigAop(Long id) {
    };

    @Around("deletePrivateConfigAop(id)")
    public void deletePrivateConfigAround(ProceedingJoinPoint pJoinPoint, Long id)
            throws Throwable {

        Operation operation = Operation.DELTEE;
        log.info("@Around deletePrivateConfigAop begine for privateConfigId: {}", id);
        PrivateConfigDTO dbPrivateConfigDTO = privateConfigService.findOne(id);
        if (dbPrivateConfigDTO == null) {
            log.info(
                    "@Around deletePrivateConfigAop the privateConfig id: {} don't exist and can't {}",
                    id, operation);
            throw new BadRequestAlertException("The none-existent privateConfig can't " + operation,
                    ENTITY_NAME, ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
        }

        logPrivateConfig(dbPrivateConfigDTO, operation);
        log.info("@Around deletePrivateConfigAop privateConfigDTO: {}", dbPrivateConfigDTO);
        pJoinPoint.proceed();
    }

    private final void logPrivateConfig(PrivateConfigDTO privateConfigDTO, Operation operation) {

        Long id = privateConfigDTO.getId();
        log.info("logPrivateConfig for id: {} operation: {}", id, operation);
        PrivateConfigHistoryDTO privateConfigHistoryDTO = new PrivateConfigHistoryDTO();
        privateConfigHistoryDTO.setId(null);
        privateConfigHistoryDTO.setApplicationId(privateConfigDTO.getApplicationId());
        privateConfigHistoryDTO.setPrivateConfigId(id);
        privateConfigHistoryDTO.setKey(privateConfigDTO.getKey());
        privateConfigHistoryDTO.setValue(privateConfigDTO.getValue());
        privateConfigHistoryDTO.setOperation(operation);
        privateConfigHistoryDTO.setCreateTime(UapolloUtil.getUnixTime());
        privateConfigHistoryService.save(privateConfigHistoryDTO);

    }
}

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

import com.utstar.uapollo.custom.service.GlobalConfigCustomService;
import com.utstar.uapollo.custom.service.enumeration.ResponseErrorKey;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.domain.enumeration.Operation;
import com.utstar.uapollo.service.GlobalConfigHistoryService;
import com.utstar.uapollo.service.GlobalConfigService;
import com.utstar.uapollo.service.dto.GlobalConfigDTO;
import com.utstar.uapollo.service.dto.GlobalConfigHistoryDTO;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;

/**
 * @author UTSC0167
 * @date 2018年4月24日
 *
 */
@Aspect
@Component
public class GlobalConfigServiceAop {

    private final Logger log = LoggerFactory.getLogger(GlobalConfigServiceAop.class);

    private static final String ENTITY_NAME = "globalConfig";

    private final GlobalConfigService globalConfigService;

    private final GlobalConfigCustomService globalConfigCustomService;

    private final GlobalConfigHistoryService globalConfigHistoryService;

    public GlobalConfigServiceAop(GlobalConfigService globalConfigService,
            GlobalConfigCustomService globalConfigCustomService,
            GlobalConfigHistoryService globalConfigHistoryService) {
        this.globalConfigService = globalConfigService;
        this.globalConfigCustomService = globalConfigCustomService;
        this.globalConfigHistoryService = globalConfigHistoryService;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.GlobalConfigServiceImpl.save(com.utstar.uapollo.service.dto.GlobalConfigDTO)) "
            + "&& args(globalConfigDTO)")
    public void saveGlobalConfigAop(GlobalConfigDTO globalConfigDTO) {
    };

    @Around("saveGlobalConfigAop(globalConfigDTO)")
    public Object saveGlobalConfigAround(ProceedingJoinPoint pJoinPoint,
            GlobalConfigDTO globalConfigDTO) throws Throwable {

        Operation operation = Operation.CREATE;
        Long id = globalConfigDTO.getId();
        Long applicationMetaId = globalConfigDTO.getApplicationMetaId();
        String key = globalConfigDTO.getKey();
        String value = globalConfigDTO.getValue();
        log.info(
                "@Around saveGlobalConfigAop begine for globalConfigId: {} applicationMetaId: {} key: {} value: {}",
                id, applicationMetaId, key, value);

        Optional<GlobalConfigDTO> existingGlobalConfigDTO = globalConfigCustomService
                .findByApplicationMetaIdAndKey(applicationMetaId, key);
        long unixTime = UapolloUtil.getUnixTime();
        if (existingGlobalConfigDTO.isPresent()) {

            if (id == null) {

                log.info(
                        "@Around saveGlobalConfigAop the globalConfig applicationMetaId: {} key: {} has existed and can't {}",
                        applicationMetaId, key, operation);
                throw new BadRequestAlertException("The duplicate globalConfig can't " + operation,
                        ENTITY_NAME, ResponseErrorKey.DUPLICATE_ENTITY.toString());
            } else {

                operation = Operation.UPDATE;
                if (!id.equals(existingGlobalConfigDTO.get().getId())) {

                    log.info(
                            "@Around saveGlobalConfigAop the input globalConfig id: {} mismatch and can't {}",
                            id, operation);
                    throw new BadRequestAlertException(
                            "The id mismatch globalConfig can't " + operation, ENTITY_NAME,
                            ResponseErrorKey.ID_MISMATCH_ENTITY.toString());
                } else {

                    if (value.equals(existingGlobalConfigDTO.get().getValue())) {

                        log.info(
                                "@Around saveGlobalConfigAop the globalConfig applicationMetaId: {} key: {} value: {} hasn't be changed and can't {}",
                                applicationMetaId, key, value, operation);
                        throw new BadRequestAlertException(
                                "The non-changed globalConfig can't " + operation, ENTITY_NAME,
                                ResponseErrorKey.NON_CHANGED_ENTITY.toString());
                    } else {
                        globalConfigDTO.setModifyTime(unixTime);
                        logGlobalConfig(existingGlobalConfigDTO.get(), operation);
                    }
                }
            }
        } else {

            globalConfigDTO.setModifyTime(unixTime);
            if (id == null) {
                globalConfigDTO.setCreateTime(unixTime);
            } else {

                operation = Operation.UPDATE;
                log.info(
                        "@Around saveGlobalConfigAop the globalConfig id: {} applicationMetaId: {} key: {} don't exist and can't {}",
                        id, applicationMetaId, key, operation);
                throw new BadRequestAlertException(
                        "The none-existent globalConfig can't " + operation, ENTITY_NAME,
                        ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
            }
        }

        log.info("@Around saveGlobalConfigAop globalConfigDTO: {}", globalConfigDTO);
        Object object = pJoinPoint.proceed();
        if (operation.equals(Operation.CREATE))
            logGlobalConfig((GlobalConfigDTO) object, operation);

        return object;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.GlobalConfigServiceImpl.delete(java.lang.Long)) "
            + "&& args(id)")
    public void deleteGlobalConfigAop(Long id) {
    };

    @Around("deleteGlobalConfigAop(id)")
    public void deleteGlobalConfigAround(ProceedingJoinPoint pJoinPoint, Long id) throws Throwable {

        Operation operation = Operation.DELTEE;
        log.info("@Around deleteGlobalConfigAop begine for globalConfigId: {}", id);
        GlobalConfigDTO dbGlobalConfigDTO = globalConfigService.findOne(id);
        if (dbGlobalConfigDTO == null) {
            log.info(
                    "@Around deleteGlobalConfigAop the globalConfig id: {} don't exist and can't {}",
                    id, operation);
            throw new BadRequestAlertException("The none-existent globalConfig can't " + operation,
                    ENTITY_NAME, ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
        }

        logGlobalConfig(dbGlobalConfigDTO, operation);
        log.info("@Around deleteGlobalConfigAop globalConfigDTO: {}", dbGlobalConfigDTO);
        pJoinPoint.proceed();
    }

    private final void logGlobalConfig(GlobalConfigDTO globalConfigDTO, Operation operation) {

        Long id = globalConfigDTO.getId();
        log.info("logGlobalConfig for id: {} operation: {}", id, operation);
        GlobalConfigHistoryDTO globalConfigHistoryDTO = new GlobalConfigHistoryDTO();
        globalConfigHistoryDTO.setId(null);
        globalConfigHistoryDTO.setApplicationMetaId(globalConfigDTO.getApplicationMetaId());
        globalConfigHistoryDTO.setGlobalConfigId(id);
        globalConfigHistoryDTO.setKey(globalConfigDTO.getKey());
        globalConfigHistoryDTO.setValue(globalConfigDTO.getValue());
        globalConfigHistoryDTO.setOperation(operation);
        globalConfigHistoryDTO.setCreateTime(UapolloUtil.getUnixTime());
        globalConfigHistoryService.save(globalConfigHistoryDTO);

    }
}

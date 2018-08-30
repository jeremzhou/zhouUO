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

import com.utstar.uapollo.custom.service.NodeConfigCustomService;
import com.utstar.uapollo.custom.service.enumeration.ResponseErrorKey;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.domain.enumeration.Operation;
import com.utstar.uapollo.service.NodeConfigHistoryService;
import com.utstar.uapollo.service.NodeConfigService;
import com.utstar.uapollo.service.dto.NodeConfigDTO;
import com.utstar.uapollo.service.dto.NodeConfigHistoryDTO;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;

/**
 * @author UTSC0167
 * @date 2018年4月24日
 *
 */
@Aspect
@Component
public class NodeConfigServiceAop {

    private final Logger log = LoggerFactory.getLogger(NodeConfigServiceAop.class);

    private static final String ENTITY_NAME = "nodeConfig";

    private final NodeConfigService nodeConfigService;

    private final NodeConfigCustomService nodeConfigCustomService;

    private final NodeConfigHistoryService nodeConfigHistoryService;

    public NodeConfigServiceAop(NodeConfigService nodeConfigService,
            NodeConfigCustomService nodeConfigCustomService,
            NodeConfigHistoryService nodeConfigHistoryService) {
        this.nodeConfigService = nodeConfigService;
        this.nodeConfigCustomService = nodeConfigCustomService;
        this.nodeConfigHistoryService = nodeConfigHistoryService;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.NodeConfigServiceImpl.save(com.utstar.uapollo.service.dto.NodeConfigDTO)) "
            + "&& args(nodeConfigDTO)")
    public void saveNodeConfigAop(NodeConfigDTO nodeConfigDTO) {
    };

    @Around("saveNodeConfigAop(nodeConfigDTO)")
    public Object saveNodeConfigAround(ProceedingJoinPoint pJoinPoint, NodeConfigDTO nodeConfigDTO)
            throws Throwable {

        Operation operation = Operation.CREATE;
        Long id = nodeConfigDTO.getId();
        Long applicationMetaId = nodeConfigDTO.getApplicationMetaId();
        Long nodeId = nodeConfigDTO.getNodeId();
        String key = nodeConfigDTO.getKey();
        String value = nodeConfigDTO.getValue();
        log.info(
                "@Around saveNodeConfigAop begine for nodeConfigId: {} applicationMetaId: {} nodeId: {} key: {} value: {}",
                id, applicationMetaId, nodeId, key, value);

        Optional<NodeConfigDTO> existingNodeConfigDTO = nodeConfigCustomService
                .findByApplicationMetaIdAndNodeIdAndKey(applicationMetaId, nodeId, key);
        long unixTime = UapolloUtil.getUnixTime();
        if (existingNodeConfigDTO.isPresent()) {

            if (id == null) {

                log.info(
                        "@Around saveNodeConfigAop the nodeConfig applicationMetaId: {} nodeId: {} key: {} has existed and can't {}",
                        applicationMetaId, nodeId, key, operation);
                throw new BadRequestAlertException("The duplicate nodeConfig can't " + operation,
                        ENTITY_NAME, ResponseErrorKey.DUPLICATE_ENTITY.toString());
            } else {

                operation = Operation.UPDATE;
                if (!id.equals(existingNodeConfigDTO.get().getId())) {

                    log.info(
                            "@Around saveNodeConfigAop the input nodeConfig id: {} mismatch and can't {}",
                            id, operation);
                    throw new BadRequestAlertException(
                            "The id mismatch nodeConfig can't " + operation, ENTITY_NAME,
                            ResponseErrorKey.ID_MISMATCH_ENTITY.toString());
                } else {

                    if (value.equals(existingNodeConfigDTO.get().getValue())) {

                        log.info(
                                "@Around saveNodeConfigAop the nodeConfig applicationMetaId: {} nodeId: {} key: {} value: {} hasn't be changed and can't {}",
                                applicationMetaId, nodeId, key, value, operation);
                        throw new BadRequestAlertException(
                                "The non-changed nodeConfig can't " + operation, ENTITY_NAME,
                                ResponseErrorKey.NON_CHANGED_ENTITY.toString());
                    } else {
                        nodeConfigDTO.setModifyTime(unixTime);
                        logNodeConfig(existingNodeConfigDTO.get(), operation);
                    }
                }
            }
        } else {

            nodeConfigDTO.setModifyTime(unixTime);
            if (id == null) {
                nodeConfigDTO.setCreateTime(unixTime);
            } else {

                operation = Operation.UPDATE;
                log.info(
                        "@Around saveNodeConfigAop the nodeConfig id: {} applicationMetaId: {} nodeId: {} key: {} don't exist and can't {}",
                        id, applicationMetaId, nodeId, key, operation);
                throw new BadRequestAlertException(
                        "The none-existent nodeConfig can't " + operation, ENTITY_NAME,
                        ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
            }
        }

        log.info("@Around saveNodeConfigAop nodeConfigDTO: {}", nodeConfigDTO);
        Object object = pJoinPoint.proceed();
        if (operation.equals(Operation.CREATE))
            logNodeConfig((NodeConfigDTO) object, operation);

        return object;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.NodeConfigServiceImpl.delete(java.lang.Long)) "
            + "&& args(id)")
    public void deleteNodeConfigAop(Long id) {
    };

    @Around("deleteNodeConfigAop(id)")
    public void deleteNodeConfigAround(ProceedingJoinPoint pJoinPoint, Long id) throws Throwable {

        Operation operation = Operation.DELTEE;
        log.info("@Around deleteNodeConfigAop begine for nodeConfigId: {}", id);
        NodeConfigDTO dbNodeConfigDTO = nodeConfigService.findOne(id);
        if (dbNodeConfigDTO == null) {
            log.info(
                    "@Around deleteNodeConfigAop the nodeConfig id: {} don't exist and can't {}",
                    id, operation);
            throw new BadRequestAlertException("The none-existent nodeConfig can't " + operation,
                    ENTITY_NAME, ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
        }

        logNodeConfig(dbNodeConfigDTO, operation);
        log.info("@Around deleteNodeConfigAop nodeConfigDTO: {}", dbNodeConfigDTO);
        pJoinPoint.proceed();
    }

    private final void logNodeConfig(NodeConfigDTO nodeConfigDTO, Operation operation) {

        Long id = nodeConfigDTO.getId();
        log.info("logNodeConfig for id: {} operation: {}", id, operation);
        NodeConfigHistoryDTO nodeConfigHistoryDTO = new NodeConfigHistoryDTO();
        nodeConfigHistoryDTO.setId(null);
        nodeConfigHistoryDTO.setApplicationMetaId(nodeConfigDTO.getApplicationMetaId());
        nodeConfigHistoryDTO.setNodeId(nodeConfigDTO.getNodeId());
        nodeConfigHistoryDTO.setNodeConfigId(id);
        nodeConfigHistoryDTO.setKey(nodeConfigDTO.getKey());
        nodeConfigHistoryDTO.setValue(nodeConfigDTO.getValue());
        nodeConfigHistoryDTO.setOperation(operation);
        nodeConfigHistoryDTO.setCreateTime(UapolloUtil.getUnixTime());
        nodeConfigHistoryService.save(nodeConfigHistoryDTO);

    }
}

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

import com.utstar.uapollo.custom.service.NodeCustomService;
import com.utstar.uapollo.custom.service.enumeration.ResponseErrorKey;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.domain.enumeration.Operation;
import com.utstar.uapollo.service.NodeHistoryService;
import com.utstar.uapollo.service.NodeService;
import com.utstar.uapollo.service.dto.NodeDTO;
import com.utstar.uapollo.service.dto.NodeHistoryDTO;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;

/**
 * @author UTSC0167
 * @date 2018年4月24日
 *
 */
@Aspect
@Component
public class NodeServiceAop {

    private final Logger log = LoggerFactory.getLogger(NodeServiceAop.class);

    private static final String ENTITY_NAME = "node";

    private final NodeService nodeService;

    private final NodeCustomService nodeCustomService;

    private final NodeHistoryService nodeHistoryService;

    public NodeServiceAop(NodeService nodeService, NodeCustomService nodeCustomService,
            NodeHistoryService nodeHistoryService) {
        this.nodeService = nodeService;
        this.nodeCustomService = nodeCustomService;
        this.nodeHistoryService = nodeHistoryService;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.NodeServiceImpl.save(com.utstar.uapollo.service.dto.NodeDTO)) "
            + "&& args(nodeDTO)")
    public void saveNodeAop(NodeDTO nodeDTO) {
    };

    @Around("saveNodeAop(nodeDTO)")
    public Object saveNodeAround(ProceedingJoinPoint pJoinPoint, NodeDTO nodeDTO) throws Throwable {

        Operation operation = Operation.CREATE;
        Long id = nodeDTO.getId();
        Long projectId = nodeDTO.getProjectId();
        String name = nodeDTO.getName();
        log.info("@Around saveNodeAop begine for nodeId: {} projectId: {} name: {}", id,
                projectId, name);

        Optional<NodeDTO> existingNodeDTO = nodeCustomService.findByProjectIdAndName(projectId,
                name);
        long unixTime = UapolloUtil.getUnixTime();
        if (existingNodeDTO.isPresent()) {

            if (id != null)
                operation = Operation.UPDATE;

            log.info(
                    "@Around saveNodeAop the node projectId: {} name: {} has existed and can't {}",
                    projectId, name, operation);
            throw new BadRequestAlertException("The duplicate node can't " + operation, ENTITY_NAME,
                    ResponseErrorKey.DUPLICATE_ENTITY.toString());
        } else {

            nodeDTO.setModifyTime(unixTime);
            if (id == null) {
                nodeDTO.setCreateTime(unixTime);
            } else {

                NodeDTO dbNodeDTO = nodeService.findOne(id);
                operation = Operation.UPDATE;
                if (dbNodeDTO == null) {
                    log.info(
                            "@Around saveNodeAop the nodId: {} projectId: {} name: {} don't exist and can't {}",
                            id, projectId, name, operation);
                    throw new BadRequestAlertException("The none-existent node can't " + operation,
                            ENTITY_NAME, ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
                } else {
                    nodeDTO.setCreateTime(dbNodeDTO.getCreateTime());
                    logNode(dbNodeDTO, operation);
                }
            }
        }

        log.info("@Around saveNodeAop nodeDTO: {}", nodeDTO);
        Object object = pJoinPoint.proceed();

        if (operation.equals(Operation.CREATE)) {
            logNode((NodeDTO) object, operation);
        }

        return object;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.NodeServiceImpl.delete(java.lang.Long)) "
            + "&& args(id)")
    public void deleteNodeAop(Long id) {
    };

    @Around("deleteNodeAop(id)")
    public void deleteNodeAround(ProceedingJoinPoint pJoinPoint, Long id) throws Throwable {

        Operation operation = Operation.DELTEE;
        log.info("@Around deleteNodeAop begine for nodeId: {}", id);
        NodeDTO dbNodeDTO = nodeService.findOne(id);
        if (dbNodeDTO == null) {
            log.info("@Around deleteNodeAop the nodeId: {} don't exist and can't {}", id,
                    operation);
            throw new BadRequestAlertException("The none-existent node can't " + operation,
                    ENTITY_NAME, ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
        }

        logNode(dbNodeDTO, operation);
        log.info("@Around deleteNodeAop nodeDTO: {}", dbNodeDTO);
        pJoinPoint.proceed();
    }

    private final void logNode(NodeDTO nodeDTO, Operation operation) {

        Long id = nodeDTO.getId();
        log.info("logNode for id: {} operation: {}", id, operation);
        NodeHistoryDTO nodeHistoryDTO = new NodeHistoryDTO();
        nodeHistoryDTO.setId(null);
        nodeHistoryDTO.setProjectId(nodeDTO.getProjectId());
        nodeHistoryDTO.setNodeId(id);
        nodeHistoryDTO.setName(nodeDTO.getName());
        nodeHistoryDTO.setOperation(operation);
        nodeHistoryDTO.setCreateTime(UapolloUtil.getUnixTime());
        nodeHistoryService.save(nodeHistoryDTO);
    }
}

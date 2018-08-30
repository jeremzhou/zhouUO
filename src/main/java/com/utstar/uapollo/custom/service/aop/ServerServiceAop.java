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

import com.utstar.uapollo.custom.service.ServerCustomService;
import com.utstar.uapollo.custom.service.enumeration.ResponseErrorKey;
import com.utstar.uapollo.custom.util.UapolloUtil;
import com.utstar.uapollo.domain.enumeration.Operation;
import com.utstar.uapollo.service.ServerHistoryService;
import com.utstar.uapollo.service.ServerService;
import com.utstar.uapollo.service.dto.ServerDTO;
import com.utstar.uapollo.service.dto.ServerHistoryDTO;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;

/**
 * @author UTSC0167
 * @date 2018年4月24日
 *
 */
@Aspect
@Component
public class ServerServiceAop {

    private final Logger log = LoggerFactory.getLogger(ServerServiceAop.class);

    private static final String ENTITY_NAME = "server";

    private final ServerService serverService;

    private final ServerCustomService serverCustomService;

    private final ServerHistoryService serverHistoryService;

    public ServerServiceAop(ServerService serverService, ServerCustomService serverCustomService,
            ServerHistoryService serverHistoryService) {
        this.serverService = serverService;
        this.serverCustomService = serverCustomService;
        this.serverHistoryService = serverHistoryService;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.ServerServiceImpl.save(com.utstar.uapollo.service.dto.ServerDTO)) "
            + "&& args(serverDTO)")
    public void saveServerAop(ServerDTO serverDTO) {
    };

    @Around("saveServerAop(serverDTO)")
    public Object saveServerAround(ProceedingJoinPoint pJoinPoint, ServerDTO serverDTO)
            throws Throwable {

        Operation operation = Operation.CREATE;
        Long id = serverDTO.getId();
        Long nodeId = serverDTO.getNodeId();
        String ip = serverDTO.getIp();
        log.info("@Around saveServerAop begine for serverId: {} nodeId: {} ip: {}", id, nodeId,
                ip);

        Optional<ServerDTO> existingServerDTO = serverCustomService.findByNodeIdAndIp(nodeId, ip);
        long unixTime = UapolloUtil.getUnixTime();
        if (existingServerDTO.isPresent()) {

            if (id != null)
                operation = Operation.UPDATE;

            log.info(
                    "@Around saveServerAop the server nodeId: {} ip: {} has existed and can't {}",
                    nodeId, ip, operation);
            throw new BadRequestAlertException("The duplicate server can't " + operation,
                    ENTITY_NAME, ResponseErrorKey.DUPLICATE_ENTITY.toString());
        } else {

            serverDTO.setModifyTime(unixTime);
            if (id == null) {
                serverDTO.setCreateTime(unixTime);
            } else {

                ServerDTO dbServerDTO = serverService.findOne(id);
                operation = Operation.UPDATE;
                if (dbServerDTO == null) {
                    log.info(
                            "@Around saveServerAop the nodId: {} nodeId: {} ip: {} don't exist and can't {}",
                            id, nodeId, ip, operation);
                    throw new BadRequestAlertException(
                            "The none-existent server can't " + operation, ENTITY_NAME,
                            ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
                } else {
                    serverDTO.setCreateTime(dbServerDTO.getCreateTime());
                    logServer(dbServerDTO, operation);
                }
            }
        }

        log.info("@Around saveServerAop serverDTO: {}", serverDTO);
        Object object = pJoinPoint.proceed();

        if (operation.equals(Operation.CREATE)) {
            logServer((ServerDTO) object, operation);
        }

        return object;
    }

    @Pointcut("execution(* com.utstar.uapollo.service.impl.ServerServiceImpl.delete(java.lang.Long)) "
            + "&& args(id)")
    public void deleteServerAop(Long id) {
    };

    @Around("deleteServerAop(id)")
    public void deleteServerAround(ProceedingJoinPoint pJoinPoint, Long id) throws Throwable {

        Operation operation = Operation.DELTEE;
        log.info("@Around deleteServerAop begine for serverId: {}", id);
        ServerDTO dbServerDTO = serverService.findOne(id);
        if (dbServerDTO == null) {
            log.info("@Around deleteServerAop the serverId: {} don't exist and can't {}", id,
                    operation);
            throw new BadRequestAlertException("The none-existent server can't " + operation,
                    ENTITY_NAME, ResponseErrorKey.NON_EXISTENT_ENTITY.toString());
        }

        logServer(dbServerDTO, operation);
        log.info("@Around deleteServerAop serverDTO: {}", dbServerDTO);
        pJoinPoint.proceed();
    }

    private final void logServer(ServerDTO serverDTO, Operation operation) {

        Long id = serverDTO.getId();
        log.info("logServer for id: {} operation: {}", id, operation);
        ServerHistoryDTO serverHistoryDTO = new ServerHistoryDTO();
        serverHistoryDTO.setId(null);
        serverHistoryDTO.setNodeId(serverDTO.getNodeId());
        serverHistoryDTO.setServerId(id);
        serverHistoryDTO.setIp(serverDTO.getIp());
        serverHistoryDTO.setOperation(operation);
        serverHistoryDTO.setCreateTime(UapolloUtil.getUnixTime());
        serverHistoryService.save(serverHistoryDTO);
    }
}

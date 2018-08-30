package com.utstar.uapollo.custom.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.custom.service.ServerCustomService;
import com.utstar.uapollo.custom.view.ServerVO;
import com.utstar.uapollo.service.dto.ServerDTO;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * @author UTSC0167
 * @date 2018年4月18日
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ServerController {

    private final Logger log = LoggerFactory.getLogger(ServerController.class);

    private final ServerCustomService serverCustomService;

    public ServerController(ServerCustomService serverCustomService) {
        this.serverCustomService = serverCustomService;
    }

    @GetMapping("/projects/{projectId}/servers")
    @Timed
    public List<ServerDTO> getServersByProjectId(@PathVariable Long projectId) {

        log.debug("REST request to get all Servers by projectId: {}", projectId);
        return serverCustomService.findByProjectId(projectId);
    }

    @GetMapping("/application-metas/{applicationMetaId}/nodes/{nodeId}/servers")
    @Timed
    public List<ServerDTO> getNodesByApplicationMetaIdAndNodeId(
            @PathVariable Long applicationMetaId, @PathVariable Long nodeId) {

        log.debug("REST request to get all Node by applicationMetaId: {} and nodeId",
                applicationMetaId, nodeId);
        return serverCustomService.findByApplicationMetaIdAndNodeId(applicationMetaId, nodeId);
    }

    @GetMapping("/servers")
    @Timed
    public ResponseEntity<ServerDTO> getServerByNodeIdAndIp(@RequestParam Long nodeId,
            @RequestParam String ip) {

        log.debug("REST request to get the Server by nodeId: {} and ip: {}", nodeId, ip);
        return ResponseUtil.wrapOrNotFound(serverCustomService.findByNodeIdAndIp(nodeId, ip));
    }
    
    @GetMapping("/infomation/servers")
    @Timed
    public List<ServerVO> getServerInfomation(){
    	
    	log.debug("REST request to get the Server infomation");
    	return serverCustomService.finaAllAboutServerInfomation();
    }
}

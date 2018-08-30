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
import com.utstar.uapollo.custom.service.NodeCustomService;
import com.utstar.uapollo.custom.view.NodeVO;
import com.utstar.uapollo.service.dto.NodeDTO;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * @author UTSC0167
 * @date 2018年4月18日
 *
 */
@RestController
@RequestMapping("/api/v1")
public class NodeController {

    private final Logger log = LoggerFactory.getLogger(NodeController.class);

    private final NodeCustomService nodeCustomService;

    public NodeController(NodeCustomService nodeCustomService) {
        this.nodeCustomService = nodeCustomService;
    }

    @GetMapping("/projects/{projectId}/nodes")
    @Timed
    public List<NodeDTO> getNodesByProjectId(@PathVariable Long projectId) {

        log.debug("REST request to get all Nodes by projectId: {}", projectId);
        return nodeCustomService.findByProjectId(projectId);
    }

    @GetMapping("/application-metas/{applicationMetaId}/nodes")
    @Timed
    public List<NodeDTO> getNodesByApplicationMetaId(@PathVariable Long applicationMetaId) {

        log.debug("REST request to get all Node by applicationMetaId: {}", applicationMetaId);
        return nodeCustomService.findByApplicationMetaId(applicationMetaId);
    }

    @GetMapping("/nodes")
    @Timed
    public ResponseEntity<NodeDTO> getNodeByProjectIdAndName(@RequestParam Long projectId,
            @RequestParam String name) {

        log.debug("REST request to get the Node by projectId: {} and name: {}", projectId, name);
        return ResponseUtil
                .wrapOrNotFound(nodeCustomService.findByProjectIdAndName(projectId, name));
    }
    
    @GetMapping("/infomation/nodes")
    @Timed
    public List<NodeVO> getAllNodeInfomation(){
    	
    	log.debug("REST request to get all Nodes infomation");
    	return nodeCustomService.findAllInfomation();
    }
    
    
}

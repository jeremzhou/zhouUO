package com.utstar.uapollo.custom.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.custom.dto.ReleasingResult;
import com.utstar.uapollo.custom.service.NodeConfigCustomService;
import com.utstar.uapollo.custom.service.ReleaseConfigService;
import com.utstar.uapollo.service.dto.NodeConfigDTO;

/**
 * @author UTSC0167
 * @date 2018年4月18日
 *
 */
@RestController
@RequestMapping("/api/v1")
public class NodeConfigController {

    private final Logger log = LoggerFactory.getLogger(NodeConfigController.class);

    private final NodeConfigCustomService nodeConfigCustomService;

    private final ReleaseConfigService releaseConfigService;

    public NodeConfigController(NodeConfigCustomService nodeConfigCustomService,
            ReleaseConfigService releaseConfigService) {
        this.nodeConfigCustomService = nodeConfigCustomService;
        this.releaseConfigService = releaseConfigService;
    }

    @PostMapping("/application-metas/{applicationMetaId}/nodes/{nodeId}/node-configs/release")
    @Timed
    public List<ReleasingResult> releaseNodeConfig(@PathVariable Long applicationMetaId,
            @PathVariable Long nodeId) {

        log.debug("REST request to release NodeConfig for applicationMetaId: {} and nodeId: {}",
                applicationMetaId, nodeId);
        return releaseConfigService.releaseNodeConfig(applicationMetaId, nodeId);
    }

    @GetMapping("/application-metas/{applicationMetaId}/nodes/{nodeId}/node-configs")
    @Timed
    public List<NodeConfigDTO> getNodeConfigsByApplicationMetaIdAndNodeId(
            @PathVariable Long applicationMetaId, @PathVariable Long nodeId) {

        log.debug("REST request to get all NodeConfigs by applicationMetaId: {} and nodeId: {}",
                applicationMetaId, nodeId);
        return nodeConfigCustomService.findByApplicationMetaIdAndNodeId(applicationMetaId, nodeId);
    }
}

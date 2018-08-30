package com.utstar.uapollo.custom.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.custom.service.PrivateConfigCustomService;
import com.utstar.uapollo.service.dto.PrivateConfigDTO;

/**
 * @author UTSC0167
 * @date 2018年4月18日
 *
 */
@RestController
@RequestMapping("/api/v1")
public class PrivateConfigController {

    private final Logger log = LoggerFactory.getLogger(PrivateConfigController.class);

    private final PrivateConfigCustomService privateConfigCustomService;

    public PrivateConfigController(PrivateConfigCustomService privateConfigCustomService) {
        this.privateConfigCustomService = privateConfigCustomService;
    }

    @GetMapping("/applications/{applicationId}/private-configs")
    @Timed
    public List<PrivateConfigDTO> getPrivateConfigsByApplicationId(
            @PathVariable Long applicationId) {

        log.debug("REST request to get all PrivateConfigs by applicationId: {}", applicationId);
        return privateConfigCustomService.findByApplicationId(applicationId);
    }

}

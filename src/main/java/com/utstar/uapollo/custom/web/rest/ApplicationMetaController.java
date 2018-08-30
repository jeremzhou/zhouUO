package com.utstar.uapollo.custom.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.custom.config.UapolloCustomConfiguration;
import com.utstar.uapollo.custom.dto.ApplicationMetaReloadResult;
import com.utstar.uapollo.custom.service.ApplicationMetaCustomService;
import com.utstar.uapollo.service.dto.ApplicationMetaDTO;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * @author UTSC0167
 * @date 2018年4月16日
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ApplicationMetaController {

    private final Logger log = LoggerFactory.getLogger(ApplicationMetaController.class);

    private final UapolloCustomConfiguration uapolloCustomConfiguration;

    private final ApplicationMetaCustomService applicationMetaCustomService;

    public ApplicationMetaController(ApplicationMetaCustomService applicationMetaCustomService,
            UapolloCustomConfiguration uapolloCustomConfiguration) {
        this.applicationMetaCustomService = applicationMetaCustomService;
        this.uapolloCustomConfiguration = uapolloCustomConfiguration;
    }

    @PutMapping("/application-metas/reload")
    @Timed
    public ResponseEntity<List<ApplicationMetaReloadResult>> reloadApplicationMetas(
            @RequestParam(required = false) String templateConfigurationLocation) {

        if (templateConfigurationLocation == null) {
            templateConfigurationLocation = uapolloCustomConfiguration
                    .getTemplateConfigurationLocation();
        }
        log.debug(
                "REST request to reload all ApplicationMetas use templateConfigurationLocation: {}",
                templateConfigurationLocation);
        return ResponseUtil
                .wrapOrNotFound(applicationMetaCustomService.reload(templateConfigurationLocation));
    }

    @GetMapping("/projects/{projectId}/application-metas")
    @Timed
    public List<ApplicationMetaDTO> getApplicationMetasByProjectId(@PathVariable Long projectId) {

        log.debug("REST request to get all ApplicationMetas by projectId: {}", projectId);
        return applicationMetaCustomService.findByProjectId(projectId);
    }

    @GetMapping("/application-metas")
    @Timed
    public ResponseEntity<ApplicationMetaDTO> getApplicationMetaByProjectIdAndName(
            @RequestParam Long projectId, @RequestParam String name) {

        log.debug("REST request to get the ApplicationMeta by projectId: {} and name: {}",
                projectId, name);
        return ResponseUtil.wrapOrNotFound(
                applicationMetaCustomService.findByProjectIdAndName(projectId, name));
    }
}

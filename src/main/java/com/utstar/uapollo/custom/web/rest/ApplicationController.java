package com.utstar.uapollo.custom.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.custom.dto.ReleasingResult;
import com.utstar.uapollo.custom.service.ApplicationCustomService;
import com.utstar.uapollo.custom.service.ReleaseConfigService;
import com.utstar.uapollo.custom.view.ApplicationVO;
import com.utstar.uapollo.service.dto.ApplicationDTO;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * @author UTSC0167
 * @date 2018年4月18日
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ApplicationController {

    private final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    private final ApplicationCustomService applicationCustomService;

    private final ReleaseConfigService releaseConfigService;

    public ApplicationController(ApplicationCustomService applicationCustomService,
            ReleaseConfigService releaseConfigService) {
        this.applicationCustomService = applicationCustomService;
        this.releaseConfigService = releaseConfigService;
    }

    @PostMapping("/applications/{id}/release")
    @Timed
    public List<ReleasingResult> releaseApplicationConfig(@PathVariable Long id) {

        log.debug("REST request to release Application config for applicationId: {}", id);
        return releaseConfigService.releaseApplicationConfig(id);
    }

    @GetMapping("/projects/{projectId}/applications")
    @Timed
    public List<ApplicationDTO> getApplicationsByProjectId(@PathVariable Long projectId) {

        log.debug("REST request to get all Applications by projectId: {}", projectId);
        return applicationCustomService.findByProjectId(projectId);
    }

    @GetMapping("/application-metas/{applicationMetaId}/servers/{serverId}/applications")
    @Timed
    public ResponseEntity<ApplicationDTO> getApplicationsByApplicationMetaIdAndServerId(
            @PathVariable Long applicationMetaId, @PathVariable Long serverId) {

        log.debug("REST request to get all Applications by applicationMetaId: {} and serverId: {}",
                applicationMetaId, serverId);
        return ResponseUtil.wrapOrNotFound(applicationCustomService
                .findByApplicationMetaIdAndServerId(applicationMetaId, serverId));
    }
    
    @GetMapping("/infomation/applications")
    @Timed
    public List<ApplicationVO> getApplicationInformation(){
    	
    	log.debug("REST request to get all about Applications infomation");
    	return applicationCustomService.findAllAboutApplicationInfomation();
    }
}

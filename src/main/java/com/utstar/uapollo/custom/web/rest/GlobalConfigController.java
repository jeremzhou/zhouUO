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
import com.utstar.uapollo.custom.service.GlobalConfigCustomService;
import com.utstar.uapollo.custom.service.ReleaseConfigService;
import com.utstar.uapollo.service.dto.GlobalConfigDTO;

/**
 * @author UTSC0167
 * @date 2018年4月18日
 *
 */
@RestController
@RequestMapping("/api/v1")
public class GlobalConfigController {

    private final Logger log = LoggerFactory.getLogger(GlobalConfigController.class);

    private final GlobalConfigCustomService globalConfigCustomService;

    private final ReleaseConfigService releaseConfigService;

    public GlobalConfigController(GlobalConfigCustomService globalConfigCustomService,
            ReleaseConfigService releaseConfigService) {
        this.globalConfigCustomService = globalConfigCustomService;
        this.releaseConfigService = releaseConfigService;
    }

    @PostMapping("/application-metas/{applicationMetaId}/global-configs/release")
    @Timed
    public List<ReleasingResult> releaseGlobalConfig(@PathVariable Long applicationMetaId) {

        log.debug("REST request to release GlobalConfig for applicationMetaId: {}",
                applicationMetaId);
        return releaseConfigService.releaseGlobalConfig(applicationMetaId);
    }

    @GetMapping("/application-metas/{applicationMetaId}/global-configs")
    @Timed
    public List<GlobalConfigDTO> getGlobalConfigsByApplicationMetaId(
            @PathVariable Long applicationMetaId) {

        log.debug("REST request to get all GlobalConfigs by applicationMetaId: {}",
                applicationMetaId);
        return globalConfigCustomService.findByApplicationMetaId(applicationMetaId);
    }
}

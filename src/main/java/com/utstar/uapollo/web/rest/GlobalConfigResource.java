package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.GlobalConfigService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.GlobalConfigDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing GlobalConfig.
 */
@RestController
@RequestMapping("/api")
public class GlobalConfigResource {

    private final Logger log = LoggerFactory.getLogger(GlobalConfigResource.class);

    private static final String ENTITY_NAME = "globalConfig";

    private final GlobalConfigService globalConfigService;

    public GlobalConfigResource(GlobalConfigService globalConfigService) {
        this.globalConfigService = globalConfigService;
    }

    /**
     * POST  /global-configs : Create a new globalConfig.
     *
     * @param globalConfigDTO the globalConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new globalConfigDTO, or with status 400 (Bad Request) if the globalConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/global-configs")
    @Timed
    public ResponseEntity<GlobalConfigDTO> createGlobalConfig(@Valid @RequestBody GlobalConfigDTO globalConfigDTO) throws URISyntaxException {
        log.debug("REST request to save GlobalConfig : {}", globalConfigDTO);
        if (globalConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new globalConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GlobalConfigDTO result = globalConfigService.save(globalConfigDTO);
        return ResponseEntity.created(new URI("/api/global-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /global-configs : Updates an existing globalConfig.
     *
     * @param globalConfigDTO the globalConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated globalConfigDTO,
     * or with status 400 (Bad Request) if the globalConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the globalConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/global-configs")
    @Timed
    public ResponseEntity<GlobalConfigDTO> updateGlobalConfig(@Valid @RequestBody GlobalConfigDTO globalConfigDTO) throws URISyntaxException {
        log.debug("REST request to update GlobalConfig : {}", globalConfigDTO);
        if (globalConfigDTO.getId() == null) {
            return createGlobalConfig(globalConfigDTO);
        }
        GlobalConfigDTO result = globalConfigService.save(globalConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, globalConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /global-configs : get all the globalConfigs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of globalConfigs in body
     */
    @GetMapping("/global-configs")
    @Timed
    public List<GlobalConfigDTO> getAllGlobalConfigs() {
        log.debug("REST request to get all GlobalConfigs");
        return globalConfigService.findAll();
        }

    /**
     * GET  /global-configs/:id : get the "id" globalConfig.
     *
     * @param id the id of the globalConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the globalConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/global-configs/{id}")
    @Timed
    public ResponseEntity<GlobalConfigDTO> getGlobalConfig(@PathVariable Long id) {
        log.debug("REST request to get GlobalConfig : {}", id);
        GlobalConfigDTO globalConfigDTO = globalConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(globalConfigDTO));
    }

    /**
     * DELETE  /global-configs/:id : delete the "id" globalConfig.
     *
     * @param id the id of the globalConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/global-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteGlobalConfig(@PathVariable Long id) {
        log.debug("REST request to delete GlobalConfig : {}", id);
        globalConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

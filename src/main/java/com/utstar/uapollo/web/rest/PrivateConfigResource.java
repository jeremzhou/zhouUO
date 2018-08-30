package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.PrivateConfigService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.PrivateConfigDTO;
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
 * REST controller for managing PrivateConfig.
 */
@RestController
@RequestMapping("/api")
public class PrivateConfigResource {

    private final Logger log = LoggerFactory.getLogger(PrivateConfigResource.class);

    private static final String ENTITY_NAME = "privateConfig";

    private final PrivateConfigService privateConfigService;

    public PrivateConfigResource(PrivateConfigService privateConfigService) {
        this.privateConfigService = privateConfigService;
    }

    /**
     * POST  /private-configs : Create a new privateConfig.
     *
     * @param privateConfigDTO the privateConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new privateConfigDTO, or with status 400 (Bad Request) if the privateConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/private-configs")
    @Timed
    public ResponseEntity<PrivateConfigDTO> createPrivateConfig(@Valid @RequestBody PrivateConfigDTO privateConfigDTO) throws URISyntaxException {
        log.debug("REST request to save PrivateConfig : {}", privateConfigDTO);
        if (privateConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new privateConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrivateConfigDTO result = privateConfigService.save(privateConfigDTO);
        return ResponseEntity.created(new URI("/api/private-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /private-configs : Updates an existing privateConfig.
     *
     * @param privateConfigDTO the privateConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated privateConfigDTO,
     * or with status 400 (Bad Request) if the privateConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the privateConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/private-configs")
    @Timed
    public ResponseEntity<PrivateConfigDTO> updatePrivateConfig(@Valid @RequestBody PrivateConfigDTO privateConfigDTO) throws URISyntaxException {
        log.debug("REST request to update PrivateConfig : {}", privateConfigDTO);
        if (privateConfigDTO.getId() == null) {
            return createPrivateConfig(privateConfigDTO);
        }
        PrivateConfigDTO result = privateConfigService.save(privateConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, privateConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /private-configs : get all the privateConfigs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of privateConfigs in body
     */
    @GetMapping("/private-configs")
    @Timed
    public List<PrivateConfigDTO> getAllPrivateConfigs() {
        log.debug("REST request to get all PrivateConfigs");
        return privateConfigService.findAll();
        }

    /**
     * GET  /private-configs/:id : get the "id" privateConfig.
     *
     * @param id the id of the privateConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the privateConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/private-configs/{id}")
    @Timed
    public ResponseEntity<PrivateConfigDTO> getPrivateConfig(@PathVariable Long id) {
        log.debug("REST request to get PrivateConfig : {}", id);
        PrivateConfigDTO privateConfigDTO = privateConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(privateConfigDTO));
    }

    /**
     * DELETE  /private-configs/:id : delete the "id" privateConfig.
     *
     * @param id the id of the privateConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/private-configs/{id}")
    @Timed
    public ResponseEntity<Void> deletePrivateConfig(@PathVariable Long id) {
        log.debug("REST request to delete PrivateConfig : {}", id);
        privateConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

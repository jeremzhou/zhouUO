package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.ApplicationConfigService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.ApplicationConfigDTO;
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
 * REST controller for managing ApplicationConfig.
 */
@RestController
@RequestMapping("/api")
public class ApplicationConfigResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationConfigResource.class);

    private static final String ENTITY_NAME = "applicationConfig";

    private final ApplicationConfigService applicationConfigService;

    public ApplicationConfigResource(ApplicationConfigService applicationConfigService) {
        this.applicationConfigService = applicationConfigService;
    }

    /**
     * POST  /application-configs : Create a new applicationConfig.
     *
     * @param applicationConfigDTO the applicationConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new applicationConfigDTO, or with status 400 (Bad Request) if the applicationConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/application-configs")
    @Timed
    public ResponseEntity<ApplicationConfigDTO> createApplicationConfig(@Valid @RequestBody ApplicationConfigDTO applicationConfigDTO) throws URISyntaxException {
        log.debug("REST request to save ApplicationConfig : {}", applicationConfigDTO);
        if (applicationConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationConfigDTO result = applicationConfigService.save(applicationConfigDTO);
        return ResponseEntity.created(new URI("/api/application-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /application-configs : Updates an existing applicationConfig.
     *
     * @param applicationConfigDTO the applicationConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated applicationConfigDTO,
     * or with status 400 (Bad Request) if the applicationConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the applicationConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/application-configs")
    @Timed
    public ResponseEntity<ApplicationConfigDTO> updateApplicationConfig(@Valid @RequestBody ApplicationConfigDTO applicationConfigDTO) throws URISyntaxException {
        log.debug("REST request to update ApplicationConfig : {}", applicationConfigDTO);
        if (applicationConfigDTO.getId() == null) {
            return createApplicationConfig(applicationConfigDTO);
        }
        ApplicationConfigDTO result = applicationConfigService.save(applicationConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, applicationConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /application-configs : get all the applicationConfigs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of applicationConfigs in body
     */
    @GetMapping("/application-configs")
    @Timed
    public List<ApplicationConfigDTO> getAllApplicationConfigs() {
        log.debug("REST request to get all ApplicationConfigs");
        return applicationConfigService.findAll();
        }

    /**
     * GET  /application-configs/:id : get the "id" applicationConfig.
     *
     * @param id the id of the applicationConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the applicationConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/application-configs/{id}")
    @Timed
    public ResponseEntity<ApplicationConfigDTO> getApplicationConfig(@PathVariable Long id) {
        log.debug("REST request to get ApplicationConfig : {}", id);
        ApplicationConfigDTO applicationConfigDTO = applicationConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(applicationConfigDTO));
    }

    /**
     * DELETE  /application-configs/:id : delete the "id" applicationConfig.
     *
     * @param id the id of the applicationConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/application-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplicationConfig(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationConfig : {}", id);
        applicationConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

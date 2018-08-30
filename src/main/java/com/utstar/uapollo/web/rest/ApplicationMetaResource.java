package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.ApplicationMetaService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.ApplicationMetaDTO;
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
 * REST controller for managing ApplicationMeta.
 */
@RestController
@RequestMapping("/api")
public class ApplicationMetaResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationMetaResource.class);

    private static final String ENTITY_NAME = "applicationMeta";

    private final ApplicationMetaService applicationMetaService;

    public ApplicationMetaResource(ApplicationMetaService applicationMetaService) {
        this.applicationMetaService = applicationMetaService;
    }

    /**
     * POST  /application-metas : Create a new applicationMeta.
     *
     * @param applicationMetaDTO the applicationMetaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new applicationMetaDTO, or with status 400 (Bad Request) if the applicationMeta has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/application-metas")
    @Timed
    public ResponseEntity<ApplicationMetaDTO> createApplicationMeta(@Valid @RequestBody ApplicationMetaDTO applicationMetaDTO) throws URISyntaxException {
        log.debug("REST request to save ApplicationMeta : {}", applicationMetaDTO);
        if (applicationMetaDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationMeta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationMetaDTO result = applicationMetaService.save(applicationMetaDTO);
        return ResponseEntity.created(new URI("/api/application-metas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /application-metas : Updates an existing applicationMeta.
     *
     * @param applicationMetaDTO the applicationMetaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated applicationMetaDTO,
     * or with status 400 (Bad Request) if the applicationMetaDTO is not valid,
     * or with status 500 (Internal Server Error) if the applicationMetaDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/application-metas")
    @Timed
    public ResponseEntity<ApplicationMetaDTO> updateApplicationMeta(@Valid @RequestBody ApplicationMetaDTO applicationMetaDTO) throws URISyntaxException {
        log.debug("REST request to update ApplicationMeta : {}", applicationMetaDTO);
        if (applicationMetaDTO.getId() == null) {
            return createApplicationMeta(applicationMetaDTO);
        }
        ApplicationMetaDTO result = applicationMetaService.save(applicationMetaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, applicationMetaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /application-metas : get all the applicationMetas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of applicationMetas in body
     */
    @GetMapping("/application-metas")
    @Timed
    public List<ApplicationMetaDTO> getAllApplicationMetas() {
        log.debug("REST request to get all ApplicationMetas");
        return applicationMetaService.findAll();
        }

    /**
     * GET  /application-metas/:id : get the "id" applicationMeta.
     *
     * @param id the id of the applicationMetaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the applicationMetaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/application-metas/{id}")
    @Timed
    public ResponseEntity<ApplicationMetaDTO> getApplicationMeta(@PathVariable Long id) {
        log.debug("REST request to get ApplicationMeta : {}", id);
        ApplicationMetaDTO applicationMetaDTO = applicationMetaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(applicationMetaDTO));
    }

    /**
     * DELETE  /application-metas/:id : delete the "id" applicationMeta.
     *
     * @param id the id of the applicationMetaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/application-metas/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplicationMeta(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationMeta : {}", id);
        applicationMetaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

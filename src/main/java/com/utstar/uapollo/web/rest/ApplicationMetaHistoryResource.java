package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.ApplicationMetaHistoryService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.ApplicationMetaHistoryDTO;
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
 * REST controller for managing ApplicationMetaHistory.
 */
@RestController
@RequestMapping("/api")
public class ApplicationMetaHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationMetaHistoryResource.class);

    private static final String ENTITY_NAME = "applicationMetaHistory";

    private final ApplicationMetaHistoryService applicationMetaHistoryService;

    public ApplicationMetaHistoryResource(ApplicationMetaHistoryService applicationMetaHistoryService) {
        this.applicationMetaHistoryService = applicationMetaHistoryService;
    }

    /**
     * POST  /application-meta-histories : Create a new applicationMetaHistory.
     *
     * @param applicationMetaHistoryDTO the applicationMetaHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new applicationMetaHistoryDTO, or with status 400 (Bad Request) if the applicationMetaHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/application-meta-histories")
    @Timed
    public ResponseEntity<ApplicationMetaHistoryDTO> createApplicationMetaHistory(@Valid @RequestBody ApplicationMetaHistoryDTO applicationMetaHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save ApplicationMetaHistory : {}", applicationMetaHistoryDTO);
        if (applicationMetaHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationMetaHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationMetaHistoryDTO result = applicationMetaHistoryService.save(applicationMetaHistoryDTO);
        return ResponseEntity.created(new URI("/api/application-meta-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /application-meta-histories : Updates an existing applicationMetaHistory.
     *
     * @param applicationMetaHistoryDTO the applicationMetaHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated applicationMetaHistoryDTO,
     * or with status 400 (Bad Request) if the applicationMetaHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the applicationMetaHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/application-meta-histories")
    @Timed
    public ResponseEntity<ApplicationMetaHistoryDTO> updateApplicationMetaHistory(@Valid @RequestBody ApplicationMetaHistoryDTO applicationMetaHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update ApplicationMetaHistory : {}", applicationMetaHistoryDTO);
        if (applicationMetaHistoryDTO.getId() == null) {
            return createApplicationMetaHistory(applicationMetaHistoryDTO);
        }
        ApplicationMetaHistoryDTO result = applicationMetaHistoryService.save(applicationMetaHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, applicationMetaHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /application-meta-histories : get all the applicationMetaHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of applicationMetaHistories in body
     */
    @GetMapping("/application-meta-histories")
    @Timed
    public List<ApplicationMetaHistoryDTO> getAllApplicationMetaHistories() {
        log.debug("REST request to get all ApplicationMetaHistories");
        return applicationMetaHistoryService.findAll();
        }

    /**
     * GET  /application-meta-histories/:id : get the "id" applicationMetaHistory.
     *
     * @param id the id of the applicationMetaHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the applicationMetaHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/application-meta-histories/{id}")
    @Timed
    public ResponseEntity<ApplicationMetaHistoryDTO> getApplicationMetaHistory(@PathVariable Long id) {
        log.debug("REST request to get ApplicationMetaHistory : {}", id);
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(applicationMetaHistoryDTO));
    }

    /**
     * DELETE  /application-meta-histories/:id : delete the "id" applicationMetaHistory.
     *
     * @param id the id of the applicationMetaHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/application-meta-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplicationMetaHistory(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationMetaHistory : {}", id);
        applicationMetaHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

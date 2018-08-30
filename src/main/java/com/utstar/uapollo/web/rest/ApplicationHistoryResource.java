package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.ApplicationHistoryService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.ApplicationHistoryDTO;
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
 * REST controller for managing ApplicationHistory.
 */
@RestController
@RequestMapping("/api")
public class ApplicationHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationHistoryResource.class);

    private static final String ENTITY_NAME = "applicationHistory";

    private final ApplicationHistoryService applicationHistoryService;

    public ApplicationHistoryResource(ApplicationHistoryService applicationHistoryService) {
        this.applicationHistoryService = applicationHistoryService;
    }

    /**
     * POST  /application-histories : Create a new applicationHistory.
     *
     * @param applicationHistoryDTO the applicationHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new applicationHistoryDTO, or with status 400 (Bad Request) if the applicationHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/application-histories")
    @Timed
    public ResponseEntity<ApplicationHistoryDTO> createApplicationHistory(@Valid @RequestBody ApplicationHistoryDTO applicationHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save ApplicationHistory : {}", applicationHistoryDTO);
        if (applicationHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationHistoryDTO result = applicationHistoryService.save(applicationHistoryDTO);
        return ResponseEntity.created(new URI("/api/application-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /application-histories : Updates an existing applicationHistory.
     *
     * @param applicationHistoryDTO the applicationHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated applicationHistoryDTO,
     * or with status 400 (Bad Request) if the applicationHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the applicationHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/application-histories")
    @Timed
    public ResponseEntity<ApplicationHistoryDTO> updateApplicationHistory(@Valid @RequestBody ApplicationHistoryDTO applicationHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update ApplicationHistory : {}", applicationHistoryDTO);
        if (applicationHistoryDTO.getId() == null) {
            return createApplicationHistory(applicationHistoryDTO);
        }
        ApplicationHistoryDTO result = applicationHistoryService.save(applicationHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, applicationHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /application-histories : get all the applicationHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of applicationHistories in body
     */
    @GetMapping("/application-histories")
    @Timed
    public List<ApplicationHistoryDTO> getAllApplicationHistories() {
        log.debug("REST request to get all ApplicationHistories");
        return applicationHistoryService.findAll();
        }

    /**
     * GET  /application-histories/:id : get the "id" applicationHistory.
     *
     * @param id the id of the applicationHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the applicationHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/application-histories/{id}")
    @Timed
    public ResponseEntity<ApplicationHistoryDTO> getApplicationHistory(@PathVariable Long id) {
        log.debug("REST request to get ApplicationHistory : {}", id);
        ApplicationHistoryDTO applicationHistoryDTO = applicationHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(applicationHistoryDTO));
    }

    /**
     * DELETE  /application-histories/:id : delete the "id" applicationHistory.
     *
     * @param id the id of the applicationHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/application-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplicationHistory(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationHistory : {}", id);
        applicationHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.ApplicationConfigHistoryService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.ApplicationConfigHistoryDTO;
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
 * REST controller for managing ApplicationConfigHistory.
 */
@RestController
@RequestMapping("/api")
public class ApplicationConfigHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationConfigHistoryResource.class);

    private static final String ENTITY_NAME = "applicationConfigHistory";

    private final ApplicationConfigHistoryService applicationConfigHistoryService;

    public ApplicationConfigHistoryResource(ApplicationConfigHistoryService applicationConfigHistoryService) {
        this.applicationConfigHistoryService = applicationConfigHistoryService;
    }

    /**
     * POST  /application-config-histories : Create a new applicationConfigHistory.
     *
     * @param applicationConfigHistoryDTO the applicationConfigHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new applicationConfigHistoryDTO, or with status 400 (Bad Request) if the applicationConfigHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/application-config-histories")
    @Timed
    public ResponseEntity<ApplicationConfigHistoryDTO> createApplicationConfigHistory(@Valid @RequestBody ApplicationConfigHistoryDTO applicationConfigHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save ApplicationConfigHistory : {}", applicationConfigHistoryDTO);
        if (applicationConfigHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new applicationConfigHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ApplicationConfigHistoryDTO result = applicationConfigHistoryService.save(applicationConfigHistoryDTO);
        return ResponseEntity.created(new URI("/api/application-config-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /application-config-histories : Updates an existing applicationConfigHistory.
     *
     * @param applicationConfigHistoryDTO the applicationConfigHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated applicationConfigHistoryDTO,
     * or with status 400 (Bad Request) if the applicationConfigHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the applicationConfigHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/application-config-histories")
    @Timed
    public ResponseEntity<ApplicationConfigHistoryDTO> updateApplicationConfigHistory(@Valid @RequestBody ApplicationConfigHistoryDTO applicationConfigHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update ApplicationConfigHistory : {}", applicationConfigHistoryDTO);
        if (applicationConfigHistoryDTO.getId() == null) {
            return createApplicationConfigHistory(applicationConfigHistoryDTO);
        }
        ApplicationConfigHistoryDTO result = applicationConfigHistoryService.save(applicationConfigHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, applicationConfigHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /application-config-histories : get all the applicationConfigHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of applicationConfigHistories in body
     */
    @GetMapping("/application-config-histories")
    @Timed
    public List<ApplicationConfigHistoryDTO> getAllApplicationConfigHistories() {
        log.debug("REST request to get all ApplicationConfigHistories");
        return applicationConfigHistoryService.findAll();
        }

    /**
     * GET  /application-config-histories/:id : get the "id" applicationConfigHistory.
     *
     * @param id the id of the applicationConfigHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the applicationConfigHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/application-config-histories/{id}")
    @Timed
    public ResponseEntity<ApplicationConfigHistoryDTO> getApplicationConfigHistory(@PathVariable Long id) {
        log.debug("REST request to get ApplicationConfigHistory : {}", id);
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = applicationConfigHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(applicationConfigHistoryDTO));
    }

    /**
     * DELETE  /application-config-histories/:id : delete the "id" applicationConfigHistory.
     *
     * @param id the id of the applicationConfigHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/application-config-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteApplicationConfigHistory(@PathVariable Long id) {
        log.debug("REST request to delete ApplicationConfigHistory : {}", id);
        applicationConfigHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

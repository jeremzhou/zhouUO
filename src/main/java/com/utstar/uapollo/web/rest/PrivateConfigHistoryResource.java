package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.PrivateConfigHistoryService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.PrivateConfigHistoryDTO;
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
 * REST controller for managing PrivateConfigHistory.
 */
@RestController
@RequestMapping("/api")
public class PrivateConfigHistoryResource {

    private final Logger log = LoggerFactory.getLogger(PrivateConfigHistoryResource.class);

    private static final String ENTITY_NAME = "privateConfigHistory";

    private final PrivateConfigHistoryService privateConfigHistoryService;

    public PrivateConfigHistoryResource(PrivateConfigHistoryService privateConfigHistoryService) {
        this.privateConfigHistoryService = privateConfigHistoryService;
    }

    /**
     * POST  /private-config-histories : Create a new privateConfigHistory.
     *
     * @param privateConfigHistoryDTO the privateConfigHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new privateConfigHistoryDTO, or with status 400 (Bad Request) if the privateConfigHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/private-config-histories")
    @Timed
    public ResponseEntity<PrivateConfigHistoryDTO> createPrivateConfigHistory(@Valid @RequestBody PrivateConfigHistoryDTO privateConfigHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save PrivateConfigHistory : {}", privateConfigHistoryDTO);
        if (privateConfigHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new privateConfigHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrivateConfigHistoryDTO result = privateConfigHistoryService.save(privateConfigHistoryDTO);
        return ResponseEntity.created(new URI("/api/private-config-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /private-config-histories : Updates an existing privateConfigHistory.
     *
     * @param privateConfigHistoryDTO the privateConfigHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated privateConfigHistoryDTO,
     * or with status 400 (Bad Request) if the privateConfigHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the privateConfigHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/private-config-histories")
    @Timed
    public ResponseEntity<PrivateConfigHistoryDTO> updatePrivateConfigHistory(@Valid @RequestBody PrivateConfigHistoryDTO privateConfigHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update PrivateConfigHistory : {}", privateConfigHistoryDTO);
        if (privateConfigHistoryDTO.getId() == null) {
            return createPrivateConfigHistory(privateConfigHistoryDTO);
        }
        PrivateConfigHistoryDTO result = privateConfigHistoryService.save(privateConfigHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, privateConfigHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /private-config-histories : get all the privateConfigHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of privateConfigHistories in body
     */
    @GetMapping("/private-config-histories")
    @Timed
    public List<PrivateConfigHistoryDTO> getAllPrivateConfigHistories() {
        log.debug("REST request to get all PrivateConfigHistories");
        return privateConfigHistoryService.findAll();
        }

    /**
     * GET  /private-config-histories/:id : get the "id" privateConfigHistory.
     *
     * @param id the id of the privateConfigHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the privateConfigHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/private-config-histories/{id}")
    @Timed
    public ResponseEntity<PrivateConfigHistoryDTO> getPrivateConfigHistory(@PathVariable Long id) {
        log.debug("REST request to get PrivateConfigHistory : {}", id);
        PrivateConfigHistoryDTO privateConfigHistoryDTO = privateConfigHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(privateConfigHistoryDTO));
    }

    /**
     * DELETE  /private-config-histories/:id : delete the "id" privateConfigHistory.
     *
     * @param id the id of the privateConfigHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/private-config-histories/{id}")
    @Timed
    public ResponseEntity<Void> deletePrivateConfigHistory(@PathVariable Long id) {
        log.debug("REST request to delete PrivateConfigHistory : {}", id);
        privateConfigHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

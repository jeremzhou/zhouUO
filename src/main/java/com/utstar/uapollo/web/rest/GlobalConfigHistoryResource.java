package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.GlobalConfigHistoryService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.GlobalConfigHistoryDTO;
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
 * REST controller for managing GlobalConfigHistory.
 */
@RestController
@RequestMapping("/api")
public class GlobalConfigHistoryResource {

    private final Logger log = LoggerFactory.getLogger(GlobalConfigHistoryResource.class);

    private static final String ENTITY_NAME = "globalConfigHistory";

    private final GlobalConfigHistoryService globalConfigHistoryService;

    public GlobalConfigHistoryResource(GlobalConfigHistoryService globalConfigHistoryService) {
        this.globalConfigHistoryService = globalConfigHistoryService;
    }

    /**
     * POST  /global-config-histories : Create a new globalConfigHistory.
     *
     * @param globalConfigHistoryDTO the globalConfigHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new globalConfigHistoryDTO, or with status 400 (Bad Request) if the globalConfigHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/global-config-histories")
    @Timed
    public ResponseEntity<GlobalConfigHistoryDTO> createGlobalConfigHistory(@Valid @RequestBody GlobalConfigHistoryDTO globalConfigHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save GlobalConfigHistory : {}", globalConfigHistoryDTO);
        if (globalConfigHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new globalConfigHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GlobalConfigHistoryDTO result = globalConfigHistoryService.save(globalConfigHistoryDTO);
        return ResponseEntity.created(new URI("/api/global-config-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /global-config-histories : Updates an existing globalConfigHistory.
     *
     * @param globalConfigHistoryDTO the globalConfigHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated globalConfigHistoryDTO,
     * or with status 400 (Bad Request) if the globalConfigHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the globalConfigHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/global-config-histories")
    @Timed
    public ResponseEntity<GlobalConfigHistoryDTO> updateGlobalConfigHistory(@Valid @RequestBody GlobalConfigHistoryDTO globalConfigHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update GlobalConfigHistory : {}", globalConfigHistoryDTO);
        if (globalConfigHistoryDTO.getId() == null) {
            return createGlobalConfigHistory(globalConfigHistoryDTO);
        }
        GlobalConfigHistoryDTO result = globalConfigHistoryService.save(globalConfigHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, globalConfigHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /global-config-histories : get all the globalConfigHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of globalConfigHistories in body
     */
    @GetMapping("/global-config-histories")
    @Timed
    public List<GlobalConfigHistoryDTO> getAllGlobalConfigHistories() {
        log.debug("REST request to get all GlobalConfigHistories");
        return globalConfigHistoryService.findAll();
        }

    /**
     * GET  /global-config-histories/:id : get the "id" globalConfigHistory.
     *
     * @param id the id of the globalConfigHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the globalConfigHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/global-config-histories/{id}")
    @Timed
    public ResponseEntity<GlobalConfigHistoryDTO> getGlobalConfigHistory(@PathVariable Long id) {
        log.debug("REST request to get GlobalConfigHistory : {}", id);
        GlobalConfigHistoryDTO globalConfigHistoryDTO = globalConfigHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(globalConfigHistoryDTO));
    }

    /**
     * DELETE  /global-config-histories/:id : delete the "id" globalConfigHistory.
     *
     * @param id the id of the globalConfigHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/global-config-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteGlobalConfigHistory(@PathVariable Long id) {
        log.debug("REST request to delete GlobalConfigHistory : {}", id);
        globalConfigHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

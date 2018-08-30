package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.ServerHistoryService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.ServerHistoryDTO;
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
 * REST controller for managing ServerHistory.
 */
@RestController
@RequestMapping("/api")
public class ServerHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ServerHistoryResource.class);

    private static final String ENTITY_NAME = "serverHistory";

    private final ServerHistoryService serverHistoryService;

    public ServerHistoryResource(ServerHistoryService serverHistoryService) {
        this.serverHistoryService = serverHistoryService;
    }

    /**
     * POST  /server-histories : Create a new serverHistory.
     *
     * @param serverHistoryDTO the serverHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serverHistoryDTO, or with status 400 (Bad Request) if the serverHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/server-histories")
    @Timed
    public ResponseEntity<ServerHistoryDTO> createServerHistory(@Valid @RequestBody ServerHistoryDTO serverHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save ServerHistory : {}", serverHistoryDTO);
        if (serverHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new serverHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServerHistoryDTO result = serverHistoryService.save(serverHistoryDTO);
        return ResponseEntity.created(new URI("/api/server-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /server-histories : Updates an existing serverHistory.
     *
     * @param serverHistoryDTO the serverHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serverHistoryDTO,
     * or with status 400 (Bad Request) if the serverHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the serverHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/server-histories")
    @Timed
    public ResponseEntity<ServerHistoryDTO> updateServerHistory(@Valid @RequestBody ServerHistoryDTO serverHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update ServerHistory : {}", serverHistoryDTO);
        if (serverHistoryDTO.getId() == null) {
            return createServerHistory(serverHistoryDTO);
        }
        ServerHistoryDTO result = serverHistoryService.save(serverHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serverHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /server-histories : get all the serverHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of serverHistories in body
     */
    @GetMapping("/server-histories")
    @Timed
    public List<ServerHistoryDTO> getAllServerHistories() {
        log.debug("REST request to get all ServerHistories");
        return serverHistoryService.findAll();
        }

    /**
     * GET  /server-histories/:id : get the "id" serverHistory.
     *
     * @param id the id of the serverHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serverHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/server-histories/{id}")
    @Timed
    public ResponseEntity<ServerHistoryDTO> getServerHistory(@PathVariable Long id) {
        log.debug("REST request to get ServerHistory : {}", id);
        ServerHistoryDTO serverHistoryDTO = serverHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(serverHistoryDTO));
    }

    /**
     * DELETE  /server-histories/:id : delete the "id" serverHistory.
     *
     * @param id the id of the serverHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/server-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteServerHistory(@PathVariable Long id) {
        log.debug("REST request to delete ServerHistory : {}", id);
        serverHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

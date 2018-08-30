package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.ServerService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.ServerDTO;
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
 * REST controller for managing Server.
 */
@RestController
@RequestMapping("/api")
public class ServerResource {

    private final Logger log = LoggerFactory.getLogger(ServerResource.class);

    private static final String ENTITY_NAME = "server";

    private final ServerService serverService;

    public ServerResource(ServerService serverService) {
        this.serverService = serverService;
    }

    /**
     * POST  /servers : Create a new server.
     *
     * @param serverDTO the serverDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new serverDTO, or with status 400 (Bad Request) if the server has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/servers")
    @Timed
    public ResponseEntity<ServerDTO> createServer(@Valid @RequestBody ServerDTO serverDTO) throws URISyntaxException {
        log.debug("REST request to save Server : {}", serverDTO);
        if (serverDTO.getId() != null) {
            throw new BadRequestAlertException("A new server cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServerDTO result = serverService.save(serverDTO);
        return ResponseEntity.created(new URI("/api/servers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /servers : Updates an existing server.
     *
     * @param serverDTO the serverDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated serverDTO,
     * or with status 400 (Bad Request) if the serverDTO is not valid,
     * or with status 500 (Internal Server Error) if the serverDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/servers")
    @Timed
    public ResponseEntity<ServerDTO> updateServer(@Valid @RequestBody ServerDTO serverDTO) throws URISyntaxException {
        log.debug("REST request to update Server : {}", serverDTO);
        if (serverDTO.getId() == null) {
            return createServer(serverDTO);
        }
        ServerDTO result = serverService.save(serverDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, serverDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /servers : get all the servers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of servers in body
     */
    @GetMapping("/servers")
    @Timed
    public List<ServerDTO> getAllServers() {
        log.debug("REST request to get all Servers");
        return serverService.findAll();
        }

    /**
     * GET  /servers/:id : get the "id" server.
     *
     * @param id the id of the serverDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the serverDTO, or with status 404 (Not Found)
     */
    @GetMapping("/servers/{id}")
    @Timed
    public ResponseEntity<ServerDTO> getServer(@PathVariable Long id) {
        log.debug("REST request to get Server : {}", id);
        ServerDTO serverDTO = serverService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(serverDTO));
    }

    /**
     * DELETE  /servers/:id : delete the "id" server.
     *
     * @param id the id of the serverDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/servers/{id}")
    @Timed
    public ResponseEntity<Void> deleteServer(@PathVariable Long id) {
        log.debug("REST request to delete Server : {}", id);
        serverService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.ClientHeartbeatInfoService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.ClientHeartbeatInfoDTO;
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
 * REST controller for managing ClientHeartbeatInfo.
 */
@RestController
@RequestMapping("/api")
public class ClientHeartbeatInfoResource {

    private final Logger log = LoggerFactory.getLogger(ClientHeartbeatInfoResource.class);

    private static final String ENTITY_NAME = "clientHeartbeatInfo";

    private final ClientHeartbeatInfoService clientHeartbeatInfoService;

    public ClientHeartbeatInfoResource(ClientHeartbeatInfoService clientHeartbeatInfoService) {
        this.clientHeartbeatInfoService = clientHeartbeatInfoService;
    }

    /**
     * POST  /client-heartbeat-infos : Create a new clientHeartbeatInfo.
     *
     * @param clientHeartbeatInfoDTO the clientHeartbeatInfoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new clientHeartbeatInfoDTO, or with status 400 (Bad Request) if the clientHeartbeatInfo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/client-heartbeat-infos")
    @Timed
    public ResponseEntity<ClientHeartbeatInfoDTO> createClientHeartbeatInfo(@Valid @RequestBody ClientHeartbeatInfoDTO clientHeartbeatInfoDTO) throws URISyntaxException {
        log.debug("REST request to save ClientHeartbeatInfo : {}", clientHeartbeatInfoDTO);
        if (clientHeartbeatInfoDTO.getId() != null) {
            throw new BadRequestAlertException("A new clientHeartbeatInfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClientHeartbeatInfoDTO result = clientHeartbeatInfoService.save(clientHeartbeatInfoDTO);
        return ResponseEntity.created(new URI("/api/client-heartbeat-infos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /client-heartbeat-infos : Updates an existing clientHeartbeatInfo.
     *
     * @param clientHeartbeatInfoDTO the clientHeartbeatInfoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated clientHeartbeatInfoDTO,
     * or with status 400 (Bad Request) if the clientHeartbeatInfoDTO is not valid,
     * or with status 500 (Internal Server Error) if the clientHeartbeatInfoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/client-heartbeat-infos")
    @Timed
    public ResponseEntity<ClientHeartbeatInfoDTO> updateClientHeartbeatInfo(@Valid @RequestBody ClientHeartbeatInfoDTO clientHeartbeatInfoDTO) throws URISyntaxException {
        log.debug("REST request to update ClientHeartbeatInfo : {}", clientHeartbeatInfoDTO);
        if (clientHeartbeatInfoDTO.getId() == null) {
            return createClientHeartbeatInfo(clientHeartbeatInfoDTO);
        }
        ClientHeartbeatInfoDTO result = clientHeartbeatInfoService.save(clientHeartbeatInfoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, clientHeartbeatInfoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /client-heartbeat-infos : get all the clientHeartbeatInfos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of clientHeartbeatInfos in body
     */
    @GetMapping("/client-heartbeat-infos")
    @Timed
    public List<ClientHeartbeatInfoDTO> getAllClientHeartbeatInfos() {
        log.debug("REST request to get all ClientHeartbeatInfos");
        return clientHeartbeatInfoService.findAll();
        }

    /**
     * GET  /client-heartbeat-infos/:id : get the "id" clientHeartbeatInfo.
     *
     * @param id the id of the clientHeartbeatInfoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the clientHeartbeatInfoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/client-heartbeat-infos/{id}")
    @Timed
    public ResponseEntity<ClientHeartbeatInfoDTO> getClientHeartbeatInfo(@PathVariable Long id) {
        log.debug("REST request to get ClientHeartbeatInfo : {}", id);
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = clientHeartbeatInfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(clientHeartbeatInfoDTO));
    }

    /**
     * DELETE  /client-heartbeat-infos/:id : delete the "id" clientHeartbeatInfo.
     *
     * @param id the id of the clientHeartbeatInfoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/client-heartbeat-infos/{id}")
    @Timed
    public ResponseEntity<Void> deleteClientHeartbeatInfo(@PathVariable Long id) {
        log.debug("REST request to delete ClientHeartbeatInfo : {}", id);
        clientHeartbeatInfoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

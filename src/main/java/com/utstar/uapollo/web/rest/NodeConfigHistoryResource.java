package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.NodeConfigHistoryService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.NodeConfigHistoryDTO;
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
 * REST controller for managing NodeConfigHistory.
 */
@RestController
@RequestMapping("/api")
public class NodeConfigHistoryResource {

    private final Logger log = LoggerFactory.getLogger(NodeConfigHistoryResource.class);

    private static final String ENTITY_NAME = "nodeConfigHistory";

    private final NodeConfigHistoryService nodeConfigHistoryService;

    public NodeConfigHistoryResource(NodeConfigHistoryService nodeConfigHistoryService) {
        this.nodeConfigHistoryService = nodeConfigHistoryService;
    }

    /**
     * POST  /node-config-histories : Create a new nodeConfigHistory.
     *
     * @param nodeConfigHistoryDTO the nodeConfigHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new nodeConfigHistoryDTO, or with status 400 (Bad Request) if the nodeConfigHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/node-config-histories")
    @Timed
    public ResponseEntity<NodeConfigHistoryDTO> createNodeConfigHistory(@Valid @RequestBody NodeConfigHistoryDTO nodeConfigHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save NodeConfigHistory : {}", nodeConfigHistoryDTO);
        if (nodeConfigHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new nodeConfigHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NodeConfigHistoryDTO result = nodeConfigHistoryService.save(nodeConfigHistoryDTO);
        return ResponseEntity.created(new URI("/api/node-config-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /node-config-histories : Updates an existing nodeConfigHistory.
     *
     * @param nodeConfigHistoryDTO the nodeConfigHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated nodeConfigHistoryDTO,
     * or with status 400 (Bad Request) if the nodeConfigHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the nodeConfigHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/node-config-histories")
    @Timed
    public ResponseEntity<NodeConfigHistoryDTO> updateNodeConfigHistory(@Valid @RequestBody NodeConfigHistoryDTO nodeConfigHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update NodeConfigHistory : {}", nodeConfigHistoryDTO);
        if (nodeConfigHistoryDTO.getId() == null) {
            return createNodeConfigHistory(nodeConfigHistoryDTO);
        }
        NodeConfigHistoryDTO result = nodeConfigHistoryService.save(nodeConfigHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nodeConfigHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /node-config-histories : get all the nodeConfigHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of nodeConfigHistories in body
     */
    @GetMapping("/node-config-histories")
    @Timed
    public List<NodeConfigHistoryDTO> getAllNodeConfigHistories() {
        log.debug("REST request to get all NodeConfigHistories");
        return nodeConfigHistoryService.findAll();
        }

    /**
     * GET  /node-config-histories/:id : get the "id" nodeConfigHistory.
     *
     * @param id the id of the nodeConfigHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the nodeConfigHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/node-config-histories/{id}")
    @Timed
    public ResponseEntity<NodeConfigHistoryDTO> getNodeConfigHistory(@PathVariable Long id) {
        log.debug("REST request to get NodeConfigHistory : {}", id);
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(nodeConfigHistoryDTO));
    }

    /**
     * DELETE  /node-config-histories/:id : delete the "id" nodeConfigHistory.
     *
     * @param id the id of the nodeConfigHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/node-config-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteNodeConfigHistory(@PathVariable Long id) {
        log.debug("REST request to delete NodeConfigHistory : {}", id);
        nodeConfigHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

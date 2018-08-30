package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.NodeHistoryService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.NodeHistoryDTO;
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
 * REST controller for managing NodeHistory.
 */
@RestController
@RequestMapping("/api")
public class NodeHistoryResource {

    private final Logger log = LoggerFactory.getLogger(NodeHistoryResource.class);

    private static final String ENTITY_NAME = "nodeHistory";

    private final NodeHistoryService nodeHistoryService;

    public NodeHistoryResource(NodeHistoryService nodeHistoryService) {
        this.nodeHistoryService = nodeHistoryService;
    }

    /**
     * POST  /node-histories : Create a new nodeHistory.
     *
     * @param nodeHistoryDTO the nodeHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new nodeHistoryDTO, or with status 400 (Bad Request) if the nodeHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/node-histories")
    @Timed
    public ResponseEntity<NodeHistoryDTO> createNodeHistory(@Valid @RequestBody NodeHistoryDTO nodeHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save NodeHistory : {}", nodeHistoryDTO);
        if (nodeHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new nodeHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NodeHistoryDTO result = nodeHistoryService.save(nodeHistoryDTO);
        return ResponseEntity.created(new URI("/api/node-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /node-histories : Updates an existing nodeHistory.
     *
     * @param nodeHistoryDTO the nodeHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated nodeHistoryDTO,
     * or with status 400 (Bad Request) if the nodeHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the nodeHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/node-histories")
    @Timed
    public ResponseEntity<NodeHistoryDTO> updateNodeHistory(@Valid @RequestBody NodeHistoryDTO nodeHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update NodeHistory : {}", nodeHistoryDTO);
        if (nodeHistoryDTO.getId() == null) {
            return createNodeHistory(nodeHistoryDTO);
        }
        NodeHistoryDTO result = nodeHistoryService.save(nodeHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nodeHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /node-histories : get all the nodeHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of nodeHistories in body
     */
    @GetMapping("/node-histories")
    @Timed
    public List<NodeHistoryDTO> getAllNodeHistories() {
        log.debug("REST request to get all NodeHistories");
        return nodeHistoryService.findAll();
        }

    /**
     * GET  /node-histories/:id : get the "id" nodeHistory.
     *
     * @param id the id of the nodeHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the nodeHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/node-histories/{id}")
    @Timed
    public ResponseEntity<NodeHistoryDTO> getNodeHistory(@PathVariable Long id) {
        log.debug("REST request to get NodeHistory : {}", id);
        NodeHistoryDTO nodeHistoryDTO = nodeHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(nodeHistoryDTO));
    }

    /**
     * DELETE  /node-histories/:id : delete the "id" nodeHistory.
     *
     * @param id the id of the nodeHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/node-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteNodeHistory(@PathVariable Long id) {
        log.debug("REST request to delete NodeHistory : {}", id);
        nodeHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

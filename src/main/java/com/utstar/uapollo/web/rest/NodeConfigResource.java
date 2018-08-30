package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.NodeConfigService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.NodeConfigDTO;
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
 * REST controller for managing NodeConfig.
 */
@RestController
@RequestMapping("/api")
public class NodeConfigResource {

    private final Logger log = LoggerFactory.getLogger(NodeConfigResource.class);

    private static final String ENTITY_NAME = "nodeConfig";

    private final NodeConfigService nodeConfigService;

    public NodeConfigResource(NodeConfigService nodeConfigService) {
        this.nodeConfigService = nodeConfigService;
    }

    /**
     * POST  /node-configs : Create a new nodeConfig.
     *
     * @param nodeConfigDTO the nodeConfigDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new nodeConfigDTO, or with status 400 (Bad Request) if the nodeConfig has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/node-configs")
    @Timed
    public ResponseEntity<NodeConfigDTO> createNodeConfig(@Valid @RequestBody NodeConfigDTO nodeConfigDTO) throws URISyntaxException {
        log.debug("REST request to save NodeConfig : {}", nodeConfigDTO);
        if (nodeConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new nodeConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NodeConfigDTO result = nodeConfigService.save(nodeConfigDTO);
        return ResponseEntity.created(new URI("/api/node-configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /node-configs : Updates an existing nodeConfig.
     *
     * @param nodeConfigDTO the nodeConfigDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated nodeConfigDTO,
     * or with status 400 (Bad Request) if the nodeConfigDTO is not valid,
     * or with status 500 (Internal Server Error) if the nodeConfigDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/node-configs")
    @Timed
    public ResponseEntity<NodeConfigDTO> updateNodeConfig(@Valid @RequestBody NodeConfigDTO nodeConfigDTO) throws URISyntaxException {
        log.debug("REST request to update NodeConfig : {}", nodeConfigDTO);
        if (nodeConfigDTO.getId() == null) {
            return createNodeConfig(nodeConfigDTO);
        }
        NodeConfigDTO result = nodeConfigService.save(nodeConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nodeConfigDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /node-configs : get all the nodeConfigs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of nodeConfigs in body
     */
    @GetMapping("/node-configs")
    @Timed
    public List<NodeConfigDTO> getAllNodeConfigs() {
        log.debug("REST request to get all NodeConfigs");
        return nodeConfigService.findAll();
        }

    /**
     * GET  /node-configs/:id : get the "id" nodeConfig.
     *
     * @param id the id of the nodeConfigDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the nodeConfigDTO, or with status 404 (Not Found)
     */
    @GetMapping("/node-configs/{id}")
    @Timed
    public ResponseEntity<NodeConfigDTO> getNodeConfig(@PathVariable Long id) {
        log.debug("REST request to get NodeConfig : {}", id);
        NodeConfigDTO nodeConfigDTO = nodeConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(nodeConfigDTO));
    }

    /**
     * DELETE  /node-configs/:id : delete the "id" nodeConfig.
     *
     * @param id the id of the nodeConfigDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/node-configs/{id}")
    @Timed
    public ResponseEntity<Void> deleteNodeConfig(@PathVariable Long id) {
        log.debug("REST request to delete NodeConfig : {}", id);
        nodeConfigService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

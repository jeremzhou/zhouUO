package com.utstar.uapollo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.service.ProjectHistoryService;
import com.utstar.uapollo.web.rest.errors.BadRequestAlertException;
import com.utstar.uapollo.web.rest.util.HeaderUtil;
import com.utstar.uapollo.service.dto.ProjectHistoryDTO;
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
 * REST controller for managing ProjectHistory.
 */
@RestController
@RequestMapping("/api")
public class ProjectHistoryResource {

    private final Logger log = LoggerFactory.getLogger(ProjectHistoryResource.class);

    private static final String ENTITY_NAME = "projectHistory";

    private final ProjectHistoryService projectHistoryService;

    public ProjectHistoryResource(ProjectHistoryService projectHistoryService) {
        this.projectHistoryService = projectHistoryService;
    }

    /**
     * POST  /project-histories : Create a new projectHistory.
     *
     * @param projectHistoryDTO the projectHistoryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectHistoryDTO, or with status 400 (Bad Request) if the projectHistory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-histories")
    @Timed
    public ResponseEntity<ProjectHistoryDTO> createProjectHistory(@Valid @RequestBody ProjectHistoryDTO projectHistoryDTO) throws URISyntaxException {
        log.debug("REST request to save ProjectHistory : {}", projectHistoryDTO);
        if (projectHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new projectHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectHistoryDTO result = projectHistoryService.save(projectHistoryDTO);
        return ResponseEntity.created(new URI("/api/project-histories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-histories : Updates an existing projectHistory.
     *
     * @param projectHistoryDTO the projectHistoryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectHistoryDTO,
     * or with status 400 (Bad Request) if the projectHistoryDTO is not valid,
     * or with status 500 (Internal Server Error) if the projectHistoryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-histories")
    @Timed
    public ResponseEntity<ProjectHistoryDTO> updateProjectHistory(@Valid @RequestBody ProjectHistoryDTO projectHistoryDTO) throws URISyntaxException {
        log.debug("REST request to update ProjectHistory : {}", projectHistoryDTO);
        if (projectHistoryDTO.getId() == null) {
            return createProjectHistory(projectHistoryDTO);
        }
        ProjectHistoryDTO result = projectHistoryService.save(projectHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectHistoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-histories : get all the projectHistories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projectHistories in body
     */
    @GetMapping("/project-histories")
    @Timed
    public List<ProjectHistoryDTO> getAllProjectHistories() {
        log.debug("REST request to get all ProjectHistories");
        return projectHistoryService.findAll();
        }

    /**
     * GET  /project-histories/:id : get the "id" projectHistory.
     *
     * @param id the id of the projectHistoryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectHistoryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/project-histories/{id}")
    @Timed
    public ResponseEntity<ProjectHistoryDTO> getProjectHistory(@PathVariable Long id) {
        log.debug("REST request to get ProjectHistory : {}", id);
        ProjectHistoryDTO projectHistoryDTO = projectHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(projectHistoryDTO));
    }

    /**
     * DELETE  /project-histories/:id : delete the "id" projectHistory.
     *
     * @param id the id of the projectHistoryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-histories/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectHistory(@PathVariable Long id) {
        log.debug("REST request to delete ProjectHistory : {}", id);
        projectHistoryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}

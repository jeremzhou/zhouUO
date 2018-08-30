package com.utstar.uapollo.custom.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.utstar.uapollo.custom.service.ProjectCustomService;
import com.utstar.uapollo.service.dto.ProjectDTO;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * @author UTSC0167
 * @date 2018年4月16日
 *
 */
@RestController
@RequestMapping("/api/v1")
public class ProjectController {

    private final Logger log = LoggerFactory.getLogger(ProjectController.class);

    private final ProjectCustomService projectCustomService;

    public ProjectController(ProjectCustomService projectCustomService) {
        this.projectCustomService = projectCustomService;
    }

    @GetMapping("/projects")
    @Timed
    public List<ProjectDTO> getProjectsByUserId(@RequestParam Integer userId) {

        log.debug("REST request to get all Projects by userId: {}", userId);
        return projectCustomService.findByUserId(userId);
    }

    @GetMapping("/projects/{name}")
    @Timed
    public ResponseEntity<ProjectDTO> getProjectByName(@PathVariable String name) {

        log.debug("REST request to get the Project by name: {}", name);
        return ResponseUtil.wrapOrNotFound(projectCustomService.findByName(name));
    }
}

package com.itgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgm.domain.ModeloExclusivo;
import com.itgm.repository.search.ModeloExclusivoSearchRepository;

import com.itgm.repository.ModeloExclusivoRepository;
import com.itgm.security.SecurityUtils;
import com.itgm.web.rest.util.HeaderUtil;
import com.itgm.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ModeloExclusivo.
 */
@RestController
@RequestMapping("/api")
public class ModeloExclusivoResource {

    private final Logger log = LoggerFactory.getLogger(ModeloExclusivoResource.class);

    private static final String ENTITY_NAME = "modeloExclusivo";

    private final ModeloExclusivoRepository modeloExclusivoRepository;

    private final ModeloExclusivoSearchRepository modeloExclusivoSearchRepository;

    public ModeloExclusivoResource(ModeloExclusivoRepository modeloExclusivoRepository, ModeloExclusivoSearchRepository modeloExclusivoSearchRepository) {
        this.modeloExclusivoRepository = modeloExclusivoRepository;
        this.modeloExclusivoSearchRepository = modeloExclusivoSearchRepository;
    }

    /**
     * POST  /modelo-exclusivos : Create a new modeloExclusivo.
     *
     * @param modeloExclusivo the modeloExclusivo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new modeloExclusivo, or with status 400 (Bad Request) if the modeloExclusivo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/modelo-exclusivos")
    @Timed
    public ResponseEntity<ModeloExclusivo> createModeloExclusivo(@RequestBody ModeloExclusivo modeloExclusivo) throws URISyntaxException {
        log.debug("REST request to save ModeloExclusivo : {}", modeloExclusivo);
        if (modeloExclusivo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new modeloExclusivo cannot already have an ID")).body(null);
        }
        ModeloExclusivo result = modeloExclusivoRepository.save(modeloExclusivo);
        modeloExclusivoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/modelo-exclusivos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /modelo-exclusivos : Updates an existing modeloExclusivo.
     *
     * @param modeloExclusivo the modeloExclusivo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated modeloExclusivo,
     * or with status 400 (Bad Request) if the modeloExclusivo is not valid,
     * or with status 500 (Internal Server Error) if the modeloExclusivo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/modelo-exclusivos")
    @Timed
    public ResponseEntity<ModeloExclusivo> updateModeloExclusivo(@RequestBody ModeloExclusivo modeloExclusivo) throws URISyntaxException {
        log.debug("REST request to update ModeloExclusivo : {}", modeloExclusivo);
        if (modeloExclusivo.getId() == null) {
            return createModeloExclusivo(modeloExclusivo);
        }
        ModeloExclusivo result = modeloExclusivoRepository.save(modeloExclusivo);
        modeloExclusivoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, modeloExclusivo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /modelo-exclusivos : get all the modeloExclusivos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of modeloExclusivos in body
     */
    @GetMapping("/modelo-exclusivos")
    @Timed
    public ResponseEntity<List<ModeloExclusivo>> getAllModeloExclusivos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of ModeloExclusivos");
        Page<ModeloExclusivo> page;

        if(SecurityUtils.isCurrentUserInRole("ROLE_ADMIN"))
            page = modeloExclusivoRepository.findAll(pageable);
        else
            page = modeloExclusivoRepository.findByUserIsCurrentUser(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/modelo-exclusivos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /modelo-exclusivos/:id : get the "id" modeloExclusivo.
     *
     * @param id the id of the modeloExclusivo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the modeloExclusivo, or with status 404 (Not Found)
     */
    @GetMapping("/modelo-exclusivos/{id}")
    @Timed
    public ResponseEntity<ModeloExclusivo> getModeloExclusivo(@PathVariable Long id) {
        log.debug("REST request to get ModeloExclusivo : {}", id);
        ModeloExclusivo modeloExclusivo = modeloExclusivoRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(modeloExclusivo));
    }

    /**
     * DELETE  /modelo-exclusivos/:id : delete the "id" modeloExclusivo.
     *
     * @param id the id of the modeloExclusivo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/modelo-exclusivos/{id}")
    @Timed
    public ResponseEntity<Void> deleteModeloExclusivo(@PathVariable Long id) {
        log.debug("REST request to delete ModeloExclusivo : {}", id);
        modeloExclusivoRepository.delete(id);
        modeloExclusivoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/modelo-exclusivos?query=:query : search for the modeloExclusivo corresponding
     * to the query.
     *
     * @param query the query of the modeloExclusivo search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/modelo-exclusivos")
    @Timed
    public ResponseEntity<List<ModeloExclusivo>> searchModeloExclusivos(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of ModeloExclusivos for query {}", query);
        Page<ModeloExclusivo> page = modeloExclusivoSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/modelo-exclusivos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

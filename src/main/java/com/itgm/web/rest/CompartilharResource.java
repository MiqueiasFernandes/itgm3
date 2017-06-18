package com.itgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgm.domain.Compartilhar;

import com.itgm.repository.CompartilharRepository;
import com.itgm.repository.search.CompartilharSearchRepository;
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
 * REST controller for managing Compartilhar.
 */
@RestController
@RequestMapping("/api")
public class CompartilharResource {

    private final Logger log = LoggerFactory.getLogger(CompartilharResource.class);

    private static final String ENTITY_NAME = "compartilhar";
        
    private final CompartilharRepository compartilharRepository;

    private final CompartilharSearchRepository compartilharSearchRepository;

    public CompartilharResource(CompartilharRepository compartilharRepository, CompartilharSearchRepository compartilharSearchRepository) {
        this.compartilharRepository = compartilharRepository;
        this.compartilharSearchRepository = compartilharSearchRepository;
    }

    /**
     * POST  /compartilhars : Create a new compartilhar.
     *
     * @param compartilhar the compartilhar to create
     * @return the ResponseEntity with status 201 (Created) and with body the new compartilhar, or with status 400 (Bad Request) if the compartilhar has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/compartilhars")
    @Timed
    public ResponseEntity<Compartilhar> createCompartilhar(@RequestBody Compartilhar compartilhar) throws URISyntaxException {
        log.debug("REST request to save Compartilhar : {}", compartilhar);
        if (compartilhar.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new compartilhar cannot already have an ID")).body(null);
        }
        Compartilhar result = compartilharRepository.save(compartilhar);
        compartilharSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/compartilhars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /compartilhars : Updates an existing compartilhar.
     *
     * @param compartilhar the compartilhar to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated compartilhar,
     * or with status 400 (Bad Request) if the compartilhar is not valid,
     * or with status 500 (Internal Server Error) if the compartilhar couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/compartilhars")
    @Timed
    public ResponseEntity<Compartilhar> updateCompartilhar(@RequestBody Compartilhar compartilhar) throws URISyntaxException {
        log.debug("REST request to update Compartilhar : {}", compartilhar);
        if (compartilhar.getId() == null) {
            return createCompartilhar(compartilhar);
        }
        Compartilhar result = compartilharRepository.save(compartilhar);
        compartilharSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, compartilhar.getId().toString()))
            .body(result);
    }

    /**
     * GET  /compartilhars : get all the compartilhars.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of compartilhars in body
     */
    @GetMapping("/compartilhars")
    @Timed
    public ResponseEntity<List<Compartilhar>> getAllCompartilhars(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Compartilhars");
        Page<Compartilhar> page = compartilharRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/compartilhars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /compartilhars/:id : get the "id" compartilhar.
     *
     * @param id the id of the compartilhar to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the compartilhar, or with status 404 (Not Found)
     */
    @GetMapping("/compartilhars/{id}")
    @Timed
    public ResponseEntity<Compartilhar> getCompartilhar(@PathVariable Long id) {
        log.debug("REST request to get Compartilhar : {}", id);
        Compartilhar compartilhar = compartilharRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(compartilhar));
    }

    /**
     * DELETE  /compartilhars/:id : delete the "id" compartilhar.
     *
     * @param id the id of the compartilhar to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/compartilhars/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompartilhar(@PathVariable Long id) {
        log.debug("REST request to delete Compartilhar : {}", id);
        compartilharRepository.delete(id);
        compartilharSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/compartilhars?query=:query : search for the compartilhar corresponding
     * to the query.
     *
     * @param query the query of the compartilhar search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/compartilhars")
    @Timed
    public ResponseEntity<List<Compartilhar>> searchCompartilhars(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Compartilhars for query {}", query);
        Page<Compartilhar> page = compartilharSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/compartilhars");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

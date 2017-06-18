package com.itgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgm.domain.Customize;

import com.itgm.repository.CustomizeRepository;
import com.itgm.repository.search.CustomizeSearchRepository;
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
 * REST controller for managing Customize.
 */
@RestController
@RequestMapping("/api")
public class CustomizeResource {

    private final Logger log = LoggerFactory.getLogger(CustomizeResource.class);

    private static final String ENTITY_NAME = "customize";
        
    private final CustomizeRepository customizeRepository;

    private final CustomizeSearchRepository customizeSearchRepository;

    public CustomizeResource(CustomizeRepository customizeRepository, CustomizeSearchRepository customizeSearchRepository) {
        this.customizeRepository = customizeRepository;
        this.customizeSearchRepository = customizeSearchRepository;
    }

    /**
     * POST  /customizes : Create a new customize.
     *
     * @param customize the customize to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customize, or with status 400 (Bad Request) if the customize has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customizes")
    @Timed
    public ResponseEntity<Customize> createCustomize(@RequestBody Customize customize) throws URISyntaxException {
        log.debug("REST request to save Customize : {}", customize);
        if (customize.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new customize cannot already have an ID")).body(null);
        }
        Customize result = customizeRepository.save(customize);
        customizeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/customizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customizes : Updates an existing customize.
     *
     * @param customize the customize to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customize,
     * or with status 400 (Bad Request) if the customize is not valid,
     * or with status 500 (Internal Server Error) if the customize couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customizes")
    @Timed
    public ResponseEntity<Customize> updateCustomize(@RequestBody Customize customize) throws URISyntaxException {
        log.debug("REST request to update Customize : {}", customize);
        if (customize.getId() == null) {
            return createCustomize(customize);
        }
        Customize result = customizeRepository.save(customize);
        customizeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customize.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customizes : get all the customizes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customizes in body
     */
    @GetMapping("/customizes")
    @Timed
    public ResponseEntity<List<Customize>> getAllCustomizes(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Customizes");
        Page<Customize> page = customizeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customizes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customizes/:id : get the "id" customize.
     *
     * @param id the id of the customize to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customize, or with status 404 (Not Found)
     */
    @GetMapping("/customizes/{id}")
    @Timed
    public ResponseEntity<Customize> getCustomize(@PathVariable Long id) {
        log.debug("REST request to get Customize : {}", id);
        Customize customize = customizeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(customize));
    }

    /**
     * DELETE  /customizes/:id : delete the "id" customize.
     *
     * @param id the id of the customize to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customizes/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomize(@PathVariable Long id) {
        log.debug("REST request to delete Customize : {}", id);
        customizeRepository.delete(id);
        customizeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/customizes?query=:query : search for the customize corresponding
     * to the query.
     *
     * @param query the query of the customize search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/customizes")
    @Timed
    public ResponseEntity<List<Customize>> searchCustomizes(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Customizes for query {}", query);
        Page<Customize> page = customizeSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customizes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

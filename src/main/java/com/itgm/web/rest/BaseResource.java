package com.itgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgm.domain.Base;

import com.itgm.repository.BaseRepository;
import com.itgm.repository.search.BaseSearchRepository;
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
 * REST controller for managing Base.
 */
@RestController
@RequestMapping("/api")
public class BaseResource {

    private final Logger log = LoggerFactory.getLogger(BaseResource.class);

    private static final String ENTITY_NAME = "base";
        
    private final BaseRepository baseRepository;

    private final BaseSearchRepository baseSearchRepository;

    public BaseResource(BaseRepository baseRepository, BaseSearchRepository baseSearchRepository) {
        this.baseRepository = baseRepository;
        this.baseSearchRepository = baseSearchRepository;
    }

    /**
     * POST  /bases : Create a new base.
     *
     * @param base the base to create
     * @return the ResponseEntity with status 201 (Created) and with body the new base, or with status 400 (Bad Request) if the base has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/bases")
    @Timed
    public ResponseEntity<Base> createBase(@RequestBody Base base) throws URISyntaxException {
        log.debug("REST request to save Base : {}", base);
        if (base.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new base cannot already have an ID")).body(null);
        }
        Base result = baseRepository.save(base);
        baseSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/bases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bases : Updates an existing base.
     *
     * @param base the base to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated base,
     * or with status 400 (Bad Request) if the base is not valid,
     * or with status 500 (Internal Server Error) if the base couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/bases")
    @Timed
    public ResponseEntity<Base> updateBase(@RequestBody Base base) throws URISyntaxException {
        log.debug("REST request to update Base : {}", base);
        if (base.getId() == null) {
            return createBase(base);
        }
        Base result = baseRepository.save(base);
        baseSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, base.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bases : get all the bases.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of bases in body
     */
    @GetMapping("/bases")
    @Timed
    public ResponseEntity<List<Base>> getAllBases(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Bases");
        Page<Base> page = baseRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bases/:id : get the "id" base.
     *
     * @param id the id of the base to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the base, or with status 404 (Not Found)
     */
    @GetMapping("/bases/{id}")
    @Timed
    public ResponseEntity<Base> getBase(@PathVariable Long id) {
        log.debug("REST request to get Base : {}", id);
        Base base = baseRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(base));
    }

    /**
     * DELETE  /bases/:id : delete the "id" base.
     *
     * @param id the id of the base to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/bases/{id}")
    @Timed
    public ResponseEntity<Void> deleteBase(@PathVariable Long id) {
        log.debug("REST request to delete Base : {}", id);
        baseRepository.delete(id);
        baseSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/bases?query=:query : search for the base corresponding
     * to the query.
     *
     * @param query the query of the base search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/bases")
    @Timed
    public ResponseEntity<List<Base>> searchBases(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Bases for query {}", query);
        Page<Base> page = baseSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bases");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

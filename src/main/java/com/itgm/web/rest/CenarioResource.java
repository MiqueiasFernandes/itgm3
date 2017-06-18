package com.itgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgm.domain.Cenario;

import com.itgm.repository.CenarioRepository;
import com.itgm.repository.search.CenarioSearchRepository;
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
 * REST controller for managing Cenario.
 */
@RestController
@RequestMapping("/api")
public class CenarioResource {

    private final Logger log = LoggerFactory.getLogger(CenarioResource.class);

    private static final String ENTITY_NAME = "cenario";
        
    private final CenarioRepository cenarioRepository;

    private final CenarioSearchRepository cenarioSearchRepository;

    public CenarioResource(CenarioRepository cenarioRepository, CenarioSearchRepository cenarioSearchRepository) {
        this.cenarioRepository = cenarioRepository;
        this.cenarioSearchRepository = cenarioSearchRepository;
    }

    /**
     * POST  /cenarios : Create a new cenario.
     *
     * @param cenario the cenario to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cenario, or with status 400 (Bad Request) if the cenario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cenarios")
    @Timed
    public ResponseEntity<Cenario> createCenario(@RequestBody Cenario cenario) throws URISyntaxException {
        log.debug("REST request to save Cenario : {}", cenario);
        if (cenario.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new cenario cannot already have an ID")).body(null);
        }
        Cenario result = cenarioRepository.save(cenario);
        cenarioSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cenarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cenarios : Updates an existing cenario.
     *
     * @param cenario the cenario to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cenario,
     * or with status 400 (Bad Request) if the cenario is not valid,
     * or with status 500 (Internal Server Error) if the cenario couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cenarios")
    @Timed
    public ResponseEntity<Cenario> updateCenario(@RequestBody Cenario cenario) throws URISyntaxException {
        log.debug("REST request to update Cenario : {}", cenario);
        if (cenario.getId() == null) {
            return createCenario(cenario);
        }
        Cenario result = cenarioRepository.save(cenario);
        cenarioSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cenario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cenarios : get all the cenarios.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cenarios in body
     */
    @GetMapping("/cenarios")
    @Timed
    public ResponseEntity<List<Cenario>> getAllCenarios(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Cenarios");
        Page<Cenario> page = cenarioRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cenarios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cenarios/:id : get the "id" cenario.
     *
     * @param id the id of the cenario to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cenario, or with status 404 (Not Found)
     */
    @GetMapping("/cenarios/{id}")
    @Timed
    public ResponseEntity<Cenario> getCenario(@PathVariable Long id) {
        log.debug("REST request to get Cenario : {}", id);
        Cenario cenario = cenarioRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cenario));
    }

    /**
     * DELETE  /cenarios/:id : delete the "id" cenario.
     *
     * @param id the id of the cenario to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cenarios/{id}")
    @Timed
    public ResponseEntity<Void> deleteCenario(@PathVariable Long id) {
        log.debug("REST request to delete Cenario : {}", id);
        cenarioRepository.delete(id);
        cenarioSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cenarios?query=:query : search for the cenario corresponding
     * to the query.
     *
     * @param query the query of the cenario search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cenarios")
    @Timed
    public ResponseEntity<List<Cenario>> searchCenarios(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Cenarios for query {}", query);
        Page<Cenario> page = cenarioSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cenarios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

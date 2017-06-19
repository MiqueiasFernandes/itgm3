package com.itgm.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.itgm.domain.Cenario;
import com.itgm.domain.Terminal;
import com.itgm.repository.search.TerminalSearchRepository;

import com.itgm.repository.TerminalRepository;
import com.itgm.security.SecurityUtils;
import com.itgm.service.jriaccess.Itgmrest;
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
 * REST controller for managing Terminal.
 */
@RestController
@RequestMapping("/api")
public class TerminalResource {

    private final Logger log = LoggerFactory.getLogger(TerminalResource.class);

    private static final String ENTITY_NAME = "terminal";

    private final TerminalRepository terminalRepository;

    private final TerminalSearchRepository terminalSearchRepository;

    public TerminalResource(TerminalRepository terminalRepository, TerminalSearchRepository terminalSearchRepository) {
        this.terminalRepository = terminalRepository;
        this.terminalSearchRepository = terminalSearchRepository;
    }

    /**
     * POST  /terminals : Create a new terminal.
     *
     * @param terminal the terminal to create
     * @return the ResponseEntity with status 201 (Created) and with body the new terminal, or with status 400 (Bad Request) if the terminal has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/terminals")
    @Timed
    public ResponseEntity<Terminal> createTerminal(@RequestBody Terminal terminal) throws URISyntaxException {
        log.debug("REST request to save Terminal : {}", terminal);
        if (terminal.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new terminal cannot already have an ID")).body(null);
        }
        Cenario cenario = terminal.getCenario();
        String token =  Itgmrest.getToken(
            cenario.getProjeto().getUser().getLogin(),
            cenario.getProjeto().getNome(),
            cenario.getNome(),
            terminal.getNome(),
            new String[]{"LIVE", "log.txt", "DEBUG", "--vanilla"},
            100,
            100,
            100,
            true,
            "null"
        );

        terminal.setUrl(token);
        Terminal result = terminalRepository.save(terminal);
        terminalSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/terminals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /terminals : Updates an existing terminal.
     *
     * @param terminal the terminal to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated terminal,
     * or with status 400 (Bad Request) if the terminal is not valid,
     * or with status 500 (Internal Server Error) if the terminal couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/terminals")
    @Timed
    public ResponseEntity<Terminal> updateTerminal(@RequestBody Terminal terminal) throws URISyntaxException {
        log.debug("REST request to update Terminal : {}", terminal);
        if (terminal.getId() == null) {
            return createTerminal(terminal);
        }
        Terminal result = terminalRepository.save(terminal);
        terminalSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, terminal.getId().toString()))
            .body(result);
    }

    /**
     * GET  /terminals : get all the terminals.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of terminals in body
     */
    @GetMapping("/terminals")
    @Timed
    public ResponseEntity<List<Terminal>> getAllTerminals(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Terminals");
        Page<Terminal> page;

        if(SecurityUtils.isCurrentUserInRole("ROLE_ADMIN"))
            page = terminalRepository.findAll(pageable);
        else
            page = terminalRepository.findByUserIsCurrentUser(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/terminals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /terminals/:id : get the "id" terminal.
     *
     * @param id the id of the terminal to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the terminal, or with status 404 (Not Found)
     */
    @GetMapping("/terminals/{id}")
    @Timed
    public ResponseEntity<Terminal> getTerminal(@PathVariable Long id) {
        log.debug("REST request to get Terminal : {}", id);
        Terminal terminal = terminalRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(terminal));
    }

    /**
     * DELETE  /terminals/:id : delete the "id" terminal.
     *
     * @param id the id of the terminal to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/terminals/{id}")
    @Timed
    public ResponseEntity<Void> deleteTerminal(@PathVariable Long id) {
        log.debug("REST request to delete Terminal : {}", id);
        terminalRepository.delete(id);
        terminalSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/terminals?query=:query : search for the terminal corresponding
     * to the query.
     *
     * @param query the query of the terminal search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/terminals")
    @Timed
    public ResponseEntity<List<Terminal>> searchTerminals(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Terminals for query {}", query);
        Page<Terminal> page = terminalSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/terminals");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}

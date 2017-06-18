package com.itgm.web.rest;

import com.itgm.ItgmApp;

import com.itgm.domain.Terminal;
import com.itgm.repository.TerminalRepository;
import com.itgm.repository.search.TerminalSearchRepository;
import com.itgm.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TerminalResource REST controller.
 *
 * @see TerminalResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItgmApp.class)
public class TerminalResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private TerminalRepository terminalRepository;

    @Autowired
    private TerminalSearchRepository terminalSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTerminalMockMvc;

    private Terminal terminal;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TerminalResource terminalResource = new TerminalResource(terminalRepository, terminalSearchRepository);
        this.restTerminalMockMvc = MockMvcBuilders.standaloneSetup(terminalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Terminal createEntity(EntityManager em) {
        Terminal terminal = new Terminal()
            .nome(DEFAULT_NOME)
            .url(DEFAULT_URL)
            .status(DEFAULT_STATUS);
        return terminal;
    }

    @Before
    public void initTest() {
        terminalSearchRepository.deleteAll();
        terminal = createEntity(em);
    }

    @Test
    @Transactional
    public void createTerminal() throws Exception {
        int databaseSizeBeforeCreate = terminalRepository.findAll().size();

        // Create the Terminal
        restTerminalMockMvc.perform(post("/api/terminals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(terminal)))
            .andExpect(status().isCreated());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeCreate + 1);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testTerminal.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testTerminal.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Terminal in Elasticsearch
        Terminal terminalEs = terminalSearchRepository.findOne(testTerminal.getId());
        assertThat(terminalEs).isEqualToComparingFieldByField(testTerminal);
    }

    @Test
    @Transactional
    public void createTerminalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = terminalRepository.findAll().size();

        // Create the Terminal with an existing ID
        terminal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTerminalMockMvc.perform(post("/api/terminals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(terminal)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTerminals() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get all the terminalList
        restTerminalMockMvc.perform(get("/api/terminals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);

        // Get the terminal
        restTerminalMockMvc.perform(get("/api/terminals/{id}", terminal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(terminal.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingTerminal() throws Exception {
        // Get the terminal
        restTerminalMockMvc.perform(get("/api/terminals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);
        terminalSearchRepository.save(terminal);
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Update the terminal
        Terminal updatedTerminal = terminalRepository.findOne(terminal.getId());
        updatedTerminal
            .nome(UPDATED_NOME)
            .url(UPDATED_URL)
            .status(UPDATED_STATUS);

        restTerminalMockMvc.perform(put("/api/terminals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTerminal)))
            .andExpect(status().isOk());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate);
        Terminal testTerminal = terminalList.get(terminalList.size() - 1);
        assertThat(testTerminal.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testTerminal.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testTerminal.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Terminal in Elasticsearch
        Terminal terminalEs = terminalSearchRepository.findOne(testTerminal.getId());
        assertThat(terminalEs).isEqualToComparingFieldByField(testTerminal);
    }

    @Test
    @Transactional
    public void updateNonExistingTerminal() throws Exception {
        int databaseSizeBeforeUpdate = terminalRepository.findAll().size();

        // Create the Terminal

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTerminalMockMvc.perform(put("/api/terminals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(terminal)))
            .andExpect(status().isCreated());

        // Validate the Terminal in the database
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);
        terminalSearchRepository.save(terminal);
        int databaseSizeBeforeDelete = terminalRepository.findAll().size();

        // Get the terminal
        restTerminalMockMvc.perform(delete("/api/terminals/{id}", terminal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean terminalExistsInEs = terminalSearchRepository.exists(terminal.getId());
        assertThat(terminalExistsInEs).isFalse();

        // Validate the database is empty
        List<Terminal> terminalList = terminalRepository.findAll();
        assertThat(terminalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTerminal() throws Exception {
        // Initialize the database
        terminalRepository.saveAndFlush(terminal);
        terminalSearchRepository.save(terminal);

        // Search the terminal
        restTerminalMockMvc.perform(get("/api/_search/terminals?query=id:" + terminal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(terminal.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Terminal.class);
    }
}

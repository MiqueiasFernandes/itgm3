package com.itgm.web.rest;

import com.itgm.ItgmApp;

import com.itgm.domain.Cenario;
import com.itgm.repository.CenarioRepository;
import com.itgm.repository.search.CenarioSearchRepository;
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
 * Test class for the CenarioResource REST controller.
 *
 * @see CenarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItgmApp.class)
public class CenarioResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CAMINHO = "AAAAAAAAAA";
    private static final String UPDATED_CAMINHO = "BBBBBBBBBB";

    @Autowired
    private CenarioRepository cenarioRepository;

    @Autowired
    private CenarioSearchRepository cenarioSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCenarioMockMvc;

    private Cenario cenario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CenarioResource cenarioResource = new CenarioResource(cenarioRepository, cenarioSearchRepository);
        this.restCenarioMockMvc = MockMvcBuilders.standaloneSetup(cenarioResource)
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
    public static Cenario createEntity(EntityManager em) {
        Cenario cenario = new Cenario()
            .nome(DEFAULT_NOME)
            .caminho(DEFAULT_CAMINHO);
        return cenario;
    }

    @Before
    public void initTest() {
        cenarioSearchRepository.deleteAll();
        cenario = createEntity(em);
    }

    @Test
    @Transactional
    public void createCenario() throws Exception {
        int databaseSizeBeforeCreate = cenarioRepository.findAll().size();

        // Create the Cenario
        restCenarioMockMvc.perform(post("/api/cenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cenario)))
            .andExpect(status().isCreated());

        // Validate the Cenario in the database
        List<Cenario> cenarioList = cenarioRepository.findAll();
        assertThat(cenarioList).hasSize(databaseSizeBeforeCreate + 1);
        Cenario testCenario = cenarioList.get(cenarioList.size() - 1);
        assertThat(testCenario.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCenario.getCaminho()).isEqualTo(DEFAULT_CAMINHO);

        // Validate the Cenario in Elasticsearch
        Cenario cenarioEs = cenarioSearchRepository.findOne(testCenario.getId());
        assertThat(cenarioEs).isEqualToComparingFieldByField(testCenario);
    }

    @Test
    @Transactional
    public void createCenarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cenarioRepository.findAll().size();

        // Create the Cenario with an existing ID
        cenario.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCenarioMockMvc.perform(post("/api/cenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cenario)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Cenario> cenarioList = cenarioRepository.findAll();
        assertThat(cenarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCenarios() throws Exception {
        // Initialize the database
        cenarioRepository.saveAndFlush(cenario);

        // Get all the cenarioList
        restCenarioMockMvc.perform(get("/api/cenarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cenario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].caminho").value(hasItem(DEFAULT_CAMINHO.toString())));
    }

    @Test
    @Transactional
    public void getCenario() throws Exception {
        // Initialize the database
        cenarioRepository.saveAndFlush(cenario);

        // Get the cenario
        restCenarioMockMvc.perform(get("/api/cenarios/{id}", cenario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cenario.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.caminho").value(DEFAULT_CAMINHO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCenario() throws Exception {
        // Get the cenario
        restCenarioMockMvc.perform(get("/api/cenarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCenario() throws Exception {
        // Initialize the database
        cenarioRepository.saveAndFlush(cenario);
        cenarioSearchRepository.save(cenario);
        int databaseSizeBeforeUpdate = cenarioRepository.findAll().size();

        // Update the cenario
        Cenario updatedCenario = cenarioRepository.findOne(cenario.getId());
        updatedCenario
            .nome(UPDATED_NOME)
            .caminho(UPDATED_CAMINHO);

        restCenarioMockMvc.perform(put("/api/cenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCenario)))
            .andExpect(status().isOk());

        // Validate the Cenario in the database
        List<Cenario> cenarioList = cenarioRepository.findAll();
        assertThat(cenarioList).hasSize(databaseSizeBeforeUpdate);
        Cenario testCenario = cenarioList.get(cenarioList.size() - 1);
        assertThat(testCenario.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCenario.getCaminho()).isEqualTo(UPDATED_CAMINHO);

        // Validate the Cenario in Elasticsearch
        Cenario cenarioEs = cenarioSearchRepository.findOne(testCenario.getId());
        assertThat(cenarioEs).isEqualToComparingFieldByField(testCenario);
    }

    @Test
    @Transactional
    public void updateNonExistingCenario() throws Exception {
        int databaseSizeBeforeUpdate = cenarioRepository.findAll().size();

        // Create the Cenario

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCenarioMockMvc.perform(put("/api/cenarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cenario)))
            .andExpect(status().isCreated());

        // Validate the Cenario in the database
        List<Cenario> cenarioList = cenarioRepository.findAll();
        assertThat(cenarioList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCenario() throws Exception {
        // Initialize the database
        cenarioRepository.saveAndFlush(cenario);
        cenarioSearchRepository.save(cenario);
        int databaseSizeBeforeDelete = cenarioRepository.findAll().size();

        // Get the cenario
        restCenarioMockMvc.perform(delete("/api/cenarios/{id}", cenario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cenarioExistsInEs = cenarioSearchRepository.exists(cenario.getId());
        assertThat(cenarioExistsInEs).isFalse();

        // Validate the database is empty
        List<Cenario> cenarioList = cenarioRepository.findAll();
        assertThat(cenarioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCenario() throws Exception {
        // Initialize the database
        cenarioRepository.saveAndFlush(cenario);
        cenarioSearchRepository.save(cenario);

        // Search the cenario
        restCenarioMockMvc.perform(get("/api/_search/cenarios?query=id:" + cenario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cenario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].caminho").value(hasItem(DEFAULT_CAMINHO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cenario.class);
    }
}

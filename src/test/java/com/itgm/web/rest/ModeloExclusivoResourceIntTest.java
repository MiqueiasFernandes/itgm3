package com.itgm.web.rest;

import com.itgm.ItgmApp;

import com.itgm.domain.ModeloExclusivo;
import com.itgm.repository.ModeloExclusivoRepository;
import com.itgm.repository.search.ModeloExclusivoSearchRepository;
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
 * Test class for the ModeloExclusivoResource REST controller.
 *
 * @see ModeloExclusivoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItgmApp.class)
public class ModeloExclusivoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_MAPEAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_MAPEAMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_PALPITE = "AAAAAAAAAA";
    private static final String UPDATED_PALPITE = "BBBBBBBBBB";

    @Autowired
    private ModeloExclusivoRepository modeloExclusivoRepository;

    @Autowired
    private ModeloExclusivoSearchRepository modeloExclusivoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restModeloExclusivoMockMvc;

    private ModeloExclusivo modeloExclusivo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ModeloExclusivoResource modeloExclusivoResource = new ModeloExclusivoResource(modeloExclusivoRepository, modeloExclusivoSearchRepository);
        this.restModeloExclusivoMockMvc = MockMvcBuilders.standaloneSetup(modeloExclusivoResource)
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
    public static ModeloExclusivo createEntity(EntityManager em) {
        ModeloExclusivo modeloExclusivo = new ModeloExclusivo()
            .nome(DEFAULT_NOME)
            .mapeamento(DEFAULT_MAPEAMENTO)
            .palpite(DEFAULT_PALPITE);
        return modeloExclusivo;
    }

    @Before
    public void initTest() {
        modeloExclusivoSearchRepository.deleteAll();
        modeloExclusivo = createEntity(em);
    }

    @Test
    @Transactional
    public void createModeloExclusivo() throws Exception {
        int databaseSizeBeforeCreate = modeloExclusivoRepository.findAll().size();

        // Create the ModeloExclusivo
        restModeloExclusivoMockMvc.perform(post("/api/modelo-exclusivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modeloExclusivo)))
            .andExpect(status().isCreated());

        // Validate the ModeloExclusivo in the database
        List<ModeloExclusivo> modeloExclusivoList = modeloExclusivoRepository.findAll();
        assertThat(modeloExclusivoList).hasSize(databaseSizeBeforeCreate + 1);
        ModeloExclusivo testModeloExclusivo = modeloExclusivoList.get(modeloExclusivoList.size() - 1);
        assertThat(testModeloExclusivo.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testModeloExclusivo.getMapeamento()).isEqualTo(DEFAULT_MAPEAMENTO);
        assertThat(testModeloExclusivo.getPalpite()).isEqualTo(DEFAULT_PALPITE);

        // Validate the ModeloExclusivo in Elasticsearch
        ModeloExclusivo modeloExclusivoEs = modeloExclusivoSearchRepository.findOne(testModeloExclusivo.getId());
        assertThat(modeloExclusivoEs).isEqualToComparingFieldByField(testModeloExclusivo);
    }

    @Test
    @Transactional
    public void createModeloExclusivoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modeloExclusivoRepository.findAll().size();

        // Create the ModeloExclusivo with an existing ID
        modeloExclusivo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModeloExclusivoMockMvc.perform(post("/api/modelo-exclusivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modeloExclusivo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<ModeloExclusivo> modeloExclusivoList = modeloExclusivoRepository.findAll();
        assertThat(modeloExclusivoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllModeloExclusivos() throws Exception {
        // Initialize the database
        modeloExclusivoRepository.saveAndFlush(modeloExclusivo);

        // Get all the modeloExclusivoList
        restModeloExclusivoMockMvc.perform(get("/api/modelo-exclusivos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modeloExclusivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].mapeamento").value(hasItem(DEFAULT_MAPEAMENTO.toString())))
            .andExpect(jsonPath("$.[*].palpite").value(hasItem(DEFAULT_PALPITE.toString())));
    }

    @Test
    @Transactional
    public void getModeloExclusivo() throws Exception {
        // Initialize the database
        modeloExclusivoRepository.saveAndFlush(modeloExclusivo);

        // Get the modeloExclusivo
        restModeloExclusivoMockMvc.perform(get("/api/modelo-exclusivos/{id}", modeloExclusivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(modeloExclusivo.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.mapeamento").value(DEFAULT_MAPEAMENTO.toString()))
            .andExpect(jsonPath("$.palpite").value(DEFAULT_PALPITE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingModeloExclusivo() throws Exception {
        // Get the modeloExclusivo
        restModeloExclusivoMockMvc.perform(get("/api/modelo-exclusivos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModeloExclusivo() throws Exception {
        // Initialize the database
        modeloExclusivoRepository.saveAndFlush(modeloExclusivo);
        modeloExclusivoSearchRepository.save(modeloExclusivo);
        int databaseSizeBeforeUpdate = modeloExclusivoRepository.findAll().size();

        // Update the modeloExclusivo
        ModeloExclusivo updatedModeloExclusivo = modeloExclusivoRepository.findOne(modeloExclusivo.getId());
        updatedModeloExclusivo
            .nome(UPDATED_NOME)
            .mapeamento(UPDATED_MAPEAMENTO)
            .palpite(UPDATED_PALPITE);

        restModeloExclusivoMockMvc.perform(put("/api/modelo-exclusivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModeloExclusivo)))
            .andExpect(status().isOk());

        // Validate the ModeloExclusivo in the database
        List<ModeloExclusivo> modeloExclusivoList = modeloExclusivoRepository.findAll();
        assertThat(modeloExclusivoList).hasSize(databaseSizeBeforeUpdate);
        ModeloExclusivo testModeloExclusivo = modeloExclusivoList.get(modeloExclusivoList.size() - 1);
        assertThat(testModeloExclusivo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testModeloExclusivo.getMapeamento()).isEqualTo(UPDATED_MAPEAMENTO);
        assertThat(testModeloExclusivo.getPalpite()).isEqualTo(UPDATED_PALPITE);

        // Validate the ModeloExclusivo in Elasticsearch
        ModeloExclusivo modeloExclusivoEs = modeloExclusivoSearchRepository.findOne(testModeloExclusivo.getId());
        assertThat(modeloExclusivoEs).isEqualToComparingFieldByField(testModeloExclusivo);
    }

    @Test
    @Transactional
    public void updateNonExistingModeloExclusivo() throws Exception {
        int databaseSizeBeforeUpdate = modeloExclusivoRepository.findAll().size();

        // Create the ModeloExclusivo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restModeloExclusivoMockMvc.perform(put("/api/modelo-exclusivos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modeloExclusivo)))
            .andExpect(status().isCreated());

        // Validate the ModeloExclusivo in the database
        List<ModeloExclusivo> modeloExclusivoList = modeloExclusivoRepository.findAll();
        assertThat(modeloExclusivoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteModeloExclusivo() throws Exception {
        // Initialize the database
        modeloExclusivoRepository.saveAndFlush(modeloExclusivo);
        modeloExclusivoSearchRepository.save(modeloExclusivo);
        int databaseSizeBeforeDelete = modeloExclusivoRepository.findAll().size();

        // Get the modeloExclusivo
        restModeloExclusivoMockMvc.perform(delete("/api/modelo-exclusivos/{id}", modeloExclusivo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean modeloExclusivoExistsInEs = modeloExclusivoSearchRepository.exists(modeloExclusivo.getId());
        assertThat(modeloExclusivoExistsInEs).isFalse();

        // Validate the database is empty
        List<ModeloExclusivo> modeloExclusivoList = modeloExclusivoRepository.findAll();
        assertThat(modeloExclusivoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchModeloExclusivo() throws Exception {
        // Initialize the database
        modeloExclusivoRepository.saveAndFlush(modeloExclusivo);
        modeloExclusivoSearchRepository.save(modeloExclusivo);

        // Search the modeloExclusivo
        restModeloExclusivoMockMvc.perform(get("/api/_search/modelo-exclusivos?query=id:" + modeloExclusivo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modeloExclusivo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].mapeamento").value(hasItem(DEFAULT_MAPEAMENTO.toString())))
            .andExpect(jsonPath("$.[*].palpite").value(hasItem(DEFAULT_PALPITE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModeloExclusivo.class);
    }
}

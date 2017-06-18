package com.itgm.web.rest;

import com.itgm.ItgmApp;

import com.itgm.domain.Modelo;
import com.itgm.repository.ModeloRepository;
import com.itgm.repository.search.ModeloSearchRepository;
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
 * Test class for the ModeloResource REST controller.
 *
 * @see ModeloResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItgmApp.class)
public class ModeloResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_COR = "AAAAAAAAAA";
    private static final String UPDATED_COR = "BBBBBBBBBB";

    private static final String DEFAULT_FORMULA = "AAAAAAAAAA";
    private static final String UPDATED_FORMULA = "BBBBBBBBBB";

    private static final String DEFAULT_FUNCAO = "AAAAAAAAAA";
    private static final String UPDATED_FUNCAO = "BBBBBBBBBB";

    private static final String DEFAULT_VARIAVEIS = "AAAAAAAAAA";
    private static final String UPDATED_VARIAVEIS = "BBBBBBBBBB";

    private static final String DEFAULT_PALPITE = "AAAAAAAAAA";
    private static final String UPDATED_PALPITE = "BBBBBBBBBB";

    private static final String DEFAULT_PARAMETROS = "AAAAAAAAAA";
    private static final String UPDATED_PARAMETROS = "BBBBBBBBBB";

    private static final String DEFAULT_REQUIRES = "AAAAAAAAAA";
    private static final String UPDATED_REQUIRES = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private ModeloSearchRepository modeloSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restModeloMockMvc;

    private Modelo modelo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ModeloResource modeloResource = new ModeloResource(modeloRepository, modeloSearchRepository);
        this.restModeloMockMvc = MockMvcBuilders.standaloneSetup(modeloResource)
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
    public static Modelo createEntity(EntityManager em) {
        Modelo modelo = new Modelo()
            .nome(DEFAULT_NOME)
            .cor(DEFAULT_COR)
            .formula(DEFAULT_FORMULA)
            .funcao(DEFAULT_FUNCAO)
            .variaveis(DEFAULT_VARIAVEIS)
            .palpite(DEFAULT_PALPITE)
            .parametros(DEFAULT_PARAMETROS)
            .requires(DEFAULT_REQUIRES)
            .codigo(DEFAULT_CODIGO);
        return modelo;
    }

    @Before
    public void initTest() {
        modeloSearchRepository.deleteAll();
        modelo = createEntity(em);
    }

    @Test
    @Transactional
    public void createModelo() throws Exception {
        int databaseSizeBeforeCreate = modeloRepository.findAll().size();

        // Create the Modelo
        restModeloMockMvc.perform(post("/api/modelos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelo)))
            .andExpect(status().isCreated());

        // Validate the Modelo in the database
        List<Modelo> modeloList = modeloRepository.findAll();
        assertThat(modeloList).hasSize(databaseSizeBeforeCreate + 1);
        Modelo testModelo = modeloList.get(modeloList.size() - 1);
        assertThat(testModelo.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testModelo.getCor()).isEqualTo(DEFAULT_COR);
        assertThat(testModelo.getFormula()).isEqualTo(DEFAULT_FORMULA);
        assertThat(testModelo.getFuncao()).isEqualTo(DEFAULT_FUNCAO);
        assertThat(testModelo.getVariaveis()).isEqualTo(DEFAULT_VARIAVEIS);
        assertThat(testModelo.getPalpite()).isEqualTo(DEFAULT_PALPITE);
        assertThat(testModelo.getParametros()).isEqualTo(DEFAULT_PARAMETROS);
        assertThat(testModelo.getRequires()).isEqualTo(DEFAULT_REQUIRES);
        assertThat(testModelo.getCodigo()).isEqualTo(DEFAULT_CODIGO);

        // Validate the Modelo in Elasticsearch
        Modelo modeloEs = modeloSearchRepository.findOne(testModelo.getId());
        assertThat(modeloEs).isEqualToComparingFieldByField(testModelo);
    }

    @Test
    @Transactional
    public void createModeloWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = modeloRepository.findAll().size();

        // Create the Modelo with an existing ID
        modelo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restModeloMockMvc.perform(post("/api/modelos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Modelo> modeloList = modeloRepository.findAll();
        assertThat(modeloList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllModelos() throws Exception {
        // Initialize the database
        modeloRepository.saveAndFlush(modelo);

        // Get all the modeloList
        restModeloMockMvc.perform(get("/api/modelos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modelo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].cor").value(hasItem(DEFAULT_COR.toString())))
            .andExpect(jsonPath("$.[*].formula").value(hasItem(DEFAULT_FORMULA.toString())))
            .andExpect(jsonPath("$.[*].funcao").value(hasItem(DEFAULT_FUNCAO.toString())))
            .andExpect(jsonPath("$.[*].variaveis").value(hasItem(DEFAULT_VARIAVEIS.toString())))
            .andExpect(jsonPath("$.[*].palpite").value(hasItem(DEFAULT_PALPITE.toString())))
            .andExpect(jsonPath("$.[*].parametros").value(hasItem(DEFAULT_PARAMETROS.toString())))
            .andExpect(jsonPath("$.[*].requires").value(hasItem(DEFAULT_REQUIRES.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())));
    }

    @Test
    @Transactional
    public void getModelo() throws Exception {
        // Initialize the database
        modeloRepository.saveAndFlush(modelo);

        // Get the modelo
        restModeloMockMvc.perform(get("/api/modelos/{id}", modelo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(modelo.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.cor").value(DEFAULT_COR.toString()))
            .andExpect(jsonPath("$.formula").value(DEFAULT_FORMULA.toString()))
            .andExpect(jsonPath("$.funcao").value(DEFAULT_FUNCAO.toString()))
            .andExpect(jsonPath("$.variaveis").value(DEFAULT_VARIAVEIS.toString()))
            .andExpect(jsonPath("$.palpite").value(DEFAULT_PALPITE.toString()))
            .andExpect(jsonPath("$.parametros").value(DEFAULT_PARAMETROS.toString()))
            .andExpect(jsonPath("$.requires").value(DEFAULT_REQUIRES.toString()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingModelo() throws Exception {
        // Get the modelo
        restModeloMockMvc.perform(get("/api/modelos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModelo() throws Exception {
        // Initialize the database
        modeloRepository.saveAndFlush(modelo);
        modeloSearchRepository.save(modelo);
        int databaseSizeBeforeUpdate = modeloRepository.findAll().size();

        // Update the modelo
        Modelo updatedModelo = modeloRepository.findOne(modelo.getId());
        updatedModelo
            .nome(UPDATED_NOME)
            .cor(UPDATED_COR)
            .formula(UPDATED_FORMULA)
            .funcao(UPDATED_FUNCAO)
            .variaveis(UPDATED_VARIAVEIS)
            .palpite(UPDATED_PALPITE)
            .parametros(UPDATED_PARAMETROS)
            .requires(UPDATED_REQUIRES)
            .codigo(UPDATED_CODIGO);

        restModeloMockMvc.perform(put("/api/modelos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedModelo)))
            .andExpect(status().isOk());

        // Validate the Modelo in the database
        List<Modelo> modeloList = modeloRepository.findAll();
        assertThat(modeloList).hasSize(databaseSizeBeforeUpdate);
        Modelo testModelo = modeloList.get(modeloList.size() - 1);
        assertThat(testModelo.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testModelo.getCor()).isEqualTo(UPDATED_COR);
        assertThat(testModelo.getFormula()).isEqualTo(UPDATED_FORMULA);
        assertThat(testModelo.getFuncao()).isEqualTo(UPDATED_FUNCAO);
        assertThat(testModelo.getVariaveis()).isEqualTo(UPDATED_VARIAVEIS);
        assertThat(testModelo.getPalpite()).isEqualTo(UPDATED_PALPITE);
        assertThat(testModelo.getParametros()).isEqualTo(UPDATED_PARAMETROS);
        assertThat(testModelo.getRequires()).isEqualTo(UPDATED_REQUIRES);
        assertThat(testModelo.getCodigo()).isEqualTo(UPDATED_CODIGO);

        // Validate the Modelo in Elasticsearch
        Modelo modeloEs = modeloSearchRepository.findOne(testModelo.getId());
        assertThat(modeloEs).isEqualToComparingFieldByField(testModelo);
    }

    @Test
    @Transactional
    public void updateNonExistingModelo() throws Exception {
        int databaseSizeBeforeUpdate = modeloRepository.findAll().size();

        // Create the Modelo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restModeloMockMvc.perform(put("/api/modelos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(modelo)))
            .andExpect(status().isCreated());

        // Validate the Modelo in the database
        List<Modelo> modeloList = modeloRepository.findAll();
        assertThat(modeloList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteModelo() throws Exception {
        // Initialize the database
        modeloRepository.saveAndFlush(modelo);
        modeloSearchRepository.save(modelo);
        int databaseSizeBeforeDelete = modeloRepository.findAll().size();

        // Get the modelo
        restModeloMockMvc.perform(delete("/api/modelos/{id}", modelo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean modeloExistsInEs = modeloSearchRepository.exists(modelo.getId());
        assertThat(modeloExistsInEs).isFalse();

        // Validate the database is empty
        List<Modelo> modeloList = modeloRepository.findAll();
        assertThat(modeloList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchModelo() throws Exception {
        // Initialize the database
        modeloRepository.saveAndFlush(modelo);
        modeloSearchRepository.save(modelo);

        // Search the modelo
        restModeloMockMvc.perform(get("/api/_search/modelos?query=id:" + modelo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modelo.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].cor").value(hasItem(DEFAULT_COR.toString())))
            .andExpect(jsonPath("$.[*].formula").value(hasItem(DEFAULT_FORMULA.toString())))
            .andExpect(jsonPath("$.[*].funcao").value(hasItem(DEFAULT_FUNCAO.toString())))
            .andExpect(jsonPath("$.[*].variaveis").value(hasItem(DEFAULT_VARIAVEIS.toString())))
            .andExpect(jsonPath("$.[*].palpite").value(hasItem(DEFAULT_PALPITE.toString())))
            .andExpect(jsonPath("$.[*].parametros").value(hasItem(DEFAULT_PARAMETROS.toString())))
            .andExpect(jsonPath("$.[*].requires").value(hasItem(DEFAULT_REQUIRES.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Modelo.class);
    }
}

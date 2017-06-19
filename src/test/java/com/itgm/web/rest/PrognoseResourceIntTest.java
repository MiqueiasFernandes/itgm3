package com.itgm.web.rest;

import com.itgm.ItgmApp;

import com.itgm.domain.Prognose;
import com.itgm.repository.PrognoseRepository;
import com.itgm.repository.search.PrognoseSearchRepository;
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
 * Test class for the PrognoseResource REST controller.
 *
 * @see PrognoseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItgmApp.class)
public class PrognoseResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CAMINHO = "AAAAAAAAAA";
    private static final String UPDATED_CAMINHO = "BBBBBBBBBB";

    private static final String DEFAULT_MAPEAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_MAPEAMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_SALVAR = "AAAAAAAAAA";
    private static final String UPDATED_SALVAR = "BBBBBBBBBB";

    private static final String DEFAULT_GRAFICOS = "AAAAAAAAAA";
    private static final String UPDATED_GRAFICOS = "BBBBBBBBBB";

    private static final String DEFAULT_ESTATISTICAS = "AAAAAAAAAA";
    private static final String UPDATED_ESTATISTICAS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FORCEPREDICT = false;
    private static final Boolean UPDATED_FORCEPREDICT = true;

    private static final String DEFAULT_DIVIDIR = "AAAAAAAAAA";
    private static final String UPDATED_DIVIDIR = "BBBBBBBBBB";

    private static final String DEFAULT_AGRUPAR = "AAAAAAAAAA";
    private static final String UPDATED_AGRUPAR = "BBBBBBBBBB";

    private static final Float DEFAULT_TREINO = 1F;
    private static final Float UPDATED_TREINO = 2F;

    private static final String DEFAULT_FNCALCULAVOLUME = "AAAAAAAAAA";
    private static final String UPDATED_FNCALCULAVOLUME = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private PrognoseRepository prognoseRepository;

    @Autowired
    private PrognoseSearchRepository prognoseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPrognoseMockMvc;

    private Prognose prognose;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrognoseResource prognoseResource = new PrognoseResource(prognoseRepository, prognoseSearchRepository);
        this.restPrognoseMockMvc = MockMvcBuilders.standaloneSetup(prognoseResource)
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
    public static Prognose createEntity(EntityManager em) {
        Prognose prognose = new Prognose()
            .nome(DEFAULT_NOME)
            .caminho(DEFAULT_CAMINHO)
            .mapeamento(DEFAULT_MAPEAMENTO)
            .salvar(DEFAULT_SALVAR)
            .graficos(DEFAULT_GRAFICOS)
            .estatisticas(DEFAULT_ESTATISTICAS)
            .forcepredict(DEFAULT_FORCEPREDICT)
            .dividir(DEFAULT_DIVIDIR)
            .agrupar(DEFAULT_AGRUPAR)
            .treino(DEFAULT_TREINO)
            .fncalculavolume(DEFAULT_FNCALCULAVOLUME)
            .status(DEFAULT_STATUS);
        return prognose;
    }

    @Before
    public void initTest() {
        prognoseSearchRepository.deleteAll();
        prognose = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrognose() throws Exception {
        int databaseSizeBeforeCreate = prognoseRepository.findAll().size();

        // Create the Prognose
        restPrognoseMockMvc.perform(post("/api/prognoses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prognose)))
            .andExpect(status().isCreated());

        // Validate the Prognose in the database
        List<Prognose> prognoseList = prognoseRepository.findAll();
        assertThat(prognoseList).hasSize(databaseSizeBeforeCreate + 1);
        Prognose testPrognose = prognoseList.get(prognoseList.size() - 1);
        assertThat(testPrognose.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testPrognose.getCaminho()).isEqualTo(DEFAULT_CAMINHO);
        assertThat(testPrognose.getMapeamento()).isEqualTo(DEFAULT_MAPEAMENTO);
        assertThat(testPrognose.getSalvar()).isEqualTo(DEFAULT_SALVAR);
        assertThat(testPrognose.getGraficos()).isEqualTo(DEFAULT_GRAFICOS);
        assertThat(testPrognose.getEstatisticas()).isEqualTo(DEFAULT_ESTATISTICAS);
        assertThat(testPrognose.isForcepredict()).isEqualTo(DEFAULT_FORCEPREDICT);
        assertThat(testPrognose.getDividir()).isEqualTo(DEFAULT_DIVIDIR);
        assertThat(testPrognose.getAgrupar()).isEqualTo(DEFAULT_AGRUPAR);
        assertThat(testPrognose.getTreino()).isEqualTo(DEFAULT_TREINO);
        assertThat(testPrognose.getFncalculavolume()).isEqualTo(DEFAULT_FNCALCULAVOLUME);
        assertThat(testPrognose.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Prognose in Elasticsearch
        Prognose prognoseEs = prognoseSearchRepository.findOne(testPrognose.getId());
        assertThat(prognoseEs).isEqualToComparingFieldByField(testPrognose);
    }

    @Test
    @Transactional
    public void createPrognoseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = prognoseRepository.findAll().size();

        // Create the Prognose with an existing ID
        prognose.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrognoseMockMvc.perform(post("/api/prognoses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prognose)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Prognose> prognoseList = prognoseRepository.findAll();
        assertThat(prognoseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPrognoses() throws Exception {
        // Initialize the database
        prognoseRepository.saveAndFlush(prognose);

        // Get all the prognoseList
        restPrognoseMockMvc.perform(get("/api/prognoses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prognose.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].caminho").value(hasItem(DEFAULT_CAMINHO.toString())))
            .andExpect(jsonPath("$.[*].mapeamento").value(hasItem(DEFAULT_MAPEAMENTO.toString())))
            .andExpect(jsonPath("$.[*].salvar").value(hasItem(DEFAULT_SALVAR.toString())))
            .andExpect(jsonPath("$.[*].graficos").value(hasItem(DEFAULT_GRAFICOS.toString())))
            .andExpect(jsonPath("$.[*].estatisticas").value(hasItem(DEFAULT_ESTATISTICAS.toString())))
            .andExpect(jsonPath("$.[*].forcepredict").value(hasItem(DEFAULT_FORCEPREDICT.booleanValue())))
            .andExpect(jsonPath("$.[*].dividir").value(hasItem(DEFAULT_DIVIDIR.toString())))
            .andExpect(jsonPath("$.[*].agrupar").value(hasItem(DEFAULT_AGRUPAR.toString())))
            .andExpect(jsonPath("$.[*].treino").value(hasItem(DEFAULT_TREINO.doubleValue())))
            .andExpect(jsonPath("$.[*].fncalculavolume").value(hasItem(DEFAULT_FNCALCULAVOLUME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getPrognose() throws Exception {
        // Initialize the database
        prognoseRepository.saveAndFlush(prognose);

        // Get the prognose
        restPrognoseMockMvc.perform(get("/api/prognoses/{id}", prognose.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(prognose.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.caminho").value(DEFAULT_CAMINHO.toString()))
            .andExpect(jsonPath("$.mapeamento").value(DEFAULT_MAPEAMENTO.toString()))
            .andExpect(jsonPath("$.salvar").value(DEFAULT_SALVAR.toString()))
            .andExpect(jsonPath("$.graficos").value(DEFAULT_GRAFICOS.toString()))
            .andExpect(jsonPath("$.estatisticas").value(DEFAULT_ESTATISTICAS.toString()))
            .andExpect(jsonPath("$.forcepredict").value(DEFAULT_FORCEPREDICT.booleanValue()))
            .andExpect(jsonPath("$.dividir").value(DEFAULT_DIVIDIR.toString()))
            .andExpect(jsonPath("$.agrupar").value(DEFAULT_AGRUPAR.toString()))
            .andExpect(jsonPath("$.treino").value(DEFAULT_TREINO.doubleValue()))
            .andExpect(jsonPath("$.fncalculavolume").value(DEFAULT_FNCALCULAVOLUME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingPrognose() throws Exception {
        // Get the prognose
        restPrognoseMockMvc.perform(get("/api/prognoses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrognose() throws Exception {
        // Initialize the database
        prognoseRepository.saveAndFlush(prognose);
        prognoseSearchRepository.save(prognose);
        int databaseSizeBeforeUpdate = prognoseRepository.findAll().size();

        // Update the prognose
        Prognose updatedPrognose = prognoseRepository.findOne(prognose.getId());
        updatedPrognose
            .nome(UPDATED_NOME)
            .caminho(UPDATED_CAMINHO)
            .mapeamento(UPDATED_MAPEAMENTO)
            .salvar(UPDATED_SALVAR)
            .graficos(UPDATED_GRAFICOS)
            .estatisticas(UPDATED_ESTATISTICAS)
            .forcepredict(UPDATED_FORCEPREDICT)
            .dividir(UPDATED_DIVIDIR)
            .agrupar(UPDATED_AGRUPAR)
            .treino(UPDATED_TREINO)
            .fncalculavolume(UPDATED_FNCALCULAVOLUME)
            .status(UPDATED_STATUS);

        restPrognoseMockMvc.perform(put("/api/prognoses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPrognose)))
            .andExpect(status().isOk());

        // Validate the Prognose in the database
        List<Prognose> prognoseList = prognoseRepository.findAll();
        assertThat(prognoseList).hasSize(databaseSizeBeforeUpdate);
        Prognose testPrognose = prognoseList.get(prognoseList.size() - 1);
        assertThat(testPrognose.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testPrognose.getCaminho()).isEqualTo(UPDATED_CAMINHO);
        assertThat(testPrognose.getMapeamento()).isEqualTo(UPDATED_MAPEAMENTO);
        assertThat(testPrognose.getSalvar()).isEqualTo(UPDATED_SALVAR);
        assertThat(testPrognose.getGraficos()).isEqualTo(UPDATED_GRAFICOS);
        assertThat(testPrognose.getEstatisticas()).isEqualTo(UPDATED_ESTATISTICAS);
        assertThat(testPrognose.isForcepredict()).isEqualTo(UPDATED_FORCEPREDICT);
        assertThat(testPrognose.getDividir()).isEqualTo(UPDATED_DIVIDIR);
        assertThat(testPrognose.getAgrupar()).isEqualTo(UPDATED_AGRUPAR);
        assertThat(testPrognose.getTreino()).isEqualTo(UPDATED_TREINO);
        assertThat(testPrognose.getFncalculavolume()).isEqualTo(UPDATED_FNCALCULAVOLUME);
        assertThat(testPrognose.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Prognose in Elasticsearch
        Prognose prognoseEs = prognoseSearchRepository.findOne(testPrognose.getId());
        assertThat(prognoseEs).isEqualToComparingFieldByField(testPrognose);
    }

    @Test
    @Transactional
    public void updateNonExistingPrognose() throws Exception {
        int databaseSizeBeforeUpdate = prognoseRepository.findAll().size();

        // Create the Prognose

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPrognoseMockMvc.perform(put("/api/prognoses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(prognose)))
            .andExpect(status().isCreated());

        // Validate the Prognose in the database
        List<Prognose> prognoseList = prognoseRepository.findAll();
        assertThat(prognoseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePrognose() throws Exception {
        // Initialize the database
        prognoseRepository.saveAndFlush(prognose);
        prognoseSearchRepository.save(prognose);
        int databaseSizeBeforeDelete = prognoseRepository.findAll().size();

        // Get the prognose
        restPrognoseMockMvc.perform(delete("/api/prognoses/{id}", prognose.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean prognoseExistsInEs = prognoseSearchRepository.exists(prognose.getId());
        assertThat(prognoseExistsInEs).isFalse();

        // Validate the database is empty
        List<Prognose> prognoseList = prognoseRepository.findAll();
        assertThat(prognoseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPrognose() throws Exception {
        // Initialize the database
        prognoseRepository.saveAndFlush(prognose);
        prognoseSearchRepository.save(prognose);

        // Search the prognose
        restPrognoseMockMvc.perform(get("/api/_search/prognoses?query=id:" + prognose.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(prognose.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].caminho").value(hasItem(DEFAULT_CAMINHO.toString())))
            .andExpect(jsonPath("$.[*].mapeamento").value(hasItem(DEFAULT_MAPEAMENTO.toString())))
            .andExpect(jsonPath("$.[*].salvar").value(hasItem(DEFAULT_SALVAR.toString())))
            .andExpect(jsonPath("$.[*].graficos").value(hasItem(DEFAULT_GRAFICOS.toString())))
            .andExpect(jsonPath("$.[*].estatisticas").value(hasItem(DEFAULT_ESTATISTICAS.toString())))
            .andExpect(jsonPath("$.[*].forcepredict").value(hasItem(DEFAULT_FORCEPREDICT.booleanValue())))
            .andExpect(jsonPath("$.[*].dividir").value(hasItem(DEFAULT_DIVIDIR.toString())))
            .andExpect(jsonPath("$.[*].agrupar").value(hasItem(DEFAULT_AGRUPAR.toString())))
            .andExpect(jsonPath("$.[*].treino").value(hasItem(DEFAULT_TREINO.doubleValue())))
            .andExpect(jsonPath("$.[*].fncalculavolume").value(hasItem(DEFAULT_FNCALCULAVOLUME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prognose.class);
    }
}

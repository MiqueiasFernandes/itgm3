package com.itgm.web.rest;

import com.itgm.ItgmApp;

import com.itgm.domain.Projeto;
import com.itgm.repository.ProjetoRepository;
import com.itgm.repository.search.ProjetoSearchRepository;
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
 * Test class for the ProjetoResource REST controller.
 *
 * @see ProjetoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItgmApp.class)
public class ProjetoResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CAMINHO = "AAAAAAAAAA";
    private static final String UPDATED_CAMINHO = "BBBBBBBBBB";

    private static final String DEFAULT_ARQUIVOS = "AAAAAAAAAA";
    private static final String UPDATED_ARQUIVOS = "BBBBBBBBBB";

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ProjetoSearchRepository projetoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjetoMockMvc;

    private Projeto projeto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProjetoResource projetoResource = new ProjetoResource(projetoRepository, projetoSearchRepository);
        this.restProjetoMockMvc = MockMvcBuilders.standaloneSetup(projetoResource)
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
    public static Projeto createEntity(EntityManager em) {
        Projeto projeto = new Projeto()
            .nome(DEFAULT_NOME)
            .caminho(DEFAULT_CAMINHO)
            .arquivos(DEFAULT_ARQUIVOS);
        return projeto;
    }

    @Before
    public void initTest() {
        projetoSearchRepository.deleteAll();
        projeto = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjeto() throws Exception {
        int databaseSizeBeforeCreate = projetoRepository.findAll().size();

        // Create the Projeto
        restProjetoMockMvc.perform(post("/api/projetos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isCreated());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeCreate + 1);
        Projeto testProjeto = projetoList.get(projetoList.size() - 1);
        assertThat(testProjeto.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testProjeto.getCaminho()).isEqualTo(DEFAULT_CAMINHO);
        assertThat(testProjeto.getArquivos()).isEqualTo(DEFAULT_ARQUIVOS);

        // Validate the Projeto in Elasticsearch
        Projeto projetoEs = projetoSearchRepository.findOne(testProjeto.getId());
        assertThat(projetoEs).isEqualToComparingFieldByField(testProjeto);
    }

    @Test
    @Transactional
    public void createProjetoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projetoRepository.findAll().size();

        // Create the Projeto with an existing ID
        projeto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjetoMockMvc.perform(post("/api/projetos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProjetos() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get all the projetoList
        restProjetoMockMvc.perform(get("/api/projetos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].caminho").value(hasItem(DEFAULT_CAMINHO.toString())))
            .andExpect(jsonPath("$.[*].arquivos").value(hasItem(DEFAULT_ARQUIVOS.toString())));
    }

    @Test
    @Transactional
    public void getProjeto() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);

        // Get the projeto
        restProjetoMockMvc.perform(get("/api/projetos/{id}", projeto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projeto.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.caminho").value(DEFAULT_CAMINHO.toString()))
            .andExpect(jsonPath("$.arquivos").value(DEFAULT_ARQUIVOS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjeto() throws Exception {
        // Get the projeto
        restProjetoMockMvc.perform(get("/api/projetos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjeto() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);
        projetoSearchRepository.save(projeto);
        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();

        // Update the projeto
        Projeto updatedProjeto = projetoRepository.findOne(projeto.getId());
        updatedProjeto
            .nome(UPDATED_NOME)
            .caminho(UPDATED_CAMINHO)
            .arquivos(UPDATED_ARQUIVOS);

        restProjetoMockMvc.perform(put("/api/projetos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjeto)))
            .andExpect(status().isOk());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate);
        Projeto testProjeto = projetoList.get(projetoList.size() - 1);
        assertThat(testProjeto.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testProjeto.getCaminho()).isEqualTo(UPDATED_CAMINHO);
        assertThat(testProjeto.getArquivos()).isEqualTo(UPDATED_ARQUIVOS);

        // Validate the Projeto in Elasticsearch
        Projeto projetoEs = projetoSearchRepository.findOne(testProjeto.getId());
        assertThat(projetoEs).isEqualToComparingFieldByField(testProjeto);
    }

    @Test
    @Transactional
    public void updateNonExistingProjeto() throws Exception {
        int databaseSizeBeforeUpdate = projetoRepository.findAll().size();

        // Create the Projeto

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjetoMockMvc.perform(put("/api/projetos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projeto)))
            .andExpect(status().isCreated());

        // Validate the Projeto in the database
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjeto() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);
        projetoSearchRepository.save(projeto);
        int databaseSizeBeforeDelete = projetoRepository.findAll().size();

        // Get the projeto
        restProjetoMockMvc.perform(delete("/api/projetos/{id}", projeto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean projetoExistsInEs = projetoSearchRepository.exists(projeto.getId());
        assertThat(projetoExistsInEs).isFalse();

        // Validate the database is empty
        List<Projeto> projetoList = projetoRepository.findAll();
        assertThat(projetoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProjeto() throws Exception {
        // Initialize the database
        projetoRepository.saveAndFlush(projeto);
        projetoSearchRepository.save(projeto);

        // Search the projeto
        restProjetoMockMvc.perform(get("/api/_search/projetos?query=id:" + projeto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projeto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].caminho").value(hasItem(DEFAULT_CAMINHO.toString())))
            .andExpect(jsonPath("$.[*].arquivos").value(hasItem(DEFAULT_ARQUIVOS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Projeto.class);
    }
}

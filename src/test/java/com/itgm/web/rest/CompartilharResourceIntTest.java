package com.itgm.web.rest;

import com.itgm.ItgmApp;

import com.itgm.domain.Compartilhar;
import com.itgm.repository.CompartilharRepository;
import com.itgm.repository.search.CompartilharSearchRepository;
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
 * Test class for the CompartilharResource REST controller.
 *
 * @see CompartilharResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItgmApp.class)
public class CompartilharResourceIntTest {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_MENSAGEM = "AAAAAAAAAA";
    private static final String UPDATED_MENSAGEM = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private CompartilharRepository compartilharRepository;

    @Autowired
    private CompartilharSearchRepository compartilharSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompartilharMockMvc;

    private Compartilhar compartilhar;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompartilharResource compartilharResource = new CompartilharResource(compartilharRepository, compartilharSearchRepository);
        this.restCompartilharMockMvc = MockMvcBuilders.standaloneSetup(compartilharResource)
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
    public static Compartilhar createEntity(EntityManager em) {
        Compartilhar compartilhar = new Compartilhar()
            .tipo(DEFAULT_TIPO)
            .mensagem(DEFAULT_MENSAGEM)
            .codigo(DEFAULT_CODIGO)
            .nome(DEFAULT_NOME)
            .status(DEFAULT_STATUS);
        return compartilhar;
    }

    @Before
    public void initTest() {
        compartilharSearchRepository.deleteAll();
        compartilhar = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompartilhar() throws Exception {
        int databaseSizeBeforeCreate = compartilharRepository.findAll().size();

        // Create the Compartilhar
        restCompartilharMockMvc.perform(post("/api/compartilhars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compartilhar)))
            .andExpect(status().isCreated());

        // Validate the Compartilhar in the database
        List<Compartilhar> compartilharList = compartilharRepository.findAll();
        assertThat(compartilharList).hasSize(databaseSizeBeforeCreate + 1);
        Compartilhar testCompartilhar = compartilharList.get(compartilharList.size() - 1);
        assertThat(testCompartilhar.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testCompartilhar.getMensagem()).isEqualTo(DEFAULT_MENSAGEM);
        assertThat(testCompartilhar.getCodigo()).isEqualTo(DEFAULT_CODIGO);
        assertThat(testCompartilhar.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCompartilhar.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the Compartilhar in Elasticsearch
        Compartilhar compartilharEs = compartilharSearchRepository.findOne(testCompartilhar.getId());
        assertThat(compartilharEs).isEqualToComparingFieldByField(testCompartilhar);
    }

    @Test
    @Transactional
    public void createCompartilharWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = compartilharRepository.findAll().size();

        // Create the Compartilhar with an existing ID
        compartilhar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompartilharMockMvc.perform(post("/api/compartilhars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compartilhar)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Compartilhar> compartilharList = compartilharRepository.findAll();
        assertThat(compartilharList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCompartilhars() throws Exception {
        // Initialize the database
        compartilharRepository.saveAndFlush(compartilhar);

        // Get all the compartilharList
        restCompartilharMockMvc.perform(get("/api/compartilhars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compartilhar.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].mensagem").value(hasItem(DEFAULT_MENSAGEM.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getCompartilhar() throws Exception {
        // Initialize the database
        compartilharRepository.saveAndFlush(compartilhar);

        // Get the compartilhar
        restCompartilharMockMvc.perform(get("/api/compartilhars/{id}", compartilhar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(compartilhar.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.mensagem").value(DEFAULT_MENSAGEM.toString()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingCompartilhar() throws Exception {
        // Get the compartilhar
        restCompartilharMockMvc.perform(get("/api/compartilhars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompartilhar() throws Exception {
        // Initialize the database
        compartilharRepository.saveAndFlush(compartilhar);
        compartilharSearchRepository.save(compartilhar);
        int databaseSizeBeforeUpdate = compartilharRepository.findAll().size();

        // Update the compartilhar
        Compartilhar updatedCompartilhar = compartilharRepository.findOne(compartilhar.getId());
        updatedCompartilhar
            .tipo(UPDATED_TIPO)
            .mensagem(UPDATED_MENSAGEM)
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .status(UPDATED_STATUS);

        restCompartilharMockMvc.perform(put("/api/compartilhars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompartilhar)))
            .andExpect(status().isOk());

        // Validate the Compartilhar in the database
        List<Compartilhar> compartilharList = compartilharRepository.findAll();
        assertThat(compartilharList).hasSize(databaseSizeBeforeUpdate);
        Compartilhar testCompartilhar = compartilharList.get(compartilharList.size() - 1);
        assertThat(testCompartilhar.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCompartilhar.getMensagem()).isEqualTo(UPDATED_MENSAGEM);
        assertThat(testCompartilhar.getCodigo()).isEqualTo(UPDATED_CODIGO);
        assertThat(testCompartilhar.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCompartilhar.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the Compartilhar in Elasticsearch
        Compartilhar compartilharEs = compartilharSearchRepository.findOne(testCompartilhar.getId());
        assertThat(compartilharEs).isEqualToComparingFieldByField(testCompartilhar);
    }

    @Test
    @Transactional
    public void updateNonExistingCompartilhar() throws Exception {
        int databaseSizeBeforeUpdate = compartilharRepository.findAll().size();

        // Create the Compartilhar

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompartilharMockMvc.perform(put("/api/compartilhars")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(compartilhar)))
            .andExpect(status().isCreated());

        // Validate the Compartilhar in the database
        List<Compartilhar> compartilharList = compartilharRepository.findAll();
        assertThat(compartilharList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompartilhar() throws Exception {
        // Initialize the database
        compartilharRepository.saveAndFlush(compartilhar);
        compartilharSearchRepository.save(compartilhar);
        int databaseSizeBeforeDelete = compartilharRepository.findAll().size();

        // Get the compartilhar
        restCompartilharMockMvc.perform(delete("/api/compartilhars/{id}", compartilhar.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean compartilharExistsInEs = compartilharSearchRepository.exists(compartilhar.getId());
        assertThat(compartilharExistsInEs).isFalse();

        // Validate the database is empty
        List<Compartilhar> compartilharList = compartilharRepository.findAll();
        assertThat(compartilharList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompartilhar() throws Exception {
        // Initialize the database
        compartilharRepository.saveAndFlush(compartilhar);
        compartilharSearchRepository.save(compartilhar);

        // Search the compartilhar
        restCompartilharMockMvc.perform(get("/api/_search/compartilhars?query=id:" + compartilhar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compartilhar.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].mensagem").value(hasItem(DEFAULT_MENSAGEM.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compartilhar.class);
    }
}

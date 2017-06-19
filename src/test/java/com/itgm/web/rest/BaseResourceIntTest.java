package com.itgm.web.rest;

import com.itgm.ItgmApp;

import com.itgm.domain.Base;
import com.itgm.repository.BaseRepository;
import com.itgm.repository.search.BaseSearchRepository;
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
 * Test class for the BaseResource REST controller.
 *
 * @see BaseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItgmApp.class)
public class BaseResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCAL = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL = "BBBBBBBBBB";

    @Autowired
    private BaseRepository baseRepository;

    @Autowired
    private BaseSearchRepository baseSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBaseMockMvc;

    private Base base;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BaseResource baseResource = new BaseResource(baseRepository, baseSearchRepository);
        this.restBaseMockMvc = MockMvcBuilders.standaloneSetup(baseResource)
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
    public static Base createEntity(EntityManager em) {
        Base base = new Base()
            .nome(DEFAULT_NOME)
            .local(DEFAULT_LOCAL);
        return base;
    }

    @Before
    public void initTest() {
        baseSearchRepository.deleteAll();
        base = createEntity(em);
    }

    @Test
    @Transactional
    public void createBase() throws Exception {
        int databaseSizeBeforeCreate = baseRepository.findAll().size();

        // Create the Base
        restBaseMockMvc.perform(post("/api/bases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(base)))
            .andExpect(status().isCreated());

        // Validate the Base in the database
        List<Base> baseList = baseRepository.findAll();
        assertThat(baseList).hasSize(databaseSizeBeforeCreate + 1);
        Base testBase = baseList.get(baseList.size() - 1);
        assertThat(testBase.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testBase.getLocal()).isEqualTo(DEFAULT_LOCAL);

        // Validate the Base in Elasticsearch
        Base baseEs = baseSearchRepository.findOne(testBase.getId());
        assertThat(baseEs).isEqualToComparingFieldByField(testBase);
    }

    @Test
    @Transactional
    public void createBaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = baseRepository.findAll().size();

        // Create the Base with an existing ID
        base.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBaseMockMvc.perform(post("/api/bases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(base)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Base> baseList = baseRepository.findAll();
        assertThat(baseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBases() throws Exception {
        // Initialize the database
        baseRepository.saveAndFlush(base);

        // Get all the baseList
        restBaseMockMvc.perform(get("/api/bases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(base.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL.toString())));
    }

    @Test
    @Transactional
    public void getBase() throws Exception {
        // Initialize the database
        baseRepository.saveAndFlush(base);

        // Get the base
        restBaseMockMvc.perform(get("/api/bases/{id}", base.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(base.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.local").value(DEFAULT_LOCAL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBase() throws Exception {
        // Get the base
        restBaseMockMvc.perform(get("/api/bases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBase() throws Exception {
        // Initialize the database
        baseRepository.saveAndFlush(base);
        baseSearchRepository.save(base);
        int databaseSizeBeforeUpdate = baseRepository.findAll().size();

        // Update the base
        Base updatedBase = baseRepository.findOne(base.getId());
        updatedBase
            .nome(UPDATED_NOME)
            .local(UPDATED_LOCAL);

        restBaseMockMvc.perform(put("/api/bases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBase)))
            .andExpect(status().isOk());

        // Validate the Base in the database
        List<Base> baseList = baseRepository.findAll();
        assertThat(baseList).hasSize(databaseSizeBeforeUpdate);
        Base testBase = baseList.get(baseList.size() - 1);
        assertThat(testBase.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testBase.getLocal()).isEqualTo(UPDATED_LOCAL);

        // Validate the Base in Elasticsearch
        Base baseEs = baseSearchRepository.findOne(testBase.getId());
        assertThat(baseEs).isEqualToComparingFieldByField(testBase);
    }

    @Test
    @Transactional
    public void updateNonExistingBase() throws Exception {
        int databaseSizeBeforeUpdate = baseRepository.findAll().size();

        // Create the Base

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBaseMockMvc.perform(put("/api/bases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(base)))
            .andExpect(status().isCreated());

        // Validate the Base in the database
        List<Base> baseList = baseRepository.findAll();
        assertThat(baseList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBase() throws Exception {
        // Initialize the database
        baseRepository.saveAndFlush(base);
        baseSearchRepository.save(base);
        int databaseSizeBeforeDelete = baseRepository.findAll().size();

        // Get the base
        restBaseMockMvc.perform(delete("/api/bases/{id}", base.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean baseExistsInEs = baseSearchRepository.exists(base.getId());
        assertThat(baseExistsInEs).isFalse();

        // Validate the database is empty
        List<Base> baseList = baseRepository.findAll();
        assertThat(baseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBase() throws Exception {
        // Initialize the database
        baseRepository.saveAndFlush(base);
        baseSearchRepository.save(base);

        // Search the base
        restBaseMockMvc.perform(get("/api/_search/bases?query=id:" + base.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(base.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].local").value(hasItem(DEFAULT_LOCAL.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Base.class);
    }
}

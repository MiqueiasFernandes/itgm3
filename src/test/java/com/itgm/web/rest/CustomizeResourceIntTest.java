package com.itgm.web.rest;

import com.itgm.ItgmApp;

import com.itgm.domain.Customize;
import com.itgm.repository.CustomizeRepository;
import com.itgm.repository.search.CustomizeSearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CustomizeResource REST controller.
 *
 * @see CustomizeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItgmApp.class)
public class CustomizeResourceIntTest {

    private static final Boolean DEFAULT_SIDEBAR = false;
    private static final Boolean UPDATED_SIDEBAR = true;

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final byte[] DEFAULT_AVATAR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_AVATAR = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_AVATAR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_AVATAR_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESKTOP = "AAAAAAAAAA";
    private static final String UPDATED_DESKTOP = "BBBBBBBBBB";

    @Autowired
    private CustomizeRepository customizeRepository;

    @Autowired
    private CustomizeSearchRepository customizeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCustomizeMockMvc;

    private Customize customize;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomizeResource customizeResource = new CustomizeResource(customizeRepository, customizeSearchRepository);
        this.restCustomizeMockMvc = MockMvcBuilders.standaloneSetup(customizeResource)
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
    public static Customize createEntity(EntityManager em) {
        Customize customize = new Customize()
            .sidebar(DEFAULT_SIDEBAR)
            .color(DEFAULT_COLOR)
            .avatar(DEFAULT_AVATAR)
            .avatarContentType(DEFAULT_AVATAR_CONTENT_TYPE)
            .desktop(DEFAULT_DESKTOP);
        return customize;
    }

    @Before
    public void initTest() {
        customizeSearchRepository.deleteAll();
        customize = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomize() throws Exception {
        int databaseSizeBeforeCreate = customizeRepository.findAll().size();

        // Create the Customize
        restCustomizeMockMvc.perform(post("/api/customizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customize)))
            .andExpect(status().isCreated());

        // Validate the Customize in the database
        List<Customize> customizeList = customizeRepository.findAll();
        assertThat(customizeList).hasSize(databaseSizeBeforeCreate + 1);
        Customize testCustomize = customizeList.get(customizeList.size() - 1);
        assertThat(testCustomize.isSidebar()).isEqualTo(DEFAULT_SIDEBAR);
        assertThat(testCustomize.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testCustomize.getAvatar()).isEqualTo(DEFAULT_AVATAR);
        assertThat(testCustomize.getAvatarContentType()).isEqualTo(DEFAULT_AVATAR_CONTENT_TYPE);
        assertThat(testCustomize.getDesktop()).isEqualTo(DEFAULT_DESKTOP);

        // Validate the Customize in Elasticsearch
        Customize customizeEs = customizeSearchRepository.findOne(testCustomize.getId());
        assertThat(customizeEs).isEqualToComparingFieldByField(testCustomize);
    }

    @Test
    @Transactional
    public void createCustomizeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customizeRepository.findAll().size();

        // Create the Customize with an existing ID
        customize.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomizeMockMvc.perform(post("/api/customizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customize)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Customize> customizeList = customizeRepository.findAll();
        assertThat(customizeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCustomizes() throws Exception {
        // Initialize the database
        customizeRepository.saveAndFlush(customize);

        // Get all the customizeList
        restCustomizeMockMvc.perform(get("/api/customizes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customize.getId().intValue())))
            .andExpect(jsonPath("$.[*].sidebar").value(hasItem(DEFAULT_SIDEBAR.booleanValue())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))))
            .andExpect(jsonPath("$.[*].desktop").value(hasItem(DEFAULT_DESKTOP.toString())));
    }

    @Test
    @Transactional
    public void getCustomize() throws Exception {
        // Initialize the database
        customizeRepository.saveAndFlush(customize);

        // Get the customize
        restCustomizeMockMvc.perform(get("/api/customizes/{id}", customize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customize.getId().intValue()))
            .andExpect(jsonPath("$.sidebar").value(DEFAULT_SIDEBAR.booleanValue()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.avatarContentType").value(DEFAULT_AVATAR_CONTENT_TYPE))
            .andExpect(jsonPath("$.avatar").value(Base64Utils.encodeToString(DEFAULT_AVATAR)))
            .andExpect(jsonPath("$.desktop").value(DEFAULT_DESKTOP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomize() throws Exception {
        // Get the customize
        restCustomizeMockMvc.perform(get("/api/customizes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomize() throws Exception {
        // Initialize the database
        customizeRepository.saveAndFlush(customize);
        customizeSearchRepository.save(customize);
        int databaseSizeBeforeUpdate = customizeRepository.findAll().size();

        // Update the customize
        Customize updatedCustomize = customizeRepository.findOne(customize.getId());
        updatedCustomize
            .sidebar(UPDATED_SIDEBAR)
            .color(UPDATED_COLOR)
            .avatar(UPDATED_AVATAR)
            .avatarContentType(UPDATED_AVATAR_CONTENT_TYPE)
            .desktop(UPDATED_DESKTOP);

        restCustomizeMockMvc.perform(put("/api/customizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomize)))
            .andExpect(status().isOk());

        // Validate the Customize in the database
        List<Customize> customizeList = customizeRepository.findAll();
        assertThat(customizeList).hasSize(databaseSizeBeforeUpdate);
        Customize testCustomize = customizeList.get(customizeList.size() - 1);
        assertThat(testCustomize.isSidebar()).isEqualTo(UPDATED_SIDEBAR);
        assertThat(testCustomize.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testCustomize.getAvatar()).isEqualTo(UPDATED_AVATAR);
        assertThat(testCustomize.getAvatarContentType()).isEqualTo(UPDATED_AVATAR_CONTENT_TYPE);
        assertThat(testCustomize.getDesktop()).isEqualTo(UPDATED_DESKTOP);

        // Validate the Customize in Elasticsearch
        Customize customizeEs = customizeSearchRepository.findOne(testCustomize.getId());
        assertThat(customizeEs).isEqualToComparingFieldByField(testCustomize);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomize() throws Exception {
        int databaseSizeBeforeUpdate = customizeRepository.findAll().size();

        // Create the Customize

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCustomizeMockMvc.perform(put("/api/customizes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customize)))
            .andExpect(status().isCreated());

        // Validate the Customize in the database
        List<Customize> customizeList = customizeRepository.findAll();
        assertThat(customizeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCustomize() throws Exception {
        // Initialize the database
        customizeRepository.saveAndFlush(customize);
        customizeSearchRepository.save(customize);
        int databaseSizeBeforeDelete = customizeRepository.findAll().size();

        // Get the customize
        restCustomizeMockMvc.perform(delete("/api/customizes/{id}", customize.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean customizeExistsInEs = customizeSearchRepository.exists(customize.getId());
        assertThat(customizeExistsInEs).isFalse();

        // Validate the database is empty
        List<Customize> customizeList = customizeRepository.findAll();
        assertThat(customizeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCustomize() throws Exception {
        // Initialize the database
        customizeRepository.saveAndFlush(customize);
        customizeSearchRepository.save(customize);

        // Search the customize
        restCustomizeMockMvc.perform(get("/api/_search/customizes?query=id:" + customize.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customize.getId().intValue())))
            .andExpect(jsonPath("$.[*].sidebar").value(hasItem(DEFAULT_SIDEBAR.booleanValue())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].avatarContentType").value(hasItem(DEFAULT_AVATAR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].avatar").value(hasItem(Base64Utils.encodeToString(DEFAULT_AVATAR))))
            .andExpect(jsonPath("$.[*].desktop").value(hasItem(DEFAULT_DESKTOP.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customize.class);
    }
}

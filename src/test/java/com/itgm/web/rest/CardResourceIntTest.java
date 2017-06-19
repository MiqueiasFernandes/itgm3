package com.itgm.web.rest;

import com.itgm.ItgmApp;

import com.itgm.domain.Card;
import com.itgm.repository.CardRepository;
import com.itgm.repository.search.CardSearchRepository;
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
 * Test class for the CardResource REST controller.
 *
 * @see CardResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ItgmApp.class)
public class CardResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_HTTPS = false;
    private static final Boolean UPDATED_HTTPS = true;

    private static final String DEFAULT_META = "AAAAAAAAAA";
    private static final String UPDATED_META = "BBBBBBBBBB";

    private static final String DEFAULT_PREVIA = "AAAAAAAAAA";
    private static final String UPDATED_PREVIA = "BBBBBBBBBB";

    private static final String DEFAULT_DISPOSICAO = "AAAAAAAAAA";
    private static final String UPDATED_DISPOSICAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIPO = 1;
    private static final Integer UPDATED_TIPO = 2;

    private static final Integer DEFAULT_LINHA = 1;
    private static final Integer UPDATED_LINHA = 2;

    private static final Integer DEFAULT_COLUNA = 1;
    private static final Integer UPDATED_COLUNA = 2;

    private static final String DEFAULT_MODO = "AAAAAAAAAA";
    private static final String UPDATED_MODO = "BBBBBBBBBB";

    private static final String DEFAULT_CAMINHO = "AAAAAAAAAA";
    private static final String UPDATED_CAMINHO = "BBBBBBBBBB";

    private static final String DEFAULT_ARQUIVO = "AAAAAAAAAA";
    private static final String UPDATED_ARQUIVO = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSAO = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSAO = "BBBBBBBBBB";

    private static final Integer DEFAULT_LARGURA = 1;
    private static final Integer UPDATED_LARGURA = 2;

    private static final String DEFAULT_CLASSE = "AAAAAAAAAA";
    private static final String UPDATED_CLASSE = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardSearchRepository cardSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCardMockMvc;

    private Card card;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CardResource cardResource = new CardResource(cardRepository, cardSearchRepository);
        this.restCardMockMvc = MockMvcBuilders.standaloneSetup(cardResource)
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
    public static Card createEntity(EntityManager em) {
        Card card = new Card()
            .nome(DEFAULT_NOME)
            .url(DEFAULT_URL)
            .https(DEFAULT_HTTPS)
            .meta(DEFAULT_META)
            .previa(DEFAULT_PREVIA)
            .disposicao(DEFAULT_DISPOSICAO)
            .tipo(DEFAULT_TIPO)
            .linha(DEFAULT_LINHA)
            .coluna(DEFAULT_COLUNA)
            .modo(DEFAULT_MODO)
            .caminho(DEFAULT_CAMINHO)
            .arquivo(DEFAULT_ARQUIVO)
            .extensao(DEFAULT_EXTENSAO)
            .largura(DEFAULT_LARGURA)
            .classe(DEFAULT_CLASSE)
            .codigo(DEFAULT_CODIGO);
        return card;
    }

    @Before
    public void initTest() {
        cardSearchRepository.deleteAll();
        card = createEntity(em);
    }

    @Test
    @Transactional
    public void createCard() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card
        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate + 1);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testCard.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testCard.isHttps()).isEqualTo(DEFAULT_HTTPS);
        assertThat(testCard.getMeta()).isEqualTo(DEFAULT_META);
        assertThat(testCard.getPrevia()).isEqualTo(DEFAULT_PREVIA);
        assertThat(testCard.getDisposicao()).isEqualTo(DEFAULT_DISPOSICAO);
        assertThat(testCard.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testCard.getLinha()).isEqualTo(DEFAULT_LINHA);
        assertThat(testCard.getColuna()).isEqualTo(DEFAULT_COLUNA);
        assertThat(testCard.getModo()).isEqualTo(DEFAULT_MODO);
        assertThat(testCard.getCaminho()).isEqualTo(DEFAULT_CAMINHO);
        assertThat(testCard.getArquivo()).isEqualTo(DEFAULT_ARQUIVO);
        assertThat(testCard.getExtensao()).isEqualTo(DEFAULT_EXTENSAO);
        assertThat(testCard.getLargura()).isEqualTo(DEFAULT_LARGURA);
        assertThat(testCard.getClasse()).isEqualTo(DEFAULT_CLASSE);
        assertThat(testCard.getCodigo()).isEqualTo(DEFAULT_CODIGO);

        // Validate the Card in Elasticsearch
        Card cardEs = cardSearchRepository.findOne(testCard.getId());
        assertThat(cardEs).isEqualToComparingFieldByField(testCard);
    }

    @Test
    @Transactional
    public void createCardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cardRepository.findAll().size();

        // Create the Card with an existing ID
        card.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCardMockMvc.perform(post("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCards() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get all the cardList
        restCardMockMvc.perform(get("/api/cards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].https").value(hasItem(DEFAULT_HTTPS.booleanValue())))
            .andExpect(jsonPath("$.[*].meta").value(hasItem(DEFAULT_META.toString())))
            .andExpect(jsonPath("$.[*].previa").value(hasItem(DEFAULT_PREVIA.toString())))
            .andExpect(jsonPath("$.[*].disposicao").value(hasItem(DEFAULT_DISPOSICAO.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].linha").value(hasItem(DEFAULT_LINHA)))
            .andExpect(jsonPath("$.[*].coluna").value(hasItem(DEFAULT_COLUNA)))
            .andExpect(jsonPath("$.[*].modo").value(hasItem(DEFAULT_MODO.toString())))
            .andExpect(jsonPath("$.[*].caminho").value(hasItem(DEFAULT_CAMINHO.toString())))
            .andExpect(jsonPath("$.[*].arquivo").value(hasItem(DEFAULT_ARQUIVO.toString())))
            .andExpect(jsonPath("$.[*].extensao").value(hasItem(DEFAULT_EXTENSAO.toString())))
            .andExpect(jsonPath("$.[*].largura").value(hasItem(DEFAULT_LARGURA)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())));
    }

    @Test
    @Transactional
    public void getCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);

        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(card.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.https").value(DEFAULT_HTTPS.booleanValue()))
            .andExpect(jsonPath("$.meta").value(DEFAULT_META.toString()))
            .andExpect(jsonPath("$.previa").value(DEFAULT_PREVIA.toString()))
            .andExpect(jsonPath("$.disposicao").value(DEFAULT_DISPOSICAO.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.linha").value(DEFAULT_LINHA))
            .andExpect(jsonPath("$.coluna").value(DEFAULT_COLUNA))
            .andExpect(jsonPath("$.modo").value(DEFAULT_MODO.toString()))
            .andExpect(jsonPath("$.caminho").value(DEFAULT_CAMINHO.toString()))
            .andExpect(jsonPath("$.arquivo").value(DEFAULT_ARQUIVO.toString()))
            .andExpect(jsonPath("$.extensao").value(DEFAULT_EXTENSAO.toString()))
            .andExpect(jsonPath("$.largura").value(DEFAULT_LARGURA))
            .andExpect(jsonPath("$.classe").value(DEFAULT_CLASSE.toString()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCard() throws Exception {
        // Get the card
        restCardMockMvc.perform(get("/api/cards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);
        cardSearchRepository.save(card);
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Update the card
        Card updatedCard = cardRepository.findOne(card.getId());
        updatedCard
            .nome(UPDATED_NOME)
            .url(UPDATED_URL)
            .https(UPDATED_HTTPS)
            .meta(UPDATED_META)
            .previa(UPDATED_PREVIA)
            .disposicao(UPDATED_DISPOSICAO)
            .tipo(UPDATED_TIPO)
            .linha(UPDATED_LINHA)
            .coluna(UPDATED_COLUNA)
            .modo(UPDATED_MODO)
            .caminho(UPDATED_CAMINHO)
            .arquivo(UPDATED_ARQUIVO)
            .extensao(UPDATED_EXTENSAO)
            .largura(UPDATED_LARGURA)
            .classe(UPDATED_CLASSE)
            .codigo(UPDATED_CODIGO);

        restCardMockMvc.perform(put("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCard)))
            .andExpect(status().isOk());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate);
        Card testCard = cardList.get(cardList.size() - 1);
        assertThat(testCard.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testCard.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testCard.isHttps()).isEqualTo(UPDATED_HTTPS);
        assertThat(testCard.getMeta()).isEqualTo(UPDATED_META);
        assertThat(testCard.getPrevia()).isEqualTo(UPDATED_PREVIA);
        assertThat(testCard.getDisposicao()).isEqualTo(UPDATED_DISPOSICAO);
        assertThat(testCard.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testCard.getLinha()).isEqualTo(UPDATED_LINHA);
        assertThat(testCard.getColuna()).isEqualTo(UPDATED_COLUNA);
        assertThat(testCard.getModo()).isEqualTo(UPDATED_MODO);
        assertThat(testCard.getCaminho()).isEqualTo(UPDATED_CAMINHO);
        assertThat(testCard.getArquivo()).isEqualTo(UPDATED_ARQUIVO);
        assertThat(testCard.getExtensao()).isEqualTo(UPDATED_EXTENSAO);
        assertThat(testCard.getLargura()).isEqualTo(UPDATED_LARGURA);
        assertThat(testCard.getClasse()).isEqualTo(UPDATED_CLASSE);
        assertThat(testCard.getCodigo()).isEqualTo(UPDATED_CODIGO);

        // Validate the Card in Elasticsearch
        Card cardEs = cardSearchRepository.findOne(testCard.getId());
        assertThat(cardEs).isEqualToComparingFieldByField(testCard);
    }

    @Test
    @Transactional
    public void updateNonExistingCard() throws Exception {
        int databaseSizeBeforeUpdate = cardRepository.findAll().size();

        // Create the Card

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCardMockMvc.perform(put("/api/cards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(card)))
            .andExpect(status().isCreated());

        // Validate the Card in the database
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);
        cardSearchRepository.save(card);
        int databaseSizeBeforeDelete = cardRepository.findAll().size();

        // Get the card
        restCardMockMvc.perform(delete("/api/cards/{id}", card.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cardExistsInEs = cardSearchRepository.exists(card.getId());
        assertThat(cardExistsInEs).isFalse();

        // Validate the database is empty
        List<Card> cardList = cardRepository.findAll();
        assertThat(cardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCard() throws Exception {
        // Initialize the database
        cardRepository.saveAndFlush(card);
        cardSearchRepository.save(card);

        // Search the card
        restCardMockMvc.perform(get("/api/_search/cards?query=id:" + card.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(card.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
            .andExpect(jsonPath("$.[*].https").value(hasItem(DEFAULT_HTTPS.booleanValue())))
            .andExpect(jsonPath("$.[*].meta").value(hasItem(DEFAULT_META.toString())))
            .andExpect(jsonPath("$.[*].previa").value(hasItem(DEFAULT_PREVIA.toString())))
            .andExpect(jsonPath("$.[*].disposicao").value(hasItem(DEFAULT_DISPOSICAO.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].linha").value(hasItem(DEFAULT_LINHA)))
            .andExpect(jsonPath("$.[*].coluna").value(hasItem(DEFAULT_COLUNA)))
            .andExpect(jsonPath("$.[*].modo").value(hasItem(DEFAULT_MODO.toString())))
            .andExpect(jsonPath("$.[*].caminho").value(hasItem(DEFAULT_CAMINHO.toString())))
            .andExpect(jsonPath("$.[*].arquivo").value(hasItem(DEFAULT_ARQUIVO.toString())))
            .andExpect(jsonPath("$.[*].extensao").value(hasItem(DEFAULT_EXTENSAO.toString())))
            .andExpect(jsonPath("$.[*].largura").value(hasItem(DEFAULT_LARGURA)))
            .andExpect(jsonPath("$.[*].classe").value(hasItem(DEFAULT_CLASSE.toString())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Card.class);
    }
}

package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.PrivateConfigHistory;
import com.utstar.uapollo.repository.PrivateConfigHistoryRepository;
import com.utstar.uapollo.service.PrivateConfigHistoryService;
import com.utstar.uapollo.service.dto.PrivateConfigHistoryDTO;
import com.utstar.uapollo.service.mapper.PrivateConfigHistoryMapper;
import com.utstar.uapollo.web.rest.errors.ExceptionTranslator;

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

import static com.utstar.uapollo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.utstar.uapollo.domain.enumeration.Operation;
/**
 * Test class for the PrivateConfigHistoryResource REST controller.
 *
 * @see PrivateConfigHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class PrivateConfigHistoryResourceIntTest {

    private static final Long DEFAULT_APPLICATION_ID = 1L;
    private static final Long UPDATED_APPLICATION_ID = 2L;

    private static final Long DEFAULT_PRIVATE_CONFIG_ID = 1L;
    private static final Long UPDATED_PRIVATE_CONFIG_ID = 2L;

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Operation DEFAULT_OPERATION = Operation.CREATE;
    private static final Operation UPDATED_OPERATION = Operation.UPDATE;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Autowired
    private PrivateConfigHistoryRepository privateConfigHistoryRepository;

    @Autowired
    private PrivateConfigHistoryMapper privateConfigHistoryMapper;

    @Autowired
    private PrivateConfigHistoryService privateConfigHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPrivateConfigHistoryMockMvc;

    private PrivateConfigHistory privateConfigHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrivateConfigHistoryResource privateConfigHistoryResource = new PrivateConfigHistoryResource(privateConfigHistoryService);
        this.restPrivateConfigHistoryMockMvc = MockMvcBuilders.standaloneSetup(privateConfigHistoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PrivateConfigHistory createEntity(EntityManager em) {
        PrivateConfigHistory privateConfigHistory = new PrivateConfigHistory()
            .applicationId(DEFAULT_APPLICATION_ID)
            .privateConfigId(DEFAULT_PRIVATE_CONFIG_ID)
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .operation(DEFAULT_OPERATION)
            .createTime(DEFAULT_CREATE_TIME);
        return privateConfigHistory;
    }

    @Before
    public void initTest() {
        privateConfigHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrivateConfigHistory() throws Exception {
        int databaseSizeBeforeCreate = privateConfigHistoryRepository.findAll().size();

        // Create the PrivateConfigHistory
        PrivateConfigHistoryDTO privateConfigHistoryDTO = privateConfigHistoryMapper.toDto(privateConfigHistory);
        restPrivateConfigHistoryMockMvc.perform(post("/api/private-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the PrivateConfigHistory in the database
        List<PrivateConfigHistory> privateConfigHistoryList = privateConfigHistoryRepository.findAll();
        assertThat(privateConfigHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        PrivateConfigHistory testPrivateConfigHistory = privateConfigHistoryList.get(privateConfigHistoryList.size() - 1);
        assertThat(testPrivateConfigHistory.getApplicationId()).isEqualTo(DEFAULT_APPLICATION_ID);
        assertThat(testPrivateConfigHistory.getPrivateConfigId()).isEqualTo(DEFAULT_PRIVATE_CONFIG_ID);
        assertThat(testPrivateConfigHistory.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testPrivateConfigHistory.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testPrivateConfigHistory.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testPrivateConfigHistory.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void createPrivateConfigHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = privateConfigHistoryRepository.findAll().size();

        // Create the PrivateConfigHistory with an existing ID
        privateConfigHistory.setId(1L);
        PrivateConfigHistoryDTO privateConfigHistoryDTO = privateConfigHistoryMapper.toDto(privateConfigHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrivateConfigHistoryMockMvc.perform(post("/api/private-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PrivateConfigHistory in the database
        List<PrivateConfigHistory> privateConfigHistoryList = privateConfigHistoryRepository.findAll();
        assertThat(privateConfigHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkApplicationIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = privateConfigHistoryRepository.findAll().size();
        // set the field null
        privateConfigHistory.setApplicationId(null);

        // Create the PrivateConfigHistory, which fails.
        PrivateConfigHistoryDTO privateConfigHistoryDTO = privateConfigHistoryMapper.toDto(privateConfigHistory);

        restPrivateConfigHistoryMockMvc.perform(post("/api/private-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<PrivateConfigHistory> privateConfigHistoryList = privateConfigHistoryRepository.findAll();
        assertThat(privateConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrivateConfigIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = privateConfigHistoryRepository.findAll().size();
        // set the field null
        privateConfigHistory.setPrivateConfigId(null);

        // Create the PrivateConfigHistory, which fails.
        PrivateConfigHistoryDTO privateConfigHistoryDTO = privateConfigHistoryMapper.toDto(privateConfigHistory);

        restPrivateConfigHistoryMockMvc.perform(post("/api/private-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<PrivateConfigHistory> privateConfigHistoryList = privateConfigHistoryRepository.findAll();
        assertThat(privateConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = privateConfigHistoryRepository.findAll().size();
        // set the field null
        privateConfigHistory.setKey(null);

        // Create the PrivateConfigHistory, which fails.
        PrivateConfigHistoryDTO privateConfigHistoryDTO = privateConfigHistoryMapper.toDto(privateConfigHistory);

        restPrivateConfigHistoryMockMvc.perform(post("/api/private-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<PrivateConfigHistory> privateConfigHistoryList = privateConfigHistoryRepository.findAll();
        assertThat(privateConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = privateConfigHistoryRepository.findAll().size();
        // set the field null
        privateConfigHistory.setValue(null);

        // Create the PrivateConfigHistory, which fails.
        PrivateConfigHistoryDTO privateConfigHistoryDTO = privateConfigHistoryMapper.toDto(privateConfigHistory);

        restPrivateConfigHistoryMockMvc.perform(post("/api/private-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<PrivateConfigHistory> privateConfigHistoryList = privateConfigHistoryRepository.findAll();
        assertThat(privateConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = privateConfigHistoryRepository.findAll().size();
        // set the field null
        privateConfigHistory.setOperation(null);

        // Create the PrivateConfigHistory, which fails.
        PrivateConfigHistoryDTO privateConfigHistoryDTO = privateConfigHistoryMapper.toDto(privateConfigHistory);

        restPrivateConfigHistoryMockMvc.perform(post("/api/private-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<PrivateConfigHistory> privateConfigHistoryList = privateConfigHistoryRepository.findAll();
        assertThat(privateConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = privateConfigHistoryRepository.findAll().size();
        // set the field null
        privateConfigHistory.setCreateTime(null);

        // Create the PrivateConfigHistory, which fails.
        PrivateConfigHistoryDTO privateConfigHistoryDTO = privateConfigHistoryMapper.toDto(privateConfigHistory);

        restPrivateConfigHistoryMockMvc.perform(post("/api/private-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<PrivateConfigHistory> privateConfigHistoryList = privateConfigHistoryRepository.findAll();
        assertThat(privateConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrivateConfigHistories() throws Exception {
        // Initialize the database
        privateConfigHistoryRepository.saveAndFlush(privateConfigHistory);

        // Get all the privateConfigHistoryList
        restPrivateConfigHistoryMockMvc.perform(get("/api/private-config-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(privateConfigHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationId").value(hasItem(DEFAULT_APPLICATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].privateConfigId").value(hasItem(DEFAULT_PRIVATE_CONFIG_ID.intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getPrivateConfigHistory() throws Exception {
        // Initialize the database
        privateConfigHistoryRepository.saveAndFlush(privateConfigHistory);

        // Get the privateConfigHistory
        restPrivateConfigHistoryMockMvc.perform(get("/api/private-config-histories/{id}", privateConfigHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(privateConfigHistory.getId().intValue()))
            .andExpect(jsonPath("$.applicationId").value(DEFAULT_APPLICATION_ID.intValue()))
            .andExpect(jsonPath("$.privateConfigId").value(DEFAULT_PRIVATE_CONFIG_ID.intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPrivateConfigHistory() throws Exception {
        // Get the privateConfigHistory
        restPrivateConfigHistoryMockMvc.perform(get("/api/private-config-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrivateConfigHistory() throws Exception {
        // Initialize the database
        privateConfigHistoryRepository.saveAndFlush(privateConfigHistory);
        int databaseSizeBeforeUpdate = privateConfigHistoryRepository.findAll().size();

        // Update the privateConfigHistory
        PrivateConfigHistory updatedPrivateConfigHistory = privateConfigHistoryRepository.findOne(privateConfigHistory.getId());
        // Disconnect from session so that the updates on updatedPrivateConfigHistory are not directly saved in db
        em.detach(updatedPrivateConfigHistory);
        updatedPrivateConfigHistory
            .applicationId(UPDATED_APPLICATION_ID)
            .privateConfigId(UPDATED_PRIVATE_CONFIG_ID)
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .operation(UPDATED_OPERATION)
            .createTime(UPDATED_CREATE_TIME);
        PrivateConfigHistoryDTO privateConfigHistoryDTO = privateConfigHistoryMapper.toDto(updatedPrivateConfigHistory);

        restPrivateConfigHistoryMockMvc.perform(put("/api/private-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the PrivateConfigHistory in the database
        List<PrivateConfigHistory> privateConfigHistoryList = privateConfigHistoryRepository.findAll();
        assertThat(privateConfigHistoryList).hasSize(databaseSizeBeforeUpdate);
        PrivateConfigHistory testPrivateConfigHistory = privateConfigHistoryList.get(privateConfigHistoryList.size() - 1);
        assertThat(testPrivateConfigHistory.getApplicationId()).isEqualTo(UPDATED_APPLICATION_ID);
        assertThat(testPrivateConfigHistory.getPrivateConfigId()).isEqualTo(UPDATED_PRIVATE_CONFIG_ID);
        assertThat(testPrivateConfigHistory.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testPrivateConfigHistory.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testPrivateConfigHistory.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testPrivateConfigHistory.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingPrivateConfigHistory() throws Exception {
        int databaseSizeBeforeUpdate = privateConfigHistoryRepository.findAll().size();

        // Create the PrivateConfigHistory
        PrivateConfigHistoryDTO privateConfigHistoryDTO = privateConfigHistoryMapper.toDto(privateConfigHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPrivateConfigHistoryMockMvc.perform(put("/api/private-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the PrivateConfigHistory in the database
        List<PrivateConfigHistory> privateConfigHistoryList = privateConfigHistoryRepository.findAll();
        assertThat(privateConfigHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePrivateConfigHistory() throws Exception {
        // Initialize the database
        privateConfigHistoryRepository.saveAndFlush(privateConfigHistory);
        int databaseSizeBeforeDelete = privateConfigHistoryRepository.findAll().size();

        // Get the privateConfigHistory
        restPrivateConfigHistoryMockMvc.perform(delete("/api/private-config-histories/{id}", privateConfigHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PrivateConfigHistory> privateConfigHistoryList = privateConfigHistoryRepository.findAll();
        assertThat(privateConfigHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrivateConfigHistory.class);
        PrivateConfigHistory privateConfigHistory1 = new PrivateConfigHistory();
        privateConfigHistory1.setId(1L);
        PrivateConfigHistory privateConfigHistory2 = new PrivateConfigHistory();
        privateConfigHistory2.setId(privateConfigHistory1.getId());
        assertThat(privateConfigHistory1).isEqualTo(privateConfigHistory2);
        privateConfigHistory2.setId(2L);
        assertThat(privateConfigHistory1).isNotEqualTo(privateConfigHistory2);
        privateConfigHistory1.setId(null);
        assertThat(privateConfigHistory1).isNotEqualTo(privateConfigHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrivateConfigHistoryDTO.class);
        PrivateConfigHistoryDTO privateConfigHistoryDTO1 = new PrivateConfigHistoryDTO();
        privateConfigHistoryDTO1.setId(1L);
        PrivateConfigHistoryDTO privateConfigHistoryDTO2 = new PrivateConfigHistoryDTO();
        assertThat(privateConfigHistoryDTO1).isNotEqualTo(privateConfigHistoryDTO2);
        privateConfigHistoryDTO2.setId(privateConfigHistoryDTO1.getId());
        assertThat(privateConfigHistoryDTO1).isEqualTo(privateConfigHistoryDTO2);
        privateConfigHistoryDTO2.setId(2L);
        assertThat(privateConfigHistoryDTO1).isNotEqualTo(privateConfigHistoryDTO2);
        privateConfigHistoryDTO1.setId(null);
        assertThat(privateConfigHistoryDTO1).isNotEqualTo(privateConfigHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(privateConfigHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(privateConfigHistoryMapper.fromId(null)).isNull();
    }
}

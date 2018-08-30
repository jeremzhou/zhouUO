package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.GlobalConfigHistory;
import com.utstar.uapollo.repository.GlobalConfigHistoryRepository;
import com.utstar.uapollo.service.GlobalConfigHistoryService;
import com.utstar.uapollo.service.dto.GlobalConfigHistoryDTO;
import com.utstar.uapollo.service.mapper.GlobalConfigHistoryMapper;
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
 * Test class for the GlobalConfigHistoryResource REST controller.
 *
 * @see GlobalConfigHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class GlobalConfigHistoryResourceIntTest {

    private static final Long DEFAULT_APPLICATION_META_ID = 1L;
    private static final Long UPDATED_APPLICATION_META_ID = 2L;

    private static final Long DEFAULT_GLOBAL_CONFIG_ID = 1L;
    private static final Long UPDATED_GLOBAL_CONFIG_ID = 2L;

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Operation DEFAULT_OPERATION = Operation.CREATE;
    private static final Operation UPDATED_OPERATION = Operation.UPDATE;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Autowired
    private GlobalConfigHistoryRepository globalConfigHistoryRepository;

    @Autowired
    private GlobalConfigHistoryMapper globalConfigHistoryMapper;

    @Autowired
    private GlobalConfigHistoryService globalConfigHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGlobalConfigHistoryMockMvc;

    private GlobalConfigHistory globalConfigHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GlobalConfigHistoryResource globalConfigHistoryResource = new GlobalConfigHistoryResource(globalConfigHistoryService);
        this.restGlobalConfigHistoryMockMvc = MockMvcBuilders.standaloneSetup(globalConfigHistoryResource)
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
    public static GlobalConfigHistory createEntity(EntityManager em) {
        GlobalConfigHistory globalConfigHistory = new GlobalConfigHistory()
            .applicationMetaId(DEFAULT_APPLICATION_META_ID)
            .globalConfigId(DEFAULT_GLOBAL_CONFIG_ID)
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .operation(DEFAULT_OPERATION)
            .createTime(DEFAULT_CREATE_TIME);
        return globalConfigHistory;
    }

    @Before
    public void initTest() {
        globalConfigHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createGlobalConfigHistory() throws Exception {
        int databaseSizeBeforeCreate = globalConfigHistoryRepository.findAll().size();

        // Create the GlobalConfigHistory
        GlobalConfigHistoryDTO globalConfigHistoryDTO = globalConfigHistoryMapper.toDto(globalConfigHistory);
        restGlobalConfigHistoryMockMvc.perform(post("/api/global-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the GlobalConfigHistory in the database
        List<GlobalConfigHistory> globalConfigHistoryList = globalConfigHistoryRepository.findAll();
        assertThat(globalConfigHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        GlobalConfigHistory testGlobalConfigHistory = globalConfigHistoryList.get(globalConfigHistoryList.size() - 1);
        assertThat(testGlobalConfigHistory.getApplicationMetaId()).isEqualTo(DEFAULT_APPLICATION_META_ID);
        assertThat(testGlobalConfigHistory.getGlobalConfigId()).isEqualTo(DEFAULT_GLOBAL_CONFIG_ID);
        assertThat(testGlobalConfigHistory.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testGlobalConfigHistory.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testGlobalConfigHistory.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testGlobalConfigHistory.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void createGlobalConfigHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = globalConfigHistoryRepository.findAll().size();

        // Create the GlobalConfigHistory with an existing ID
        globalConfigHistory.setId(1L);
        GlobalConfigHistoryDTO globalConfigHistoryDTO = globalConfigHistoryMapper.toDto(globalConfigHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGlobalConfigHistoryMockMvc.perform(post("/api/global-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GlobalConfigHistory in the database
        List<GlobalConfigHistory> globalConfigHistoryList = globalConfigHistoryRepository.findAll();
        assertThat(globalConfigHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkApplicationMetaIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalConfigHistoryRepository.findAll().size();
        // set the field null
        globalConfigHistory.setApplicationMetaId(null);

        // Create the GlobalConfigHistory, which fails.
        GlobalConfigHistoryDTO globalConfigHistoryDTO = globalConfigHistoryMapper.toDto(globalConfigHistory);

        restGlobalConfigHistoryMockMvc.perform(post("/api/global-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<GlobalConfigHistory> globalConfigHistoryList = globalConfigHistoryRepository.findAll();
        assertThat(globalConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGlobalConfigIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalConfigHistoryRepository.findAll().size();
        // set the field null
        globalConfigHistory.setGlobalConfigId(null);

        // Create the GlobalConfigHistory, which fails.
        GlobalConfigHistoryDTO globalConfigHistoryDTO = globalConfigHistoryMapper.toDto(globalConfigHistory);

        restGlobalConfigHistoryMockMvc.perform(post("/api/global-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<GlobalConfigHistory> globalConfigHistoryList = globalConfigHistoryRepository.findAll();
        assertThat(globalConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalConfigHistoryRepository.findAll().size();
        // set the field null
        globalConfigHistory.setKey(null);

        // Create the GlobalConfigHistory, which fails.
        GlobalConfigHistoryDTO globalConfigHistoryDTO = globalConfigHistoryMapper.toDto(globalConfigHistory);

        restGlobalConfigHistoryMockMvc.perform(post("/api/global-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<GlobalConfigHistory> globalConfigHistoryList = globalConfigHistoryRepository.findAll();
        assertThat(globalConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalConfigHistoryRepository.findAll().size();
        // set the field null
        globalConfigHistory.setValue(null);

        // Create the GlobalConfigHistory, which fails.
        GlobalConfigHistoryDTO globalConfigHistoryDTO = globalConfigHistoryMapper.toDto(globalConfigHistory);

        restGlobalConfigHistoryMockMvc.perform(post("/api/global-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<GlobalConfigHistory> globalConfigHistoryList = globalConfigHistoryRepository.findAll();
        assertThat(globalConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalConfigHistoryRepository.findAll().size();
        // set the field null
        globalConfigHistory.setOperation(null);

        // Create the GlobalConfigHistory, which fails.
        GlobalConfigHistoryDTO globalConfigHistoryDTO = globalConfigHistoryMapper.toDto(globalConfigHistory);

        restGlobalConfigHistoryMockMvc.perform(post("/api/global-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<GlobalConfigHistory> globalConfigHistoryList = globalConfigHistoryRepository.findAll();
        assertThat(globalConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalConfigHistoryRepository.findAll().size();
        // set the field null
        globalConfigHistory.setCreateTime(null);

        // Create the GlobalConfigHistory, which fails.
        GlobalConfigHistoryDTO globalConfigHistoryDTO = globalConfigHistoryMapper.toDto(globalConfigHistory);

        restGlobalConfigHistoryMockMvc.perform(post("/api/global-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<GlobalConfigHistory> globalConfigHistoryList = globalConfigHistoryRepository.findAll();
        assertThat(globalConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigHistories() throws Exception {
        // Initialize the database
        globalConfigHistoryRepository.saveAndFlush(globalConfigHistory);

        // Get all the globalConfigHistoryList
        restGlobalConfigHistoryMockMvc.perform(get("/api/global-config-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(globalConfigHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationMetaId").value(hasItem(DEFAULT_APPLICATION_META_ID.intValue())))
            .andExpect(jsonPath("$.[*].globalConfigId").value(hasItem(DEFAULT_GLOBAL_CONFIG_ID.intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getGlobalConfigHistory() throws Exception {
        // Initialize the database
        globalConfigHistoryRepository.saveAndFlush(globalConfigHistory);

        // Get the globalConfigHistory
        restGlobalConfigHistoryMockMvc.perform(get("/api/global-config-histories/{id}", globalConfigHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(globalConfigHistory.getId().intValue()))
            .andExpect(jsonPath("$.applicationMetaId").value(DEFAULT_APPLICATION_META_ID.intValue()))
            .andExpect(jsonPath("$.globalConfigId").value(DEFAULT_GLOBAL_CONFIG_ID.intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGlobalConfigHistory() throws Exception {
        // Get the globalConfigHistory
        restGlobalConfigHistoryMockMvc.perform(get("/api/global-config-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGlobalConfigHistory() throws Exception {
        // Initialize the database
        globalConfigHistoryRepository.saveAndFlush(globalConfigHistory);
        int databaseSizeBeforeUpdate = globalConfigHistoryRepository.findAll().size();

        // Update the globalConfigHistory
        GlobalConfigHistory updatedGlobalConfigHistory = globalConfigHistoryRepository.findOne(globalConfigHistory.getId());
        // Disconnect from session so that the updates on updatedGlobalConfigHistory are not directly saved in db
        em.detach(updatedGlobalConfigHistory);
        updatedGlobalConfigHistory
            .applicationMetaId(UPDATED_APPLICATION_META_ID)
            .globalConfigId(UPDATED_GLOBAL_CONFIG_ID)
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .operation(UPDATED_OPERATION)
            .createTime(UPDATED_CREATE_TIME);
        GlobalConfigHistoryDTO globalConfigHistoryDTO = globalConfigHistoryMapper.toDto(updatedGlobalConfigHistory);

        restGlobalConfigHistoryMockMvc.perform(put("/api/global-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the GlobalConfigHistory in the database
        List<GlobalConfigHistory> globalConfigHistoryList = globalConfigHistoryRepository.findAll();
        assertThat(globalConfigHistoryList).hasSize(databaseSizeBeforeUpdate);
        GlobalConfigHistory testGlobalConfigHistory = globalConfigHistoryList.get(globalConfigHistoryList.size() - 1);
        assertThat(testGlobalConfigHistory.getApplicationMetaId()).isEqualTo(UPDATED_APPLICATION_META_ID);
        assertThat(testGlobalConfigHistory.getGlobalConfigId()).isEqualTo(UPDATED_GLOBAL_CONFIG_ID);
        assertThat(testGlobalConfigHistory.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testGlobalConfigHistory.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testGlobalConfigHistory.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testGlobalConfigHistory.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingGlobalConfigHistory() throws Exception {
        int databaseSizeBeforeUpdate = globalConfigHistoryRepository.findAll().size();

        // Create the GlobalConfigHistory
        GlobalConfigHistoryDTO globalConfigHistoryDTO = globalConfigHistoryMapper.toDto(globalConfigHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGlobalConfigHistoryMockMvc.perform(put("/api/global-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the GlobalConfigHistory in the database
        List<GlobalConfigHistory> globalConfigHistoryList = globalConfigHistoryRepository.findAll();
        assertThat(globalConfigHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteGlobalConfigHistory() throws Exception {
        // Initialize the database
        globalConfigHistoryRepository.saveAndFlush(globalConfigHistory);
        int databaseSizeBeforeDelete = globalConfigHistoryRepository.findAll().size();

        // Get the globalConfigHistory
        restGlobalConfigHistoryMockMvc.perform(delete("/api/global-config-histories/{id}", globalConfigHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<GlobalConfigHistory> globalConfigHistoryList = globalConfigHistoryRepository.findAll();
        assertThat(globalConfigHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalConfigHistory.class);
        GlobalConfigHistory globalConfigHistory1 = new GlobalConfigHistory();
        globalConfigHistory1.setId(1L);
        GlobalConfigHistory globalConfigHistory2 = new GlobalConfigHistory();
        globalConfigHistory2.setId(globalConfigHistory1.getId());
        assertThat(globalConfigHistory1).isEqualTo(globalConfigHistory2);
        globalConfigHistory2.setId(2L);
        assertThat(globalConfigHistory1).isNotEqualTo(globalConfigHistory2);
        globalConfigHistory1.setId(null);
        assertThat(globalConfigHistory1).isNotEqualTo(globalConfigHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalConfigHistoryDTO.class);
        GlobalConfigHistoryDTO globalConfigHistoryDTO1 = new GlobalConfigHistoryDTO();
        globalConfigHistoryDTO1.setId(1L);
        GlobalConfigHistoryDTO globalConfigHistoryDTO2 = new GlobalConfigHistoryDTO();
        assertThat(globalConfigHistoryDTO1).isNotEqualTo(globalConfigHistoryDTO2);
        globalConfigHistoryDTO2.setId(globalConfigHistoryDTO1.getId());
        assertThat(globalConfigHistoryDTO1).isEqualTo(globalConfigHistoryDTO2);
        globalConfigHistoryDTO2.setId(2L);
        assertThat(globalConfigHistoryDTO1).isNotEqualTo(globalConfigHistoryDTO2);
        globalConfigHistoryDTO1.setId(null);
        assertThat(globalConfigHistoryDTO1).isNotEqualTo(globalConfigHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(globalConfigHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(globalConfigHistoryMapper.fromId(null)).isNull();
    }
}

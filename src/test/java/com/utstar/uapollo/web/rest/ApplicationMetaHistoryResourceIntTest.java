package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.ApplicationMetaHistory;
import com.utstar.uapollo.repository.ApplicationMetaHistoryRepository;
import com.utstar.uapollo.service.ApplicationMetaHistoryService;
import com.utstar.uapollo.service.dto.ApplicationMetaHistoryDTO;
import com.utstar.uapollo.service.mapper.ApplicationMetaHistoryMapper;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.utstar.uapollo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.utstar.uapollo.domain.enumeration.Operation;
/**
 * Test class for the ApplicationMetaHistoryResource REST controller.
 *
 * @see ApplicationMetaHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class ApplicationMetaHistoryResourceIntTest {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long UPDATED_PROJECT_ID = 2L;

    private static final Long DEFAULT_APPLICATION_META_ID = 1L;
    private static final Long UPDATED_APPLICATION_META_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_FILE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_FILE = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_CONTENT = "BBBBBBBBBB";

    private static final Operation DEFAULT_OPERATION = Operation.CREATE;
    private static final Operation UPDATED_OPERATION = Operation.UPDATE;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Autowired
    private ApplicationMetaHistoryRepository applicationMetaHistoryRepository;

    @Autowired
    private ApplicationMetaHistoryMapper applicationMetaHistoryMapper;

    @Autowired
    private ApplicationMetaHistoryService applicationMetaHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplicationMetaHistoryMockMvc;

    private ApplicationMetaHistory applicationMetaHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationMetaHistoryResource applicationMetaHistoryResource = new ApplicationMetaHistoryResource(applicationMetaHistoryService);
        this.restApplicationMetaHistoryMockMvc = MockMvcBuilders.standaloneSetup(applicationMetaHistoryResource)
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
    public static ApplicationMetaHistory createEntity(EntityManager em) {
        ApplicationMetaHistory applicationMetaHistory = new ApplicationMetaHistory()
            .projectId(DEFAULT_PROJECT_ID)
            .applicationMetaId(DEFAULT_APPLICATION_META_ID)
            .name(DEFAULT_NAME)
            .configFile(DEFAULT_CONFIG_FILE)
            .configContent(DEFAULT_CONFIG_CONTENT)
            .operation(DEFAULT_OPERATION)
            .createTime(DEFAULT_CREATE_TIME);
        return applicationMetaHistory;
    }

    @Before
    public void initTest() {
        applicationMetaHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationMetaHistory() throws Exception {
        int databaseSizeBeforeCreate = applicationMetaHistoryRepository.findAll().size();

        // Create the ApplicationMetaHistory
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryMapper.toDto(applicationMetaHistory);
        restApplicationMetaHistoryMockMvc.perform(post("/api/application-meta-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplicationMetaHistory in the database
        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationMetaHistory testApplicationMetaHistory = applicationMetaHistoryList.get(applicationMetaHistoryList.size() - 1);
        assertThat(testApplicationMetaHistory.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
        assertThat(testApplicationMetaHistory.getApplicationMetaId()).isEqualTo(DEFAULT_APPLICATION_META_ID);
        assertThat(testApplicationMetaHistory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplicationMetaHistory.getConfigFile()).isEqualTo(DEFAULT_CONFIG_FILE);
        assertThat(testApplicationMetaHistory.getConfigContent()).isEqualTo(DEFAULT_CONFIG_CONTENT);
        assertThat(testApplicationMetaHistory.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testApplicationMetaHistory.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void createApplicationMetaHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationMetaHistoryRepository.findAll().size();

        // Create the ApplicationMetaHistory with an existing ID
        applicationMetaHistory.setId(1L);
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryMapper.toDto(applicationMetaHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationMetaHistoryMockMvc.perform(post("/api/application-meta-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationMetaHistory in the database
        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaHistoryRepository.findAll().size();
        // set the field null
        applicationMetaHistory.setProjectId(null);

        // Create the ApplicationMetaHistory, which fails.
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryMapper.toDto(applicationMetaHistory);

        restApplicationMetaHistoryMockMvc.perform(post("/api/application-meta-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApplicationMetaIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaHistoryRepository.findAll().size();
        // set the field null
        applicationMetaHistory.setApplicationMetaId(null);

        // Create the ApplicationMetaHistory, which fails.
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryMapper.toDto(applicationMetaHistory);

        restApplicationMetaHistoryMockMvc.perform(post("/api/application-meta-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaHistoryRepository.findAll().size();
        // set the field null
        applicationMetaHistory.setName(null);

        // Create the ApplicationMetaHistory, which fails.
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryMapper.toDto(applicationMetaHistory);

        restApplicationMetaHistoryMockMvc.perform(post("/api/application-meta-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConfigFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaHistoryRepository.findAll().size();
        // set the field null
        applicationMetaHistory.setConfigFile(null);

        // Create the ApplicationMetaHistory, which fails.
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryMapper.toDto(applicationMetaHistory);

        restApplicationMetaHistoryMockMvc.perform(post("/api/application-meta-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConfigContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaHistoryRepository.findAll().size();
        // set the field null
        applicationMetaHistory.setConfigContent(null);

        // Create the ApplicationMetaHistory, which fails.
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryMapper.toDto(applicationMetaHistory);

        restApplicationMetaHistoryMockMvc.perform(post("/api/application-meta-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaHistoryRepository.findAll().size();
        // set the field null
        applicationMetaHistory.setOperation(null);

        // Create the ApplicationMetaHistory, which fails.
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryMapper.toDto(applicationMetaHistory);

        restApplicationMetaHistoryMockMvc.perform(post("/api/application-meta-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaHistoryRepository.findAll().size();
        // set the field null
        applicationMetaHistory.setCreateTime(null);

        // Create the ApplicationMetaHistory, which fails.
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryMapper.toDto(applicationMetaHistory);

        restApplicationMetaHistoryMockMvc.perform(post("/api/application-meta-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicationMetaHistories() throws Exception {
        // Initialize the database
        applicationMetaHistoryRepository.saveAndFlush(applicationMetaHistory);

        // Get all the applicationMetaHistoryList
        restApplicationMetaHistoryMockMvc.perform(get("/api/application-meta-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationMetaHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].applicationMetaId").value(hasItem(DEFAULT_APPLICATION_META_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].configFile").value(hasItem(DEFAULT_CONFIG_FILE.toString())))
            .andExpect(jsonPath("$.[*].configContent").value(hasItem(DEFAULT_CONFIG_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getApplicationMetaHistory() throws Exception {
        // Initialize the database
        applicationMetaHistoryRepository.saveAndFlush(applicationMetaHistory);

        // Get the applicationMetaHistory
        restApplicationMetaHistoryMockMvc.perform(get("/api/application-meta-histories/{id}", applicationMetaHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applicationMetaHistory.getId().intValue()))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.intValue()))
            .andExpect(jsonPath("$.applicationMetaId").value(DEFAULT_APPLICATION_META_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.configFile").value(DEFAULT_CONFIG_FILE.toString()))
            .andExpect(jsonPath("$.configContent").value(DEFAULT_CONFIG_CONTENT.toString()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationMetaHistory() throws Exception {
        // Get the applicationMetaHistory
        restApplicationMetaHistoryMockMvc.perform(get("/api/application-meta-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationMetaHistory() throws Exception {
        // Initialize the database
        applicationMetaHistoryRepository.saveAndFlush(applicationMetaHistory);
        int databaseSizeBeforeUpdate = applicationMetaHistoryRepository.findAll().size();

        // Update the applicationMetaHistory
        ApplicationMetaHistory updatedApplicationMetaHistory = applicationMetaHistoryRepository.findOne(applicationMetaHistory.getId());
        // Disconnect from session so that the updates on updatedApplicationMetaHistory are not directly saved in db
        em.detach(updatedApplicationMetaHistory);
        updatedApplicationMetaHistory
            .projectId(UPDATED_PROJECT_ID)
            .applicationMetaId(UPDATED_APPLICATION_META_ID)
            .name(UPDATED_NAME)
            .configFile(UPDATED_CONFIG_FILE)
            .configContent(UPDATED_CONFIG_CONTENT)
            .operation(UPDATED_OPERATION)
            .createTime(UPDATED_CREATE_TIME);
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryMapper.toDto(updatedApplicationMetaHistory);

        restApplicationMetaHistoryMockMvc.perform(put("/api/application-meta-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the ApplicationMetaHistory in the database
        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeUpdate);
        ApplicationMetaHistory testApplicationMetaHistory = applicationMetaHistoryList.get(applicationMetaHistoryList.size() - 1);
        assertThat(testApplicationMetaHistory.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
        assertThat(testApplicationMetaHistory.getApplicationMetaId()).isEqualTo(UPDATED_APPLICATION_META_ID);
        assertThat(testApplicationMetaHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplicationMetaHistory.getConfigFile()).isEqualTo(UPDATED_CONFIG_FILE);
        assertThat(testApplicationMetaHistory.getConfigContent()).isEqualTo(UPDATED_CONFIG_CONTENT);
        assertThat(testApplicationMetaHistory.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testApplicationMetaHistory.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationMetaHistory() throws Exception {
        int databaseSizeBeforeUpdate = applicationMetaHistoryRepository.findAll().size();

        // Create the ApplicationMetaHistory
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = applicationMetaHistoryMapper.toDto(applicationMetaHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApplicationMetaHistoryMockMvc.perform(put("/api/application-meta-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplicationMetaHistory in the database
        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApplicationMetaHistory() throws Exception {
        // Initialize the database
        applicationMetaHistoryRepository.saveAndFlush(applicationMetaHistory);
        int databaseSizeBeforeDelete = applicationMetaHistoryRepository.findAll().size();

        // Get the applicationMetaHistory
        restApplicationMetaHistoryMockMvc.perform(delete("/api/application-meta-histories/{id}", applicationMetaHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ApplicationMetaHistory> applicationMetaHistoryList = applicationMetaHistoryRepository.findAll();
        assertThat(applicationMetaHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationMetaHistory.class);
        ApplicationMetaHistory applicationMetaHistory1 = new ApplicationMetaHistory();
        applicationMetaHistory1.setId(1L);
        ApplicationMetaHistory applicationMetaHistory2 = new ApplicationMetaHistory();
        applicationMetaHistory2.setId(applicationMetaHistory1.getId());
        assertThat(applicationMetaHistory1).isEqualTo(applicationMetaHistory2);
        applicationMetaHistory2.setId(2L);
        assertThat(applicationMetaHistory1).isNotEqualTo(applicationMetaHistory2);
        applicationMetaHistory1.setId(null);
        assertThat(applicationMetaHistory1).isNotEqualTo(applicationMetaHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationMetaHistoryDTO.class);
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO1 = new ApplicationMetaHistoryDTO();
        applicationMetaHistoryDTO1.setId(1L);
        ApplicationMetaHistoryDTO applicationMetaHistoryDTO2 = new ApplicationMetaHistoryDTO();
        assertThat(applicationMetaHistoryDTO1).isNotEqualTo(applicationMetaHistoryDTO2);
        applicationMetaHistoryDTO2.setId(applicationMetaHistoryDTO1.getId());
        assertThat(applicationMetaHistoryDTO1).isEqualTo(applicationMetaHistoryDTO2);
        applicationMetaHistoryDTO2.setId(2L);
        assertThat(applicationMetaHistoryDTO1).isNotEqualTo(applicationMetaHistoryDTO2);
        applicationMetaHistoryDTO1.setId(null);
        assertThat(applicationMetaHistoryDTO1).isNotEqualTo(applicationMetaHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(applicationMetaHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(applicationMetaHistoryMapper.fromId(null)).isNull();
    }
}

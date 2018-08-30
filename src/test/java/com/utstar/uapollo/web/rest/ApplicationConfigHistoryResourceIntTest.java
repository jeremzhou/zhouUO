package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.ApplicationConfigHistory;
import com.utstar.uapollo.repository.ApplicationConfigHistoryRepository;
import com.utstar.uapollo.service.ApplicationConfigHistoryService;
import com.utstar.uapollo.service.dto.ApplicationConfigHistoryDTO;
import com.utstar.uapollo.service.mapper.ApplicationConfigHistoryMapper;
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
 * Test class for the ApplicationConfigHistoryResource REST controller.
 *
 * @see ApplicationConfigHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class ApplicationConfigHistoryResourceIntTest {

    private static final Long DEFAULT_APPLICATION_ID = 1L;
    private static final Long UPDATED_APPLICATION_ID = 2L;

    private static final Long DEFAULT_APPLICATION_CONFIGID = 1L;
    private static final Long UPDATED_APPLICATION_CONFIGID = 2L;

    private static final String DEFAULT_CONFIG_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_CONTENT = "BBBBBBBBBB";

    private static final Operation DEFAULT_OPERATION = Operation.CREATE;
    private static final Operation UPDATED_OPERATION = Operation.UPDATE;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Autowired
    private ApplicationConfigHistoryRepository applicationConfigHistoryRepository;

    @Autowired
    private ApplicationConfigHistoryMapper applicationConfigHistoryMapper;

    @Autowired
    private ApplicationConfigHistoryService applicationConfigHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplicationConfigHistoryMockMvc;

    private ApplicationConfigHistory applicationConfigHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationConfigHistoryResource applicationConfigHistoryResource = new ApplicationConfigHistoryResource(applicationConfigHistoryService);
        this.restApplicationConfigHistoryMockMvc = MockMvcBuilders.standaloneSetup(applicationConfigHistoryResource)
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
    public static ApplicationConfigHistory createEntity(EntityManager em) {
        ApplicationConfigHistory applicationConfigHistory = new ApplicationConfigHistory()
            .applicationId(DEFAULT_APPLICATION_ID)
            .applicationConfigid(DEFAULT_APPLICATION_CONFIGID)
            .configContent(DEFAULT_CONFIG_CONTENT)
            .operation(DEFAULT_OPERATION)
            .createTime(DEFAULT_CREATE_TIME);
        return applicationConfigHistory;
    }

    @Before
    public void initTest() {
        applicationConfigHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationConfigHistory() throws Exception {
        int databaseSizeBeforeCreate = applicationConfigHistoryRepository.findAll().size();

        // Create the ApplicationConfigHistory
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = applicationConfigHistoryMapper.toDto(applicationConfigHistory);
        restApplicationConfigHistoryMockMvc.perform(post("/api/application-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplicationConfigHistory in the database
        List<ApplicationConfigHistory> applicationConfigHistoryList = applicationConfigHistoryRepository.findAll();
        assertThat(applicationConfigHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationConfigHistory testApplicationConfigHistory = applicationConfigHistoryList.get(applicationConfigHistoryList.size() - 1);
        assertThat(testApplicationConfigHistory.getApplicationId()).isEqualTo(DEFAULT_APPLICATION_ID);
        assertThat(testApplicationConfigHistory.getApplicationConfigid()).isEqualTo(DEFAULT_APPLICATION_CONFIGID);
        assertThat(testApplicationConfigHistory.getConfigContent()).isEqualTo(DEFAULT_CONFIG_CONTENT);
        assertThat(testApplicationConfigHistory.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testApplicationConfigHistory.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void createApplicationConfigHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationConfigHistoryRepository.findAll().size();

        // Create the ApplicationConfigHistory with an existing ID
        applicationConfigHistory.setId(1L);
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = applicationConfigHistoryMapper.toDto(applicationConfigHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationConfigHistoryMockMvc.perform(post("/api/application-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationConfigHistory in the database
        List<ApplicationConfigHistory> applicationConfigHistoryList = applicationConfigHistoryRepository.findAll();
        assertThat(applicationConfigHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkApplicationIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationConfigHistoryRepository.findAll().size();
        // set the field null
        applicationConfigHistory.setApplicationId(null);

        // Create the ApplicationConfigHistory, which fails.
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = applicationConfigHistoryMapper.toDto(applicationConfigHistory);

        restApplicationConfigHistoryMockMvc.perform(post("/api/application-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationConfigHistory> applicationConfigHistoryList = applicationConfigHistoryRepository.findAll();
        assertThat(applicationConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApplicationConfigidIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationConfigHistoryRepository.findAll().size();
        // set the field null
        applicationConfigHistory.setApplicationConfigid(null);

        // Create the ApplicationConfigHistory, which fails.
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = applicationConfigHistoryMapper.toDto(applicationConfigHistory);

        restApplicationConfigHistoryMockMvc.perform(post("/api/application-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationConfigHistory> applicationConfigHistoryList = applicationConfigHistoryRepository.findAll();
        assertThat(applicationConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConfigContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationConfigHistoryRepository.findAll().size();
        // set the field null
        applicationConfigHistory.setConfigContent(null);

        // Create the ApplicationConfigHistory, which fails.
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = applicationConfigHistoryMapper.toDto(applicationConfigHistory);

        restApplicationConfigHistoryMockMvc.perform(post("/api/application-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationConfigHistory> applicationConfigHistoryList = applicationConfigHistoryRepository.findAll();
        assertThat(applicationConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationConfigHistoryRepository.findAll().size();
        // set the field null
        applicationConfigHistory.setOperation(null);

        // Create the ApplicationConfigHistory, which fails.
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = applicationConfigHistoryMapper.toDto(applicationConfigHistory);

        restApplicationConfigHistoryMockMvc.perform(post("/api/application-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationConfigHistory> applicationConfigHistoryList = applicationConfigHistoryRepository.findAll();
        assertThat(applicationConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationConfigHistoryRepository.findAll().size();
        // set the field null
        applicationConfigHistory.setCreateTime(null);

        // Create the ApplicationConfigHistory, which fails.
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = applicationConfigHistoryMapper.toDto(applicationConfigHistory);

        restApplicationConfigHistoryMockMvc.perform(post("/api/application-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationConfigHistory> applicationConfigHistoryList = applicationConfigHistoryRepository.findAll();
        assertThat(applicationConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicationConfigHistories() throws Exception {
        // Initialize the database
        applicationConfigHistoryRepository.saveAndFlush(applicationConfigHistory);

        // Get all the applicationConfigHistoryList
        restApplicationConfigHistoryMockMvc.perform(get("/api/application-config-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationConfigHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationId").value(hasItem(DEFAULT_APPLICATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].applicationConfigid").value(hasItem(DEFAULT_APPLICATION_CONFIGID.intValue())))
            .andExpect(jsonPath("$.[*].configContent").value(hasItem(DEFAULT_CONFIG_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getApplicationConfigHistory() throws Exception {
        // Initialize the database
        applicationConfigHistoryRepository.saveAndFlush(applicationConfigHistory);

        // Get the applicationConfigHistory
        restApplicationConfigHistoryMockMvc.perform(get("/api/application-config-histories/{id}", applicationConfigHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applicationConfigHistory.getId().intValue()))
            .andExpect(jsonPath("$.applicationId").value(DEFAULT_APPLICATION_ID.intValue()))
            .andExpect(jsonPath("$.applicationConfigid").value(DEFAULT_APPLICATION_CONFIGID.intValue()))
            .andExpect(jsonPath("$.configContent").value(DEFAULT_CONFIG_CONTENT.toString()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationConfigHistory() throws Exception {
        // Get the applicationConfigHistory
        restApplicationConfigHistoryMockMvc.perform(get("/api/application-config-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationConfigHistory() throws Exception {
        // Initialize the database
        applicationConfigHistoryRepository.saveAndFlush(applicationConfigHistory);
        int databaseSizeBeforeUpdate = applicationConfigHistoryRepository.findAll().size();

        // Update the applicationConfigHistory
        ApplicationConfigHistory updatedApplicationConfigHistory = applicationConfigHistoryRepository.findOne(applicationConfigHistory.getId());
        // Disconnect from session so that the updates on updatedApplicationConfigHistory are not directly saved in db
        em.detach(updatedApplicationConfigHistory);
        updatedApplicationConfigHistory
            .applicationId(UPDATED_APPLICATION_ID)
            .applicationConfigid(UPDATED_APPLICATION_CONFIGID)
            .configContent(UPDATED_CONFIG_CONTENT)
            .operation(UPDATED_OPERATION)
            .createTime(UPDATED_CREATE_TIME);
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = applicationConfigHistoryMapper.toDto(updatedApplicationConfigHistory);

        restApplicationConfigHistoryMockMvc.perform(put("/api/application-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the ApplicationConfigHistory in the database
        List<ApplicationConfigHistory> applicationConfigHistoryList = applicationConfigHistoryRepository.findAll();
        assertThat(applicationConfigHistoryList).hasSize(databaseSizeBeforeUpdate);
        ApplicationConfigHistory testApplicationConfigHistory = applicationConfigHistoryList.get(applicationConfigHistoryList.size() - 1);
        assertThat(testApplicationConfigHistory.getApplicationId()).isEqualTo(UPDATED_APPLICATION_ID);
        assertThat(testApplicationConfigHistory.getApplicationConfigid()).isEqualTo(UPDATED_APPLICATION_CONFIGID);
        assertThat(testApplicationConfigHistory.getConfigContent()).isEqualTo(UPDATED_CONFIG_CONTENT);
        assertThat(testApplicationConfigHistory.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testApplicationConfigHistory.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationConfigHistory() throws Exception {
        int databaseSizeBeforeUpdate = applicationConfigHistoryRepository.findAll().size();

        // Create the ApplicationConfigHistory
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = applicationConfigHistoryMapper.toDto(applicationConfigHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApplicationConfigHistoryMockMvc.perform(put("/api/application-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplicationConfigHistory in the database
        List<ApplicationConfigHistory> applicationConfigHistoryList = applicationConfigHistoryRepository.findAll();
        assertThat(applicationConfigHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApplicationConfigHistory() throws Exception {
        // Initialize the database
        applicationConfigHistoryRepository.saveAndFlush(applicationConfigHistory);
        int databaseSizeBeforeDelete = applicationConfigHistoryRepository.findAll().size();

        // Get the applicationConfigHistory
        restApplicationConfigHistoryMockMvc.perform(delete("/api/application-config-histories/{id}", applicationConfigHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ApplicationConfigHistory> applicationConfigHistoryList = applicationConfigHistoryRepository.findAll();
        assertThat(applicationConfigHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationConfigHistory.class);
        ApplicationConfigHistory applicationConfigHistory1 = new ApplicationConfigHistory();
        applicationConfigHistory1.setId(1L);
        ApplicationConfigHistory applicationConfigHistory2 = new ApplicationConfigHistory();
        applicationConfigHistory2.setId(applicationConfigHistory1.getId());
        assertThat(applicationConfigHistory1).isEqualTo(applicationConfigHistory2);
        applicationConfigHistory2.setId(2L);
        assertThat(applicationConfigHistory1).isNotEqualTo(applicationConfigHistory2);
        applicationConfigHistory1.setId(null);
        assertThat(applicationConfigHistory1).isNotEqualTo(applicationConfigHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationConfigHistoryDTO.class);
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO1 = new ApplicationConfigHistoryDTO();
        applicationConfigHistoryDTO1.setId(1L);
        ApplicationConfigHistoryDTO applicationConfigHistoryDTO2 = new ApplicationConfigHistoryDTO();
        assertThat(applicationConfigHistoryDTO1).isNotEqualTo(applicationConfigHistoryDTO2);
        applicationConfigHistoryDTO2.setId(applicationConfigHistoryDTO1.getId());
        assertThat(applicationConfigHistoryDTO1).isEqualTo(applicationConfigHistoryDTO2);
        applicationConfigHistoryDTO2.setId(2L);
        assertThat(applicationConfigHistoryDTO1).isNotEqualTo(applicationConfigHistoryDTO2);
        applicationConfigHistoryDTO1.setId(null);
        assertThat(applicationConfigHistoryDTO1).isNotEqualTo(applicationConfigHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(applicationConfigHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(applicationConfigHistoryMapper.fromId(null)).isNull();
    }
}

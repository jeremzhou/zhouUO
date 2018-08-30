package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.ApplicationHistory;
import com.utstar.uapollo.repository.ApplicationHistoryRepository;
import com.utstar.uapollo.service.ApplicationHistoryService;
import com.utstar.uapollo.service.dto.ApplicationHistoryDTO;
import com.utstar.uapollo.service.mapper.ApplicationHistoryMapper;
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
 * Test class for the ApplicationHistoryResource REST controller.
 *
 * @see ApplicationHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class ApplicationHistoryResourceIntTest {

    private static final Long DEFAULT_APPLICATION_META_ID = 1L;
    private static final Long UPDATED_APPLICATION_META_ID = 2L;

    private static final Long DEFAULT_SERVER_ID = 1L;
    private static final Long UPDATED_SERVER_ID = 2L;

    private static final Long DEFAULT_APPLICATION_ID = 1L;
    private static final Long UPDATED_APPLICATION_ID = 2L;

    private static final Operation DEFAULT_OPERATION = Operation.CREATE;
    private static final Operation UPDATED_OPERATION = Operation.UPDATE;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Autowired
    private ApplicationHistoryRepository applicationHistoryRepository;

    @Autowired
    private ApplicationHistoryMapper applicationHistoryMapper;

    @Autowired
    private ApplicationHistoryService applicationHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplicationHistoryMockMvc;

    private ApplicationHistory applicationHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationHistoryResource applicationHistoryResource = new ApplicationHistoryResource(applicationHistoryService);
        this.restApplicationHistoryMockMvc = MockMvcBuilders.standaloneSetup(applicationHistoryResource)
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
    public static ApplicationHistory createEntity(EntityManager em) {
        ApplicationHistory applicationHistory = new ApplicationHistory()
            .applicationMetaId(DEFAULT_APPLICATION_META_ID)
            .serverId(DEFAULT_SERVER_ID)
            .applicationId(DEFAULT_APPLICATION_ID)
            .operation(DEFAULT_OPERATION)
            .createTime(DEFAULT_CREATE_TIME);
        return applicationHistory;
    }

    @Before
    public void initTest() {
        applicationHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationHistory() throws Exception {
        int databaseSizeBeforeCreate = applicationHistoryRepository.findAll().size();

        // Create the ApplicationHistory
        ApplicationHistoryDTO applicationHistoryDTO = applicationHistoryMapper.toDto(applicationHistory);
        restApplicationHistoryMockMvc.perform(post("/api/application-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplicationHistory in the database
        List<ApplicationHistory> applicationHistoryList = applicationHistoryRepository.findAll();
        assertThat(applicationHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationHistory testApplicationHistory = applicationHistoryList.get(applicationHistoryList.size() - 1);
        assertThat(testApplicationHistory.getApplicationMetaId()).isEqualTo(DEFAULT_APPLICATION_META_ID);
        assertThat(testApplicationHistory.getServerId()).isEqualTo(DEFAULT_SERVER_ID);
        assertThat(testApplicationHistory.getApplicationId()).isEqualTo(DEFAULT_APPLICATION_ID);
        assertThat(testApplicationHistory.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testApplicationHistory.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void createApplicationHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationHistoryRepository.findAll().size();

        // Create the ApplicationHistory with an existing ID
        applicationHistory.setId(1L);
        ApplicationHistoryDTO applicationHistoryDTO = applicationHistoryMapper.toDto(applicationHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationHistoryMockMvc.perform(post("/api/application-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationHistory in the database
        List<ApplicationHistory> applicationHistoryList = applicationHistoryRepository.findAll();
        assertThat(applicationHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkApplicationMetaIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationHistoryRepository.findAll().size();
        // set the field null
        applicationHistory.setApplicationMetaId(null);

        // Create the ApplicationHistory, which fails.
        ApplicationHistoryDTO applicationHistoryDTO = applicationHistoryMapper.toDto(applicationHistory);

        restApplicationHistoryMockMvc.perform(post("/api/application-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationHistory> applicationHistoryList = applicationHistoryRepository.findAll();
        assertThat(applicationHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationHistoryRepository.findAll().size();
        // set the field null
        applicationHistory.setServerId(null);

        // Create the ApplicationHistory, which fails.
        ApplicationHistoryDTO applicationHistoryDTO = applicationHistoryMapper.toDto(applicationHistory);

        restApplicationHistoryMockMvc.perform(post("/api/application-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationHistory> applicationHistoryList = applicationHistoryRepository.findAll();
        assertThat(applicationHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApplicationIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationHistoryRepository.findAll().size();
        // set the field null
        applicationHistory.setApplicationId(null);

        // Create the ApplicationHistory, which fails.
        ApplicationHistoryDTO applicationHistoryDTO = applicationHistoryMapper.toDto(applicationHistory);

        restApplicationHistoryMockMvc.perform(post("/api/application-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationHistory> applicationHistoryList = applicationHistoryRepository.findAll();
        assertThat(applicationHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationHistoryRepository.findAll().size();
        // set the field null
        applicationHistory.setOperation(null);

        // Create the ApplicationHistory, which fails.
        ApplicationHistoryDTO applicationHistoryDTO = applicationHistoryMapper.toDto(applicationHistory);

        restApplicationHistoryMockMvc.perform(post("/api/application-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationHistory> applicationHistoryList = applicationHistoryRepository.findAll();
        assertThat(applicationHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationHistoryRepository.findAll().size();
        // set the field null
        applicationHistory.setCreateTime(null);

        // Create the ApplicationHistory, which fails.
        ApplicationHistoryDTO applicationHistoryDTO = applicationHistoryMapper.toDto(applicationHistory);

        restApplicationHistoryMockMvc.perform(post("/api/application-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationHistory> applicationHistoryList = applicationHistoryRepository.findAll();
        assertThat(applicationHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicationHistories() throws Exception {
        // Initialize the database
        applicationHistoryRepository.saveAndFlush(applicationHistory);

        // Get all the applicationHistoryList
        restApplicationHistoryMockMvc.perform(get("/api/application-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationMetaId").value(hasItem(DEFAULT_APPLICATION_META_ID.intValue())))
            .andExpect(jsonPath("$.[*].serverId").value(hasItem(DEFAULT_SERVER_ID.intValue())))
            .andExpect(jsonPath("$.[*].applicationId").value(hasItem(DEFAULT_APPLICATION_ID.intValue())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getApplicationHistory() throws Exception {
        // Initialize the database
        applicationHistoryRepository.saveAndFlush(applicationHistory);

        // Get the applicationHistory
        restApplicationHistoryMockMvc.perform(get("/api/application-histories/{id}", applicationHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applicationHistory.getId().intValue()))
            .andExpect(jsonPath("$.applicationMetaId").value(DEFAULT_APPLICATION_META_ID.intValue()))
            .andExpect(jsonPath("$.serverId").value(DEFAULT_SERVER_ID.intValue()))
            .andExpect(jsonPath("$.applicationId").value(DEFAULT_APPLICATION_ID.intValue()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationHistory() throws Exception {
        // Get the applicationHistory
        restApplicationHistoryMockMvc.perform(get("/api/application-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationHistory() throws Exception {
        // Initialize the database
        applicationHistoryRepository.saveAndFlush(applicationHistory);
        int databaseSizeBeforeUpdate = applicationHistoryRepository.findAll().size();

        // Update the applicationHistory
        ApplicationHistory updatedApplicationHistory = applicationHistoryRepository.findOne(applicationHistory.getId());
        // Disconnect from session so that the updates on updatedApplicationHistory are not directly saved in db
        em.detach(updatedApplicationHistory);
        updatedApplicationHistory
            .applicationMetaId(UPDATED_APPLICATION_META_ID)
            .serverId(UPDATED_SERVER_ID)
            .applicationId(UPDATED_APPLICATION_ID)
            .operation(UPDATED_OPERATION)
            .createTime(UPDATED_CREATE_TIME);
        ApplicationHistoryDTO applicationHistoryDTO = applicationHistoryMapper.toDto(updatedApplicationHistory);

        restApplicationHistoryMockMvc.perform(put("/api/application-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the ApplicationHistory in the database
        List<ApplicationHistory> applicationHistoryList = applicationHistoryRepository.findAll();
        assertThat(applicationHistoryList).hasSize(databaseSizeBeforeUpdate);
        ApplicationHistory testApplicationHistory = applicationHistoryList.get(applicationHistoryList.size() - 1);
        assertThat(testApplicationHistory.getApplicationMetaId()).isEqualTo(UPDATED_APPLICATION_META_ID);
        assertThat(testApplicationHistory.getServerId()).isEqualTo(UPDATED_SERVER_ID);
        assertThat(testApplicationHistory.getApplicationId()).isEqualTo(UPDATED_APPLICATION_ID);
        assertThat(testApplicationHistory.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testApplicationHistory.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationHistory() throws Exception {
        int databaseSizeBeforeUpdate = applicationHistoryRepository.findAll().size();

        // Create the ApplicationHistory
        ApplicationHistoryDTO applicationHistoryDTO = applicationHistoryMapper.toDto(applicationHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restApplicationHistoryMockMvc.perform(put("/api/application-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ApplicationHistory in the database
        List<ApplicationHistory> applicationHistoryList = applicationHistoryRepository.findAll();
        assertThat(applicationHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteApplicationHistory() throws Exception {
        // Initialize the database
        applicationHistoryRepository.saveAndFlush(applicationHistory);
        int databaseSizeBeforeDelete = applicationHistoryRepository.findAll().size();

        // Get the applicationHistory
        restApplicationHistoryMockMvc.perform(delete("/api/application-histories/{id}", applicationHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ApplicationHistory> applicationHistoryList = applicationHistoryRepository.findAll();
        assertThat(applicationHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationHistory.class);
        ApplicationHistory applicationHistory1 = new ApplicationHistory();
        applicationHistory1.setId(1L);
        ApplicationHistory applicationHistory2 = new ApplicationHistory();
        applicationHistory2.setId(applicationHistory1.getId());
        assertThat(applicationHistory1).isEqualTo(applicationHistory2);
        applicationHistory2.setId(2L);
        assertThat(applicationHistory1).isNotEqualTo(applicationHistory2);
        applicationHistory1.setId(null);
        assertThat(applicationHistory1).isNotEqualTo(applicationHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationHistoryDTO.class);
        ApplicationHistoryDTO applicationHistoryDTO1 = new ApplicationHistoryDTO();
        applicationHistoryDTO1.setId(1L);
        ApplicationHistoryDTO applicationHistoryDTO2 = new ApplicationHistoryDTO();
        assertThat(applicationHistoryDTO1).isNotEqualTo(applicationHistoryDTO2);
        applicationHistoryDTO2.setId(applicationHistoryDTO1.getId());
        assertThat(applicationHistoryDTO1).isEqualTo(applicationHistoryDTO2);
        applicationHistoryDTO2.setId(2L);
        assertThat(applicationHistoryDTO1).isNotEqualTo(applicationHistoryDTO2);
        applicationHistoryDTO1.setId(null);
        assertThat(applicationHistoryDTO1).isNotEqualTo(applicationHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(applicationHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(applicationHistoryMapper.fromId(null)).isNull();
    }
}

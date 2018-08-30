package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.ProjectHistory;
import com.utstar.uapollo.repository.ProjectHistoryRepository;
import com.utstar.uapollo.service.ProjectHistoryService;
import com.utstar.uapollo.service.dto.ProjectHistoryDTO;
import com.utstar.uapollo.service.mapper.ProjectHistoryMapper;
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
 * Test class for the ProjectHistoryResource REST controller.
 *
 * @see ProjectHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class ProjectHistoryResourceIntTest {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long UPDATED_PROJECT_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_USER_ID = 1;
    private static final Integer UPDATED_USER_ID = 2;

    private static final Operation DEFAULT_OPERATION = Operation.CREATE;
    private static final Operation UPDATED_OPERATION = Operation.UPDATE;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Autowired
    private ProjectHistoryRepository projectHistoryRepository;

    @Autowired
    private ProjectHistoryMapper projectHistoryMapper;

    @Autowired
    private ProjectHistoryService projectHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectHistoryMockMvc;

    private ProjectHistory projectHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectHistoryResource projectHistoryResource = new ProjectHistoryResource(projectHistoryService);
        this.restProjectHistoryMockMvc = MockMvcBuilders.standaloneSetup(projectHistoryResource)
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
    public static ProjectHistory createEntity(EntityManager em) {
        ProjectHistory projectHistory = new ProjectHistory()
            .projectId(DEFAULT_PROJECT_ID)
            .name(DEFAULT_NAME)
            .userId(DEFAULT_USER_ID)
            .operation(DEFAULT_OPERATION)
            .createTime(DEFAULT_CREATE_TIME);
        return projectHistory;
    }

    @Before
    public void initTest() {
        projectHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectHistory() throws Exception {
        int databaseSizeBeforeCreate = projectHistoryRepository.findAll().size();

        // Create the ProjectHistory
        ProjectHistoryDTO projectHistoryDTO = projectHistoryMapper.toDto(projectHistory);
        restProjectHistoryMockMvc.perform(post("/api/project-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectHistory in the database
        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectHistory testProjectHistory = projectHistoryList.get(projectHistoryList.size() - 1);
        assertThat(testProjectHistory.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
        assertThat(testProjectHistory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProjectHistory.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testProjectHistory.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testProjectHistory.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void createProjectHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectHistoryRepository.findAll().size();

        // Create the ProjectHistory with an existing ID
        projectHistory.setId(1L);
        ProjectHistoryDTO projectHistoryDTO = projectHistoryMapper.toDto(projectHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectHistoryMockMvc.perform(post("/api/project-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectHistory in the database
        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectHistoryRepository.findAll().size();
        // set the field null
        projectHistory.setProjectId(null);

        // Create the ProjectHistory, which fails.
        ProjectHistoryDTO projectHistoryDTO = projectHistoryMapper.toDto(projectHistory);

        restProjectHistoryMockMvc.perform(post("/api/project-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectHistoryRepository.findAll().size();
        // set the field null
        projectHistory.setName(null);

        // Create the ProjectHistory, which fails.
        ProjectHistoryDTO projectHistoryDTO = projectHistoryMapper.toDto(projectHistory);

        restProjectHistoryMockMvc.perform(post("/api/project-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectHistoryRepository.findAll().size();
        // set the field null
        projectHistory.setUserId(null);

        // Create the ProjectHistory, which fails.
        ProjectHistoryDTO projectHistoryDTO = projectHistoryMapper.toDto(projectHistory);

        restProjectHistoryMockMvc.perform(post("/api/project-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectHistoryRepository.findAll().size();
        // set the field null
        projectHistory.setOperation(null);

        // Create the ProjectHistory, which fails.
        ProjectHistoryDTO projectHistoryDTO = projectHistoryMapper.toDto(projectHistory);

        restProjectHistoryMockMvc.perform(post("/api/project-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectHistoryRepository.findAll().size();
        // set the field null
        projectHistory.setCreateTime(null);

        // Create the ProjectHistory, which fails.
        ProjectHistoryDTO projectHistoryDTO = projectHistoryMapper.toDto(projectHistory);

        restProjectHistoryMockMvc.perform(post("/api/project-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjectHistories() throws Exception {
        // Initialize the database
        projectHistoryRepository.saveAndFlush(projectHistory);

        // Get all the projectHistoryList
        restProjectHistoryMockMvc.perform(get("/api/project-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getProjectHistory() throws Exception {
        // Initialize the database
        projectHistoryRepository.saveAndFlush(projectHistory);

        // Get the projectHistory
        restProjectHistoryMockMvc.perform(get("/api/project-histories/{id}", projectHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectHistory.getId().intValue()))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectHistory() throws Exception {
        // Get the projectHistory
        restProjectHistoryMockMvc.perform(get("/api/project-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectHistory() throws Exception {
        // Initialize the database
        projectHistoryRepository.saveAndFlush(projectHistory);
        int databaseSizeBeforeUpdate = projectHistoryRepository.findAll().size();

        // Update the projectHistory
        ProjectHistory updatedProjectHistory = projectHistoryRepository.findOne(projectHistory.getId());
        // Disconnect from session so that the updates on updatedProjectHistory are not directly saved in db
        em.detach(updatedProjectHistory);
        updatedProjectHistory
            .projectId(UPDATED_PROJECT_ID)
            .name(UPDATED_NAME)
            .userId(UPDATED_USER_ID)
            .operation(UPDATED_OPERATION)
            .createTime(UPDATED_CREATE_TIME);
        ProjectHistoryDTO projectHistoryDTO = projectHistoryMapper.toDto(updatedProjectHistory);

        restProjectHistoryMockMvc.perform(put("/api/project-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the ProjectHistory in the database
        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeUpdate);
        ProjectHistory testProjectHistory = projectHistoryList.get(projectHistoryList.size() - 1);
        assertThat(testProjectHistory.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
        assertThat(testProjectHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProjectHistory.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testProjectHistory.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testProjectHistory.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectHistory() throws Exception {
        int databaseSizeBeforeUpdate = projectHistoryRepository.findAll().size();

        // Create the ProjectHistory
        ProjectHistoryDTO projectHistoryDTO = projectHistoryMapper.toDto(projectHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProjectHistoryMockMvc.perform(put("/api/project-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ProjectHistory in the database
        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProjectHistory() throws Exception {
        // Initialize the database
        projectHistoryRepository.saveAndFlush(projectHistory);
        int databaseSizeBeforeDelete = projectHistoryRepository.findAll().size();

        // Get the projectHistory
        restProjectHistoryMockMvc.perform(delete("/api/project-histories/{id}", projectHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectHistory> projectHistoryList = projectHistoryRepository.findAll();
        assertThat(projectHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectHistory.class);
        ProjectHistory projectHistory1 = new ProjectHistory();
        projectHistory1.setId(1L);
        ProjectHistory projectHistory2 = new ProjectHistory();
        projectHistory2.setId(projectHistory1.getId());
        assertThat(projectHistory1).isEqualTo(projectHistory2);
        projectHistory2.setId(2L);
        assertThat(projectHistory1).isNotEqualTo(projectHistory2);
        projectHistory1.setId(null);
        assertThat(projectHistory1).isNotEqualTo(projectHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectHistoryDTO.class);
        ProjectHistoryDTO projectHistoryDTO1 = new ProjectHistoryDTO();
        projectHistoryDTO1.setId(1L);
        ProjectHistoryDTO projectHistoryDTO2 = new ProjectHistoryDTO();
        assertThat(projectHistoryDTO1).isNotEqualTo(projectHistoryDTO2);
        projectHistoryDTO2.setId(projectHistoryDTO1.getId());
        assertThat(projectHistoryDTO1).isEqualTo(projectHistoryDTO2);
        projectHistoryDTO2.setId(2L);
        assertThat(projectHistoryDTO1).isNotEqualTo(projectHistoryDTO2);
        projectHistoryDTO1.setId(null);
        assertThat(projectHistoryDTO1).isNotEqualTo(projectHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(projectHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(projectHistoryMapper.fromId(null)).isNull();
    }
}

package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.NodeHistory;
import com.utstar.uapollo.repository.NodeHistoryRepository;
import com.utstar.uapollo.service.NodeHistoryService;
import com.utstar.uapollo.service.dto.NodeHistoryDTO;
import com.utstar.uapollo.service.mapper.NodeHistoryMapper;
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
 * Test class for the NodeHistoryResource REST controller.
 *
 * @see NodeHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class NodeHistoryResourceIntTest {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long UPDATED_PROJECT_ID = 2L;

    private static final Long DEFAULT_NODE_ID = 1L;
    private static final Long UPDATED_NODE_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Operation DEFAULT_OPERATION = Operation.CREATE;
    private static final Operation UPDATED_OPERATION = Operation.UPDATE;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Autowired
    private NodeHistoryRepository nodeHistoryRepository;

    @Autowired
    private NodeHistoryMapper nodeHistoryMapper;

    @Autowired
    private NodeHistoryService nodeHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNodeHistoryMockMvc;

    private NodeHistory nodeHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NodeHistoryResource nodeHistoryResource = new NodeHistoryResource(nodeHistoryService);
        this.restNodeHistoryMockMvc = MockMvcBuilders.standaloneSetup(nodeHistoryResource)
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
    public static NodeHistory createEntity(EntityManager em) {
        NodeHistory nodeHistory = new NodeHistory()
            .projectId(DEFAULT_PROJECT_ID)
            .nodeId(DEFAULT_NODE_ID)
            .name(DEFAULT_NAME)
            .operation(DEFAULT_OPERATION)
            .createTime(DEFAULT_CREATE_TIME);
        return nodeHistory;
    }

    @Before
    public void initTest() {
        nodeHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createNodeHistory() throws Exception {
        int databaseSizeBeforeCreate = nodeHistoryRepository.findAll().size();

        // Create the NodeHistory
        NodeHistoryDTO nodeHistoryDTO = nodeHistoryMapper.toDto(nodeHistory);
        restNodeHistoryMockMvc.perform(post("/api/node-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the NodeHistory in the database
        List<NodeHistory> nodeHistoryList = nodeHistoryRepository.findAll();
        assertThat(nodeHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        NodeHistory testNodeHistory = nodeHistoryList.get(nodeHistoryList.size() - 1);
        assertThat(testNodeHistory.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
        assertThat(testNodeHistory.getNodeId()).isEqualTo(DEFAULT_NODE_ID);
        assertThat(testNodeHistory.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNodeHistory.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testNodeHistory.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void createNodeHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nodeHistoryRepository.findAll().size();

        // Create the NodeHistory with an existing ID
        nodeHistory.setId(1L);
        NodeHistoryDTO nodeHistoryDTO = nodeHistoryMapper.toDto(nodeHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNodeHistoryMockMvc.perform(post("/api/node-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NodeHistory in the database
        List<NodeHistory> nodeHistoryList = nodeHistoryRepository.findAll();
        assertThat(nodeHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkProjectIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeHistoryRepository.findAll().size();
        // set the field null
        nodeHistory.setProjectId(null);

        // Create the NodeHistory, which fails.
        NodeHistoryDTO nodeHistoryDTO = nodeHistoryMapper.toDto(nodeHistory);

        restNodeHistoryMockMvc.perform(post("/api/node-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeHistory> nodeHistoryList = nodeHistoryRepository.findAll();
        assertThat(nodeHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNodeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeHistoryRepository.findAll().size();
        // set the field null
        nodeHistory.setNodeId(null);

        // Create the NodeHistory, which fails.
        NodeHistoryDTO nodeHistoryDTO = nodeHistoryMapper.toDto(nodeHistory);

        restNodeHistoryMockMvc.perform(post("/api/node-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeHistory> nodeHistoryList = nodeHistoryRepository.findAll();
        assertThat(nodeHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeHistoryRepository.findAll().size();
        // set the field null
        nodeHistory.setName(null);

        // Create the NodeHistory, which fails.
        NodeHistoryDTO nodeHistoryDTO = nodeHistoryMapper.toDto(nodeHistory);

        restNodeHistoryMockMvc.perform(post("/api/node-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeHistory> nodeHistoryList = nodeHistoryRepository.findAll();
        assertThat(nodeHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeHistoryRepository.findAll().size();
        // set the field null
        nodeHistory.setOperation(null);

        // Create the NodeHistory, which fails.
        NodeHistoryDTO nodeHistoryDTO = nodeHistoryMapper.toDto(nodeHistory);

        restNodeHistoryMockMvc.perform(post("/api/node-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeHistory> nodeHistoryList = nodeHistoryRepository.findAll();
        assertThat(nodeHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeHistoryRepository.findAll().size();
        // set the field null
        nodeHistory.setCreateTime(null);

        // Create the NodeHistory, which fails.
        NodeHistoryDTO nodeHistoryDTO = nodeHistoryMapper.toDto(nodeHistory);

        restNodeHistoryMockMvc.perform(post("/api/node-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeHistory> nodeHistoryList = nodeHistoryRepository.findAll();
        assertThat(nodeHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNodeHistories() throws Exception {
        // Initialize the database
        nodeHistoryRepository.saveAndFlush(nodeHistory);

        // Get all the nodeHistoryList
        restNodeHistoryMockMvc.perform(get("/api/node-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nodeHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].nodeId").value(hasItem(DEFAULT_NODE_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getNodeHistory() throws Exception {
        // Initialize the database
        nodeHistoryRepository.saveAndFlush(nodeHistory);

        // Get the nodeHistory
        restNodeHistoryMockMvc.perform(get("/api/node-histories/{id}", nodeHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nodeHistory.getId().intValue()))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.intValue()))
            .andExpect(jsonPath("$.nodeId").value(DEFAULT_NODE_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNodeHistory() throws Exception {
        // Get the nodeHistory
        restNodeHistoryMockMvc.perform(get("/api/node-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNodeHistory() throws Exception {
        // Initialize the database
        nodeHistoryRepository.saveAndFlush(nodeHistory);
        int databaseSizeBeforeUpdate = nodeHistoryRepository.findAll().size();

        // Update the nodeHistory
        NodeHistory updatedNodeHistory = nodeHistoryRepository.findOne(nodeHistory.getId());
        // Disconnect from session so that the updates on updatedNodeHistory are not directly saved in db
        em.detach(updatedNodeHistory);
        updatedNodeHistory
            .projectId(UPDATED_PROJECT_ID)
            .nodeId(UPDATED_NODE_ID)
            .name(UPDATED_NAME)
            .operation(UPDATED_OPERATION)
            .createTime(UPDATED_CREATE_TIME);
        NodeHistoryDTO nodeHistoryDTO = nodeHistoryMapper.toDto(updatedNodeHistory);

        restNodeHistoryMockMvc.perform(put("/api/node-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the NodeHistory in the database
        List<NodeHistory> nodeHistoryList = nodeHistoryRepository.findAll();
        assertThat(nodeHistoryList).hasSize(databaseSizeBeforeUpdate);
        NodeHistory testNodeHistory = nodeHistoryList.get(nodeHistoryList.size() - 1);
        assertThat(testNodeHistory.getProjectId()).isEqualTo(UPDATED_PROJECT_ID);
        assertThat(testNodeHistory.getNodeId()).isEqualTo(UPDATED_NODE_ID);
        assertThat(testNodeHistory.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNodeHistory.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testNodeHistory.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingNodeHistory() throws Exception {
        int databaseSizeBeforeUpdate = nodeHistoryRepository.findAll().size();

        // Create the NodeHistory
        NodeHistoryDTO nodeHistoryDTO = nodeHistoryMapper.toDto(nodeHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNodeHistoryMockMvc.perform(put("/api/node-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the NodeHistory in the database
        List<NodeHistory> nodeHistoryList = nodeHistoryRepository.findAll();
        assertThat(nodeHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNodeHistory() throws Exception {
        // Initialize the database
        nodeHistoryRepository.saveAndFlush(nodeHistory);
        int databaseSizeBeforeDelete = nodeHistoryRepository.findAll().size();

        // Get the nodeHistory
        restNodeHistoryMockMvc.perform(delete("/api/node-histories/{id}", nodeHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NodeHistory> nodeHistoryList = nodeHistoryRepository.findAll();
        assertThat(nodeHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NodeHistory.class);
        NodeHistory nodeHistory1 = new NodeHistory();
        nodeHistory1.setId(1L);
        NodeHistory nodeHistory2 = new NodeHistory();
        nodeHistory2.setId(nodeHistory1.getId());
        assertThat(nodeHistory1).isEqualTo(nodeHistory2);
        nodeHistory2.setId(2L);
        assertThat(nodeHistory1).isNotEqualTo(nodeHistory2);
        nodeHistory1.setId(null);
        assertThat(nodeHistory1).isNotEqualTo(nodeHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NodeHistoryDTO.class);
        NodeHistoryDTO nodeHistoryDTO1 = new NodeHistoryDTO();
        nodeHistoryDTO1.setId(1L);
        NodeHistoryDTO nodeHistoryDTO2 = new NodeHistoryDTO();
        assertThat(nodeHistoryDTO1).isNotEqualTo(nodeHistoryDTO2);
        nodeHistoryDTO2.setId(nodeHistoryDTO1.getId());
        assertThat(nodeHistoryDTO1).isEqualTo(nodeHistoryDTO2);
        nodeHistoryDTO2.setId(2L);
        assertThat(nodeHistoryDTO1).isNotEqualTo(nodeHistoryDTO2);
        nodeHistoryDTO1.setId(null);
        assertThat(nodeHistoryDTO1).isNotEqualTo(nodeHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(nodeHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(nodeHistoryMapper.fromId(null)).isNull();
    }
}

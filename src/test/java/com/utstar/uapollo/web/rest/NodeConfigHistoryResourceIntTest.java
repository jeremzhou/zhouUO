package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.NodeConfigHistory;
import com.utstar.uapollo.repository.NodeConfigHistoryRepository;
import com.utstar.uapollo.service.NodeConfigHistoryService;
import com.utstar.uapollo.service.dto.NodeConfigHistoryDTO;
import com.utstar.uapollo.service.mapper.NodeConfigHistoryMapper;
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
 * Test class for the NodeConfigHistoryResource REST controller.
 *
 * @see NodeConfigHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class NodeConfigHistoryResourceIntTest {

    private static final Long DEFAULT_APPLICATION_META_ID = 1L;
    private static final Long UPDATED_APPLICATION_META_ID = 2L;

    private static final Long DEFAULT_NODE_ID = 1L;
    private static final Long UPDATED_NODE_ID = 2L;

    private static final Long DEFAULT_NODE_CONFIG_ID = 1L;
    private static final Long UPDATED_NODE_CONFIG_ID = 2L;

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Operation DEFAULT_OPERATION = Operation.CREATE;
    private static final Operation UPDATED_OPERATION = Operation.UPDATE;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Autowired
    private NodeConfigHistoryRepository nodeConfigHistoryRepository;

    @Autowired
    private NodeConfigHistoryMapper nodeConfigHistoryMapper;

    @Autowired
    private NodeConfigHistoryService nodeConfigHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNodeConfigHistoryMockMvc;

    private NodeConfigHistory nodeConfigHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NodeConfigHistoryResource nodeConfigHistoryResource = new NodeConfigHistoryResource(nodeConfigHistoryService);
        this.restNodeConfigHistoryMockMvc = MockMvcBuilders.standaloneSetup(nodeConfigHistoryResource)
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
    public static NodeConfigHistory createEntity(EntityManager em) {
        NodeConfigHistory nodeConfigHistory = new NodeConfigHistory()
            .applicationMetaId(DEFAULT_APPLICATION_META_ID)
            .nodeId(DEFAULT_NODE_ID)
            .nodeConfigId(DEFAULT_NODE_CONFIG_ID)
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .operation(DEFAULT_OPERATION)
            .createTime(DEFAULT_CREATE_TIME);
        return nodeConfigHistory;
    }

    @Before
    public void initTest() {
        nodeConfigHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createNodeConfigHistory() throws Exception {
        int databaseSizeBeforeCreate = nodeConfigHistoryRepository.findAll().size();

        // Create the NodeConfigHistory
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryMapper.toDto(nodeConfigHistory);
        restNodeConfigHistoryMockMvc.perform(post("/api/node-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the NodeConfigHistory in the database
        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        NodeConfigHistory testNodeConfigHistory = nodeConfigHistoryList.get(nodeConfigHistoryList.size() - 1);
        assertThat(testNodeConfigHistory.getApplicationMetaId()).isEqualTo(DEFAULT_APPLICATION_META_ID);
        assertThat(testNodeConfigHistory.getNodeId()).isEqualTo(DEFAULT_NODE_ID);
        assertThat(testNodeConfigHistory.getNodeConfigId()).isEqualTo(DEFAULT_NODE_CONFIG_ID);
        assertThat(testNodeConfigHistory.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testNodeConfigHistory.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testNodeConfigHistory.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testNodeConfigHistory.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void createNodeConfigHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nodeConfigHistoryRepository.findAll().size();

        // Create the NodeConfigHistory with an existing ID
        nodeConfigHistory.setId(1L);
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryMapper.toDto(nodeConfigHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNodeConfigHistoryMockMvc.perform(post("/api/node-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NodeConfigHistory in the database
        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkApplicationMetaIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeConfigHistoryRepository.findAll().size();
        // set the field null
        nodeConfigHistory.setApplicationMetaId(null);

        // Create the NodeConfigHistory, which fails.
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryMapper.toDto(nodeConfigHistory);

        restNodeConfigHistoryMockMvc.perform(post("/api/node-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNodeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeConfigHistoryRepository.findAll().size();
        // set the field null
        nodeConfigHistory.setNodeId(null);

        // Create the NodeConfigHistory, which fails.
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryMapper.toDto(nodeConfigHistory);

        restNodeConfigHistoryMockMvc.perform(post("/api/node-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNodeConfigIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeConfigHistoryRepository.findAll().size();
        // set the field null
        nodeConfigHistory.setNodeConfigId(null);

        // Create the NodeConfigHistory, which fails.
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryMapper.toDto(nodeConfigHistory);

        restNodeConfigHistoryMockMvc.perform(post("/api/node-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeConfigHistoryRepository.findAll().size();
        // set the field null
        nodeConfigHistory.setKey(null);

        // Create the NodeConfigHistory, which fails.
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryMapper.toDto(nodeConfigHistory);

        restNodeConfigHistoryMockMvc.perform(post("/api/node-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeConfigHistoryRepository.findAll().size();
        // set the field null
        nodeConfigHistory.setValue(null);

        // Create the NodeConfigHistory, which fails.
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryMapper.toDto(nodeConfigHistory);

        restNodeConfigHistoryMockMvc.perform(post("/api/node-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeConfigHistoryRepository.findAll().size();
        // set the field null
        nodeConfigHistory.setOperation(null);

        // Create the NodeConfigHistory, which fails.
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryMapper.toDto(nodeConfigHistory);

        restNodeConfigHistoryMockMvc.perform(post("/api/node-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeConfigHistoryRepository.findAll().size();
        // set the field null
        nodeConfigHistory.setCreateTime(null);

        // Create the NodeConfigHistory, which fails.
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryMapper.toDto(nodeConfigHistory);

        restNodeConfigHistoryMockMvc.perform(post("/api/node-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNodeConfigHistories() throws Exception {
        // Initialize the database
        nodeConfigHistoryRepository.saveAndFlush(nodeConfigHistory);

        // Get all the nodeConfigHistoryList
        restNodeConfigHistoryMockMvc.perform(get("/api/node-config-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nodeConfigHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].applicationMetaId").value(hasItem(DEFAULT_APPLICATION_META_ID.intValue())))
            .andExpect(jsonPath("$.[*].nodeId").value(hasItem(DEFAULT_NODE_ID.intValue())))
            .andExpect(jsonPath("$.[*].nodeConfigId").value(hasItem(DEFAULT_NODE_CONFIG_ID.intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getNodeConfigHistory() throws Exception {
        // Initialize the database
        nodeConfigHistoryRepository.saveAndFlush(nodeConfigHistory);

        // Get the nodeConfigHistory
        restNodeConfigHistoryMockMvc.perform(get("/api/node-config-histories/{id}", nodeConfigHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nodeConfigHistory.getId().intValue()))
            .andExpect(jsonPath("$.applicationMetaId").value(DEFAULT_APPLICATION_META_ID.intValue()))
            .andExpect(jsonPath("$.nodeId").value(DEFAULT_NODE_ID.intValue()))
            .andExpect(jsonPath("$.nodeConfigId").value(DEFAULT_NODE_CONFIG_ID.intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNodeConfigHistory() throws Exception {
        // Get the nodeConfigHistory
        restNodeConfigHistoryMockMvc.perform(get("/api/node-config-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNodeConfigHistory() throws Exception {
        // Initialize the database
        nodeConfigHistoryRepository.saveAndFlush(nodeConfigHistory);
        int databaseSizeBeforeUpdate = nodeConfigHistoryRepository.findAll().size();

        // Update the nodeConfigHistory
        NodeConfigHistory updatedNodeConfigHistory = nodeConfigHistoryRepository.findOne(nodeConfigHistory.getId());
        // Disconnect from session so that the updates on updatedNodeConfigHistory are not directly saved in db
        em.detach(updatedNodeConfigHistory);
        updatedNodeConfigHistory
            .applicationMetaId(UPDATED_APPLICATION_META_ID)
            .nodeId(UPDATED_NODE_ID)
            .nodeConfigId(UPDATED_NODE_CONFIG_ID)
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .operation(UPDATED_OPERATION)
            .createTime(UPDATED_CREATE_TIME);
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryMapper.toDto(updatedNodeConfigHistory);

        restNodeConfigHistoryMockMvc.perform(put("/api/node-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the NodeConfigHistory in the database
        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeUpdate);
        NodeConfigHistory testNodeConfigHistory = nodeConfigHistoryList.get(nodeConfigHistoryList.size() - 1);
        assertThat(testNodeConfigHistory.getApplicationMetaId()).isEqualTo(UPDATED_APPLICATION_META_ID);
        assertThat(testNodeConfigHistory.getNodeId()).isEqualTo(UPDATED_NODE_ID);
        assertThat(testNodeConfigHistory.getNodeConfigId()).isEqualTo(UPDATED_NODE_CONFIG_ID);
        assertThat(testNodeConfigHistory.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testNodeConfigHistory.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testNodeConfigHistory.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testNodeConfigHistory.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingNodeConfigHistory() throws Exception {
        int databaseSizeBeforeUpdate = nodeConfigHistoryRepository.findAll().size();

        // Create the NodeConfigHistory
        NodeConfigHistoryDTO nodeConfigHistoryDTO = nodeConfigHistoryMapper.toDto(nodeConfigHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restNodeConfigHistoryMockMvc.perform(put("/api/node-config-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the NodeConfigHistory in the database
        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteNodeConfigHistory() throws Exception {
        // Initialize the database
        nodeConfigHistoryRepository.saveAndFlush(nodeConfigHistory);
        int databaseSizeBeforeDelete = nodeConfigHistoryRepository.findAll().size();

        // Get the nodeConfigHistory
        restNodeConfigHistoryMockMvc.perform(delete("/api/node-config-histories/{id}", nodeConfigHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NodeConfigHistory> nodeConfigHistoryList = nodeConfigHistoryRepository.findAll();
        assertThat(nodeConfigHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NodeConfigHistory.class);
        NodeConfigHistory nodeConfigHistory1 = new NodeConfigHistory();
        nodeConfigHistory1.setId(1L);
        NodeConfigHistory nodeConfigHistory2 = new NodeConfigHistory();
        nodeConfigHistory2.setId(nodeConfigHistory1.getId());
        assertThat(nodeConfigHistory1).isEqualTo(nodeConfigHistory2);
        nodeConfigHistory2.setId(2L);
        assertThat(nodeConfigHistory1).isNotEqualTo(nodeConfigHistory2);
        nodeConfigHistory1.setId(null);
        assertThat(nodeConfigHistory1).isNotEqualTo(nodeConfigHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NodeConfigHistoryDTO.class);
        NodeConfigHistoryDTO nodeConfigHistoryDTO1 = new NodeConfigHistoryDTO();
        nodeConfigHistoryDTO1.setId(1L);
        NodeConfigHistoryDTO nodeConfigHistoryDTO2 = new NodeConfigHistoryDTO();
        assertThat(nodeConfigHistoryDTO1).isNotEqualTo(nodeConfigHistoryDTO2);
        nodeConfigHistoryDTO2.setId(nodeConfigHistoryDTO1.getId());
        assertThat(nodeConfigHistoryDTO1).isEqualTo(nodeConfigHistoryDTO2);
        nodeConfigHistoryDTO2.setId(2L);
        assertThat(nodeConfigHistoryDTO1).isNotEqualTo(nodeConfigHistoryDTO2);
        nodeConfigHistoryDTO1.setId(null);
        assertThat(nodeConfigHistoryDTO1).isNotEqualTo(nodeConfigHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(nodeConfigHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(nodeConfigHistoryMapper.fromId(null)).isNull();
    }
}

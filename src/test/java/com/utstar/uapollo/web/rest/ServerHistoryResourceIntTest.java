package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.ServerHistory;
import com.utstar.uapollo.repository.ServerHistoryRepository;
import com.utstar.uapollo.service.ServerHistoryService;
import com.utstar.uapollo.service.dto.ServerHistoryDTO;
import com.utstar.uapollo.service.mapper.ServerHistoryMapper;
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
 * Test class for the ServerHistoryResource REST controller.
 *
 * @see ServerHistoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class ServerHistoryResourceIntTest {

    private static final Long DEFAULT_NODE_ID = 1L;
    private static final Long UPDATED_NODE_ID = 2L;

    private static final Long DEFAULT_SERVER_ID = 1L;
    private static final Long UPDATED_SERVER_ID = 2L;

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final Operation DEFAULT_OPERATION = Operation.CREATE;
    private static final Operation UPDATED_OPERATION = Operation.UPDATE;

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    @Autowired
    private ServerHistoryRepository serverHistoryRepository;

    @Autowired
    private ServerHistoryMapper serverHistoryMapper;

    @Autowired
    private ServerHistoryService serverHistoryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restServerHistoryMockMvc;

    private ServerHistory serverHistory;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServerHistoryResource serverHistoryResource = new ServerHistoryResource(serverHistoryService);
        this.restServerHistoryMockMvc = MockMvcBuilders.standaloneSetup(serverHistoryResource)
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
    public static ServerHistory createEntity(EntityManager em) {
        ServerHistory serverHistory = new ServerHistory()
            .nodeId(DEFAULT_NODE_ID)
            .serverId(DEFAULT_SERVER_ID)
            .ip(DEFAULT_IP)
            .operation(DEFAULT_OPERATION)
            .createTime(DEFAULT_CREATE_TIME);
        return serverHistory;
    }

    @Before
    public void initTest() {
        serverHistory = createEntity(em);
    }

    @Test
    @Transactional
    public void createServerHistory() throws Exception {
        int databaseSizeBeforeCreate = serverHistoryRepository.findAll().size();

        // Create the ServerHistory
        ServerHistoryDTO serverHistoryDTO = serverHistoryMapper.toDto(serverHistory);
        restServerHistoryMockMvc.perform(post("/api/server-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ServerHistory in the database
        List<ServerHistory> serverHistoryList = serverHistoryRepository.findAll();
        assertThat(serverHistoryList).hasSize(databaseSizeBeforeCreate + 1);
        ServerHistory testServerHistory = serverHistoryList.get(serverHistoryList.size() - 1);
        assertThat(testServerHistory.getNodeId()).isEqualTo(DEFAULT_NODE_ID);
        assertThat(testServerHistory.getServerId()).isEqualTo(DEFAULT_SERVER_ID);
        assertThat(testServerHistory.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testServerHistory.getOperation()).isEqualTo(DEFAULT_OPERATION);
        assertThat(testServerHistory.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
    }

    @Test
    @Transactional
    public void createServerHistoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serverHistoryRepository.findAll().size();

        // Create the ServerHistory with an existing ID
        serverHistory.setId(1L);
        ServerHistoryDTO serverHistoryDTO = serverHistoryMapper.toDto(serverHistory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServerHistoryMockMvc.perform(post("/api/server-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServerHistory in the database
        List<ServerHistory> serverHistoryList = serverHistoryRepository.findAll();
        assertThat(serverHistoryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNodeIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverHistoryRepository.findAll().size();
        // set the field null
        serverHistory.setNodeId(null);

        // Create the ServerHistory, which fails.
        ServerHistoryDTO serverHistoryDTO = serverHistoryMapper.toDto(serverHistory);

        restServerHistoryMockMvc.perform(post("/api/server-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ServerHistory> serverHistoryList = serverHistoryRepository.findAll();
        assertThat(serverHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkServerIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverHistoryRepository.findAll().size();
        // set the field null
        serverHistory.setServerId(null);

        // Create the ServerHistory, which fails.
        ServerHistoryDTO serverHistoryDTO = serverHistoryMapper.toDto(serverHistory);

        restServerHistoryMockMvc.perform(post("/api/server-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ServerHistory> serverHistoryList = serverHistoryRepository.findAll();
        assertThat(serverHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIpIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverHistoryRepository.findAll().size();
        // set the field null
        serverHistory.setIp(null);

        // Create the ServerHistory, which fails.
        ServerHistoryDTO serverHistoryDTO = serverHistoryMapper.toDto(serverHistory);

        restServerHistoryMockMvc.perform(post("/api/server-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ServerHistory> serverHistoryList = serverHistoryRepository.findAll();
        assertThat(serverHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOperationIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverHistoryRepository.findAll().size();
        // set the field null
        serverHistory.setOperation(null);

        // Create the ServerHistory, which fails.
        ServerHistoryDTO serverHistoryDTO = serverHistoryMapper.toDto(serverHistory);

        restServerHistoryMockMvc.perform(post("/api/server-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ServerHistory> serverHistoryList = serverHistoryRepository.findAll();
        assertThat(serverHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = serverHistoryRepository.findAll().size();
        // set the field null
        serverHistory.setCreateTime(null);

        // Create the ServerHistory, which fails.
        ServerHistoryDTO serverHistoryDTO = serverHistoryMapper.toDto(serverHistory);

        restServerHistoryMockMvc.perform(post("/api/server-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverHistoryDTO)))
            .andExpect(status().isBadRequest());

        List<ServerHistory> serverHistoryList = serverHistoryRepository.findAll();
        assertThat(serverHistoryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllServerHistories() throws Exception {
        // Initialize the database
        serverHistoryRepository.saveAndFlush(serverHistory);

        // Get all the serverHistoryList
        restServerHistoryMockMvc.perform(get("/api/server-histories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serverHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].nodeId").value(hasItem(DEFAULT_NODE_ID.intValue())))
            .andExpect(jsonPath("$.[*].serverId").value(hasItem(DEFAULT_SERVER_ID.intValue())))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP.toString())))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getServerHistory() throws Exception {
        // Initialize the database
        serverHistoryRepository.saveAndFlush(serverHistory);

        // Get the serverHistory
        restServerHistoryMockMvc.perform(get("/api/server-histories/{id}", serverHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serverHistory.getId().intValue()))
            .andExpect(jsonPath("$.nodeId").value(DEFAULT_NODE_ID.intValue()))
            .andExpect(jsonPath("$.serverId").value(DEFAULT_SERVER_ID.intValue()))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP.toString()))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingServerHistory() throws Exception {
        // Get the serverHistory
        restServerHistoryMockMvc.perform(get("/api/server-histories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServerHistory() throws Exception {
        // Initialize the database
        serverHistoryRepository.saveAndFlush(serverHistory);
        int databaseSizeBeforeUpdate = serverHistoryRepository.findAll().size();

        // Update the serverHistory
        ServerHistory updatedServerHistory = serverHistoryRepository.findOne(serverHistory.getId());
        // Disconnect from session so that the updates on updatedServerHistory are not directly saved in db
        em.detach(updatedServerHistory);
        updatedServerHistory
            .nodeId(UPDATED_NODE_ID)
            .serverId(UPDATED_SERVER_ID)
            .ip(UPDATED_IP)
            .operation(UPDATED_OPERATION)
            .createTime(UPDATED_CREATE_TIME);
        ServerHistoryDTO serverHistoryDTO = serverHistoryMapper.toDto(updatedServerHistory);

        restServerHistoryMockMvc.perform(put("/api/server-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverHistoryDTO)))
            .andExpect(status().isOk());

        // Validate the ServerHistory in the database
        List<ServerHistory> serverHistoryList = serverHistoryRepository.findAll();
        assertThat(serverHistoryList).hasSize(databaseSizeBeforeUpdate);
        ServerHistory testServerHistory = serverHistoryList.get(serverHistoryList.size() - 1);
        assertThat(testServerHistory.getNodeId()).isEqualTo(UPDATED_NODE_ID);
        assertThat(testServerHistory.getServerId()).isEqualTo(UPDATED_SERVER_ID);
        assertThat(testServerHistory.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testServerHistory.getOperation()).isEqualTo(UPDATED_OPERATION);
        assertThat(testServerHistory.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingServerHistory() throws Exception {
        int databaseSizeBeforeUpdate = serverHistoryRepository.findAll().size();

        // Create the ServerHistory
        ServerHistoryDTO serverHistoryDTO = serverHistoryMapper.toDto(serverHistory);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restServerHistoryMockMvc.perform(put("/api/server-histories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serverHistoryDTO)))
            .andExpect(status().isCreated());

        // Validate the ServerHistory in the database
        List<ServerHistory> serverHistoryList = serverHistoryRepository.findAll();
        assertThat(serverHistoryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteServerHistory() throws Exception {
        // Initialize the database
        serverHistoryRepository.saveAndFlush(serverHistory);
        int databaseSizeBeforeDelete = serverHistoryRepository.findAll().size();

        // Get the serverHistory
        restServerHistoryMockMvc.perform(delete("/api/server-histories/{id}", serverHistory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ServerHistory> serverHistoryList = serverHistoryRepository.findAll();
        assertThat(serverHistoryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServerHistory.class);
        ServerHistory serverHistory1 = new ServerHistory();
        serverHistory1.setId(1L);
        ServerHistory serverHistory2 = new ServerHistory();
        serverHistory2.setId(serverHistory1.getId());
        assertThat(serverHistory1).isEqualTo(serverHistory2);
        serverHistory2.setId(2L);
        assertThat(serverHistory1).isNotEqualTo(serverHistory2);
        serverHistory1.setId(null);
        assertThat(serverHistory1).isNotEqualTo(serverHistory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServerHistoryDTO.class);
        ServerHistoryDTO serverHistoryDTO1 = new ServerHistoryDTO();
        serverHistoryDTO1.setId(1L);
        ServerHistoryDTO serverHistoryDTO2 = new ServerHistoryDTO();
        assertThat(serverHistoryDTO1).isNotEqualTo(serverHistoryDTO2);
        serverHistoryDTO2.setId(serverHistoryDTO1.getId());
        assertThat(serverHistoryDTO1).isEqualTo(serverHistoryDTO2);
        serverHistoryDTO2.setId(2L);
        assertThat(serverHistoryDTO1).isNotEqualTo(serverHistoryDTO2);
        serverHistoryDTO1.setId(null);
        assertThat(serverHistoryDTO1).isNotEqualTo(serverHistoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serverHistoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(serverHistoryMapper.fromId(null)).isNull();
    }
}

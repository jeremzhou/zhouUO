package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.ClientHeartbeatInfo;
import com.utstar.uapollo.repository.ClientHeartbeatInfoRepository;
import com.utstar.uapollo.service.ClientHeartbeatInfoService;
import com.utstar.uapollo.service.dto.ClientHeartbeatInfoDTO;
import com.utstar.uapollo.service.mapper.ClientHeartbeatInfoMapper;
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

/**
 * Test class for the ClientHeartbeatInfoResource REST controller.
 *
 * @see ClientHeartbeatInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class ClientHeartbeatInfoResourceIntTest {

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final String DEFAULT_APPLICATION_META_NAME = "AAAAAAAAAA";
    private static final String UPDATED_APPLICATION_META_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_UPDATE_TIME = 1L;
    private static final Long UPDATED_UPDATE_TIME = 2L;

    private static final Long DEFAULT_UPDATE_VERSION = 1L;
    private static final Long UPDATED_UPDATE_VERSION = 2L;

    private static final Long DEFAULT_HEARTBEAT_TIME = 1L;
    private static final Long UPDATED_HEARTBEAT_TIME = 2L;

    private static final Long DEFAULT_HEARTBEAT_VERSION = 1L;
    private static final Long UPDATED_HEARTBEAT_VERSION = 2L;

    @Autowired
    private ClientHeartbeatInfoRepository clientHeartbeatInfoRepository;

    @Autowired
    private ClientHeartbeatInfoMapper clientHeartbeatInfoMapper;

    @Autowired
    private ClientHeartbeatInfoService clientHeartbeatInfoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClientHeartbeatInfoMockMvc;

    private ClientHeartbeatInfo clientHeartbeatInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClientHeartbeatInfoResource clientHeartbeatInfoResource = new ClientHeartbeatInfoResource(clientHeartbeatInfoService);
        this.restClientHeartbeatInfoMockMvc = MockMvcBuilders.standaloneSetup(clientHeartbeatInfoResource)
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
    public static ClientHeartbeatInfo createEntity(EntityManager em) {
        ClientHeartbeatInfo clientHeartbeatInfo = new ClientHeartbeatInfo()
            .ip(DEFAULT_IP)
            .applicationMetaName(DEFAULT_APPLICATION_META_NAME)
            .updateTime(DEFAULT_UPDATE_TIME)
            .updateVersion(DEFAULT_UPDATE_VERSION)
            .heartbeatTime(DEFAULT_HEARTBEAT_TIME)
            .heartbeatVersion(DEFAULT_HEARTBEAT_VERSION);
        return clientHeartbeatInfo;
    }

    @Before
    public void initTest() {
        clientHeartbeatInfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createClientHeartbeatInfo() throws Exception {
        int databaseSizeBeforeCreate = clientHeartbeatInfoRepository.findAll().size();

        // Create the ClientHeartbeatInfo
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = clientHeartbeatInfoMapper.toDto(clientHeartbeatInfo);
        restClientHeartbeatInfoMockMvc.perform(post("/api/client-heartbeat-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientHeartbeatInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the ClientHeartbeatInfo in the database
        List<ClientHeartbeatInfo> clientHeartbeatInfoList = clientHeartbeatInfoRepository.findAll();
        assertThat(clientHeartbeatInfoList).hasSize(databaseSizeBeforeCreate + 1);
        ClientHeartbeatInfo testClientHeartbeatInfo = clientHeartbeatInfoList.get(clientHeartbeatInfoList.size() - 1);
        assertThat(testClientHeartbeatInfo.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testClientHeartbeatInfo.getApplicationMetaName()).isEqualTo(DEFAULT_APPLICATION_META_NAME);
        assertThat(testClientHeartbeatInfo.getUpdateTime()).isEqualTo(DEFAULT_UPDATE_TIME);
        assertThat(testClientHeartbeatInfo.getUpdateVersion()).isEqualTo(DEFAULT_UPDATE_VERSION);
        assertThat(testClientHeartbeatInfo.getHeartbeatTime()).isEqualTo(DEFAULT_HEARTBEAT_TIME);
        assertThat(testClientHeartbeatInfo.getHeartbeatVersion()).isEqualTo(DEFAULT_HEARTBEAT_VERSION);
    }

    @Test
    @Transactional
    public void createClientHeartbeatInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clientHeartbeatInfoRepository.findAll().size();

        // Create the ClientHeartbeatInfo with an existing ID
        clientHeartbeatInfo.setId(1L);
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = clientHeartbeatInfoMapper.toDto(clientHeartbeatInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientHeartbeatInfoMockMvc.perform(post("/api/client-heartbeat-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientHeartbeatInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClientHeartbeatInfo in the database
        List<ClientHeartbeatInfo> clientHeartbeatInfoList = clientHeartbeatInfoRepository.findAll();
        assertThat(clientHeartbeatInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkIpIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientHeartbeatInfoRepository.findAll().size();
        // set the field null
        clientHeartbeatInfo.setIp(null);

        // Create the ClientHeartbeatInfo, which fails.
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = clientHeartbeatInfoMapper.toDto(clientHeartbeatInfo);

        restClientHeartbeatInfoMockMvc.perform(post("/api/client-heartbeat-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientHeartbeatInfoDTO)))
            .andExpect(status().isBadRequest());

        List<ClientHeartbeatInfo> clientHeartbeatInfoList = clientHeartbeatInfoRepository.findAll();
        assertThat(clientHeartbeatInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkApplicationMetaNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientHeartbeatInfoRepository.findAll().size();
        // set the field null
        clientHeartbeatInfo.setApplicationMetaName(null);

        // Create the ClientHeartbeatInfo, which fails.
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = clientHeartbeatInfoMapper.toDto(clientHeartbeatInfo);

        restClientHeartbeatInfoMockMvc.perform(post("/api/client-heartbeat-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientHeartbeatInfoDTO)))
            .andExpect(status().isBadRequest());

        List<ClientHeartbeatInfo> clientHeartbeatInfoList = clientHeartbeatInfoRepository.findAll();
        assertThat(clientHeartbeatInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientHeartbeatInfoRepository.findAll().size();
        // set the field null
        clientHeartbeatInfo.setUpdateTime(null);

        // Create the ClientHeartbeatInfo, which fails.
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = clientHeartbeatInfoMapper.toDto(clientHeartbeatInfo);

        restClientHeartbeatInfoMockMvc.perform(post("/api/client-heartbeat-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientHeartbeatInfoDTO)))
            .andExpect(status().isBadRequest());

        List<ClientHeartbeatInfo> clientHeartbeatInfoList = clientHeartbeatInfoRepository.findAll();
        assertThat(clientHeartbeatInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdateVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientHeartbeatInfoRepository.findAll().size();
        // set the field null
        clientHeartbeatInfo.setUpdateVersion(null);

        // Create the ClientHeartbeatInfo, which fails.
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = clientHeartbeatInfoMapper.toDto(clientHeartbeatInfo);

        restClientHeartbeatInfoMockMvc.perform(post("/api/client-heartbeat-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientHeartbeatInfoDTO)))
            .andExpect(status().isBadRequest());

        List<ClientHeartbeatInfo> clientHeartbeatInfoList = clientHeartbeatInfoRepository.findAll();
        assertThat(clientHeartbeatInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHeartbeatTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientHeartbeatInfoRepository.findAll().size();
        // set the field null
        clientHeartbeatInfo.setHeartbeatTime(null);

        // Create the ClientHeartbeatInfo, which fails.
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = clientHeartbeatInfoMapper.toDto(clientHeartbeatInfo);

        restClientHeartbeatInfoMockMvc.perform(post("/api/client-heartbeat-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientHeartbeatInfoDTO)))
            .andExpect(status().isBadRequest());

        List<ClientHeartbeatInfo> clientHeartbeatInfoList = clientHeartbeatInfoRepository.findAll();
        assertThat(clientHeartbeatInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHeartbeatVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientHeartbeatInfoRepository.findAll().size();
        // set the field null
        clientHeartbeatInfo.setHeartbeatVersion(null);

        // Create the ClientHeartbeatInfo, which fails.
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = clientHeartbeatInfoMapper.toDto(clientHeartbeatInfo);

        restClientHeartbeatInfoMockMvc.perform(post("/api/client-heartbeat-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientHeartbeatInfoDTO)))
            .andExpect(status().isBadRequest());

        List<ClientHeartbeatInfo> clientHeartbeatInfoList = clientHeartbeatInfoRepository.findAll();
        assertThat(clientHeartbeatInfoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClientHeartbeatInfos() throws Exception {
        // Initialize the database
        clientHeartbeatInfoRepository.saveAndFlush(clientHeartbeatInfo);

        // Get all the clientHeartbeatInfoList
        restClientHeartbeatInfoMockMvc.perform(get("/api/client-heartbeat-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clientHeartbeatInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP.toString())))
            .andExpect(jsonPath("$.[*].applicationMetaName").value(hasItem(DEFAULT_APPLICATION_META_NAME.toString())))
            .andExpect(jsonPath("$.[*].updateTime").value(hasItem(DEFAULT_UPDATE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].updateVersion").value(hasItem(DEFAULT_UPDATE_VERSION.intValue())))
            .andExpect(jsonPath("$.[*].heartbeatTime").value(hasItem(DEFAULT_HEARTBEAT_TIME.intValue())))
            .andExpect(jsonPath("$.[*].heartbeatVersion").value(hasItem(DEFAULT_HEARTBEAT_VERSION.intValue())));
    }

    @Test
    @Transactional
    public void getClientHeartbeatInfo() throws Exception {
        // Initialize the database
        clientHeartbeatInfoRepository.saveAndFlush(clientHeartbeatInfo);

        // Get the clientHeartbeatInfo
        restClientHeartbeatInfoMockMvc.perform(get("/api/client-heartbeat-infos/{id}", clientHeartbeatInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(clientHeartbeatInfo.getId().intValue()))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP.toString()))
            .andExpect(jsonPath("$.applicationMetaName").value(DEFAULT_APPLICATION_META_NAME.toString()))
            .andExpect(jsonPath("$.updateTime").value(DEFAULT_UPDATE_TIME.intValue()))
            .andExpect(jsonPath("$.updateVersion").value(DEFAULT_UPDATE_VERSION.intValue()))
            .andExpect(jsonPath("$.heartbeatTime").value(DEFAULT_HEARTBEAT_TIME.intValue()))
            .andExpect(jsonPath("$.heartbeatVersion").value(DEFAULT_HEARTBEAT_VERSION.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingClientHeartbeatInfo() throws Exception {
        // Get the clientHeartbeatInfo
        restClientHeartbeatInfoMockMvc.perform(get("/api/client-heartbeat-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClientHeartbeatInfo() throws Exception {
        // Initialize the database
        clientHeartbeatInfoRepository.saveAndFlush(clientHeartbeatInfo);
        int databaseSizeBeforeUpdate = clientHeartbeatInfoRepository.findAll().size();

        // Update the clientHeartbeatInfo
        ClientHeartbeatInfo updatedClientHeartbeatInfo = clientHeartbeatInfoRepository.findOne(clientHeartbeatInfo.getId());
        // Disconnect from session so that the updates on updatedClientHeartbeatInfo are not directly saved in db
        em.detach(updatedClientHeartbeatInfo);
        updatedClientHeartbeatInfo
            .ip(UPDATED_IP)
            .applicationMetaName(UPDATED_APPLICATION_META_NAME)
            .updateTime(UPDATED_UPDATE_TIME)
            .updateVersion(UPDATED_UPDATE_VERSION)
            .heartbeatTime(UPDATED_HEARTBEAT_TIME)
            .heartbeatVersion(UPDATED_HEARTBEAT_VERSION);
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = clientHeartbeatInfoMapper.toDto(updatedClientHeartbeatInfo);

        restClientHeartbeatInfoMockMvc.perform(put("/api/client-heartbeat-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientHeartbeatInfoDTO)))
            .andExpect(status().isOk());

        // Validate the ClientHeartbeatInfo in the database
        List<ClientHeartbeatInfo> clientHeartbeatInfoList = clientHeartbeatInfoRepository.findAll();
        assertThat(clientHeartbeatInfoList).hasSize(databaseSizeBeforeUpdate);
        ClientHeartbeatInfo testClientHeartbeatInfo = clientHeartbeatInfoList.get(clientHeartbeatInfoList.size() - 1);
        assertThat(testClientHeartbeatInfo.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testClientHeartbeatInfo.getApplicationMetaName()).isEqualTo(UPDATED_APPLICATION_META_NAME);
        assertThat(testClientHeartbeatInfo.getUpdateTime()).isEqualTo(UPDATED_UPDATE_TIME);
        assertThat(testClientHeartbeatInfo.getUpdateVersion()).isEqualTo(UPDATED_UPDATE_VERSION);
        assertThat(testClientHeartbeatInfo.getHeartbeatTime()).isEqualTo(UPDATED_HEARTBEAT_TIME);
        assertThat(testClientHeartbeatInfo.getHeartbeatVersion()).isEqualTo(UPDATED_HEARTBEAT_VERSION);
    }

    @Test
    @Transactional
    public void updateNonExistingClientHeartbeatInfo() throws Exception {
        int databaseSizeBeforeUpdate = clientHeartbeatInfoRepository.findAll().size();

        // Create the ClientHeartbeatInfo
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = clientHeartbeatInfoMapper.toDto(clientHeartbeatInfo);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClientHeartbeatInfoMockMvc.perform(put("/api/client-heartbeat-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientHeartbeatInfoDTO)))
            .andExpect(status().isCreated());

        // Validate the ClientHeartbeatInfo in the database
        List<ClientHeartbeatInfo> clientHeartbeatInfoList = clientHeartbeatInfoRepository.findAll();
        assertThat(clientHeartbeatInfoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClientHeartbeatInfo() throws Exception {
        // Initialize the database
        clientHeartbeatInfoRepository.saveAndFlush(clientHeartbeatInfo);
        int databaseSizeBeforeDelete = clientHeartbeatInfoRepository.findAll().size();

        // Get the clientHeartbeatInfo
        restClientHeartbeatInfoMockMvc.perform(delete("/api/client-heartbeat-infos/{id}", clientHeartbeatInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ClientHeartbeatInfo> clientHeartbeatInfoList = clientHeartbeatInfoRepository.findAll();
        assertThat(clientHeartbeatInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientHeartbeatInfo.class);
        ClientHeartbeatInfo clientHeartbeatInfo1 = new ClientHeartbeatInfo();
        clientHeartbeatInfo1.setId(1L);
        ClientHeartbeatInfo clientHeartbeatInfo2 = new ClientHeartbeatInfo();
        clientHeartbeatInfo2.setId(clientHeartbeatInfo1.getId());
        assertThat(clientHeartbeatInfo1).isEqualTo(clientHeartbeatInfo2);
        clientHeartbeatInfo2.setId(2L);
        assertThat(clientHeartbeatInfo1).isNotEqualTo(clientHeartbeatInfo2);
        clientHeartbeatInfo1.setId(null);
        assertThat(clientHeartbeatInfo1).isNotEqualTo(clientHeartbeatInfo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientHeartbeatInfoDTO.class);
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO1 = new ClientHeartbeatInfoDTO();
        clientHeartbeatInfoDTO1.setId(1L);
        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO2 = new ClientHeartbeatInfoDTO();
        assertThat(clientHeartbeatInfoDTO1).isNotEqualTo(clientHeartbeatInfoDTO2);
        clientHeartbeatInfoDTO2.setId(clientHeartbeatInfoDTO1.getId());
        assertThat(clientHeartbeatInfoDTO1).isEqualTo(clientHeartbeatInfoDTO2);
        clientHeartbeatInfoDTO2.setId(2L);
        assertThat(clientHeartbeatInfoDTO1).isNotEqualTo(clientHeartbeatInfoDTO2);
        clientHeartbeatInfoDTO1.setId(null);
        assertThat(clientHeartbeatInfoDTO1).isNotEqualTo(clientHeartbeatInfoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(clientHeartbeatInfoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(clientHeartbeatInfoMapper.fromId(null)).isNull();
    }
}

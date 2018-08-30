package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.NodeConfig;
import com.utstar.uapollo.repository.NodeConfigRepository;
import com.utstar.uapollo.service.NodeConfigService;
import com.utstar.uapollo.service.dto.NodeConfigDTO;
import com.utstar.uapollo.service.mapper.NodeConfigMapper;
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
 * Test class for the NodeConfigResource REST controller.
 *
 * @see NodeConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class NodeConfigResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    private static final Long DEFAULT_MODIFY_TIME = 1L;
    private static final Long UPDATED_MODIFY_TIME = 2L;

    @Autowired
    private NodeConfigRepository nodeConfigRepository;

    @Autowired
    private NodeConfigMapper nodeConfigMapper;

    @Autowired
    private NodeConfigService nodeConfigService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNodeConfigMockMvc;

    private NodeConfig nodeConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NodeConfigResource nodeConfigResource = new NodeConfigResource(nodeConfigService);
        this.restNodeConfigMockMvc = MockMvcBuilders.standaloneSetup(nodeConfigResource)
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
    public static NodeConfig createEntity(EntityManager em) {
        NodeConfig nodeConfig = new NodeConfig()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .createTime(DEFAULT_CREATE_TIME)
            .modifyTime(DEFAULT_MODIFY_TIME);
        return nodeConfig;
    }

    @Before
    public void initTest() {
        nodeConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createNodeConfig() throws Exception {
        int databaseSizeBeforeCreate = nodeConfigRepository.findAll().size();
        nodeConfigRepository.save(nodeConfig);
        // Create the NodeConfig
        NodeConfigDTO nodeConfigDTO = nodeConfigMapper.toDto(nodeConfig);
        restNodeConfigMockMvc.perform(post("/api/node-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NodeConfig in the database
        List<NodeConfig> nodeConfigList = nodeConfigRepository.findAll();
        assertThat(nodeConfigList).hasSize(databaseSizeBeforeCreate + 1);
        NodeConfig testNodeConfig = nodeConfigList.get(nodeConfigList.size() - 1);
        assertThat(testNodeConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testNodeConfig.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testNodeConfig.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testNodeConfig.getModifyTime()).isEqualTo(DEFAULT_MODIFY_TIME);
    }

    @Test
    @Transactional
    public void createNodeConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nodeConfigRepository.findAll().size();

        // Create the NodeConfig with an existing ID
        nodeConfig.setId(1L);
        NodeConfigDTO nodeConfigDTO = nodeConfigMapper.toDto(nodeConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNodeConfigMockMvc.perform(post("/api/node-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NodeConfig in the database
        List<NodeConfig> nodeConfigList = nodeConfigRepository.findAll();
        assertThat(nodeConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeConfigRepository.findAll().size();
        // set the field null
        nodeConfig.setKey(null);

        // Create the NodeConfig, which fails.
        NodeConfigDTO nodeConfigDTO = nodeConfigMapper.toDto(nodeConfig);

        restNodeConfigMockMvc.perform(post("/api/node-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigDTO)))
            .andExpect(status().isBadRequest());

        List<NodeConfig> nodeConfigList = nodeConfigRepository.findAll();
        assertThat(nodeConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeConfigRepository.findAll().size();
        // set the field null
        nodeConfig.setValue(null);

        // Create the NodeConfig, which fails.
        NodeConfigDTO nodeConfigDTO = nodeConfigMapper.toDto(nodeConfig);

        restNodeConfigMockMvc.perform(post("/api/node-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigDTO)))
            .andExpect(status().isBadRequest());

        List<NodeConfig> nodeConfigList = nodeConfigRepository.findAll();
        assertThat(nodeConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeConfigRepository.findAll().size();
        // set the field null
        nodeConfig.setCreateTime(null);

        // Create the NodeConfig, which fails.
        NodeConfigDTO nodeConfigDTO = nodeConfigMapper.toDto(nodeConfig);

        restNodeConfigMockMvc.perform(post("/api/node-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigDTO)))
            .andExpect(status().isBadRequest());

        List<NodeConfig> nodeConfigList = nodeConfigRepository.findAll();
        assertThat(nodeConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = nodeConfigRepository.findAll().size();
        // set the field null
        nodeConfig.setModifyTime(null);

        // Create the NodeConfig, which fails.
        NodeConfigDTO nodeConfigDTO = nodeConfigMapper.toDto(nodeConfig);

        restNodeConfigMockMvc.perform(post("/api/node-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nodeConfigDTO)))
            .andExpect(status().isBadRequest());

        List<NodeConfig> nodeConfigList = nodeConfigRepository.findAll();
        assertThat(nodeConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllNodeConfigs() throws Exception {
        // Initialize the database
        nodeConfigRepository.saveAndFlush(nodeConfig);

        // Get all the nodeConfigList
        restNodeConfigMockMvc.perform(get("/api/node-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nodeConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].modifyTime").value(hasItem(DEFAULT_MODIFY_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getNodeConfig() throws Exception {
        // Initialize the database
        nodeConfigRepository.saveAndFlush(nodeConfig);

        // Get the nodeConfig
        restNodeConfigMockMvc.perform(get("/api/node-configs/{id}", nodeConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nodeConfig.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.modifyTime").value(DEFAULT_MODIFY_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingNodeConfig() throws Exception {
        // Get the nodeConfig
        restNodeConfigMockMvc.perform(get("/api/node-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNodeConfig() throws Exception {
        // Initialize the database
        nodeConfigRepository.saveAndFlush(nodeConfig);
        int databaseSizeBeforeUpdate = nodeConfigRepository.findAll().size();

        // Update the nodeConfig
        NodeConfig updatedNodeConfig = nodeConfigRepository.findOne(nodeConfig.getId());
        // Disconnect from session so that the updates on updatedNodeConfig are not directly saved in db
        em.detach(updatedNodeConfig);
        updatedNodeConfig
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .createTime(UPDATED_CREATE_TIME)
            .modifyTime(UPDATED_MODIFY_TIME);
        nodeConfigRepository.saveAndFlush(updatedNodeConfig);

        // Validate the NodeConfig in the database
        List<NodeConfig> nodeConfigList = nodeConfigRepository.findAll();
        assertThat(nodeConfigList).hasSize(databaseSizeBeforeUpdate);
        NodeConfig testNodeConfig = nodeConfigList.get(nodeConfigList.size() - 1);
        assertThat(testNodeConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testNodeConfig.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testNodeConfig.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testNodeConfig.getModifyTime()).isEqualTo(UPDATED_MODIFY_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingNodeConfig() throws Exception {
        int databaseSizeBeforeUpdate = nodeConfigRepository.findAll().size();

        // Create the NodeConfig

        // If the entity doesn't have an ID, it will be created instead of just being updated
        // Validate the NodeConfig in the database
        List<NodeConfig> nodeConfigList = nodeConfigRepository.findAll();
        assertThat(nodeConfigList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNodeConfig() throws Exception {
        // Initialize the database
        nodeConfigRepository.saveAndFlush(nodeConfig);
        int databaseSizeBeforeDelete = nodeConfigRepository.findAll().size();


        // Validate the database is empty
        List<NodeConfig> nodeConfigList = nodeConfigRepository.findAll();
        assertThat(nodeConfigList).hasSize(databaseSizeBeforeDelete);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NodeConfig.class);
        NodeConfig nodeConfig1 = new NodeConfig();
        nodeConfig1.setId(1L);
        NodeConfig nodeConfig2 = new NodeConfig();
        nodeConfig2.setId(nodeConfig1.getId());
        assertThat(nodeConfig1).isEqualTo(nodeConfig2);
        nodeConfig2.setId(2L);
        assertThat(nodeConfig1).isNotEqualTo(nodeConfig2);
        nodeConfig1.setId(null);
        assertThat(nodeConfig1).isNotEqualTo(nodeConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NodeConfigDTO.class);
        NodeConfigDTO nodeConfigDTO1 = new NodeConfigDTO();
        nodeConfigDTO1.setId(1L);
        NodeConfigDTO nodeConfigDTO2 = new NodeConfigDTO();
        assertThat(nodeConfigDTO1).isNotEqualTo(nodeConfigDTO2);
        nodeConfigDTO2.setId(nodeConfigDTO1.getId());
        assertThat(nodeConfigDTO1).isEqualTo(nodeConfigDTO2);
        nodeConfigDTO2.setId(2L);
        assertThat(nodeConfigDTO1).isNotEqualTo(nodeConfigDTO2);
        nodeConfigDTO1.setId(null);
        assertThat(nodeConfigDTO1).isNotEqualTo(nodeConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(nodeConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(nodeConfigMapper.fromId(null)).isNull();
    }
}

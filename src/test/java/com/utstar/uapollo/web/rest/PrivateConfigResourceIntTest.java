package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.PrivateConfig;
import com.utstar.uapollo.repository.PrivateConfigRepository;
import com.utstar.uapollo.service.PrivateConfigService;
import com.utstar.uapollo.service.dto.PrivateConfigDTO;
import com.utstar.uapollo.service.mapper.PrivateConfigMapper;
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
 * Test class for the PrivateConfigResource REST controller.
 *
 * @see PrivateConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class PrivateConfigResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    private static final Long DEFAULT_MODIFY_TIME = 1L;
    private static final Long UPDATED_MODIFY_TIME = 2L;

    @Autowired
    private PrivateConfigRepository privateConfigRepository;

    @Autowired
    private PrivateConfigMapper privateConfigMapper;

    @Autowired
    private PrivateConfigService privateConfigService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPrivateConfigMockMvc;

    private PrivateConfig privateConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PrivateConfigResource privateConfigResource = new PrivateConfigResource(privateConfigService);
        this.restPrivateConfigMockMvc = MockMvcBuilders.standaloneSetup(privateConfigResource)
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
    public static PrivateConfig createEntity(EntityManager em) {
        PrivateConfig privateConfig = new PrivateConfig()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .createTime(DEFAULT_CREATE_TIME)
            .modifyTime(DEFAULT_MODIFY_TIME);
        return privateConfig;
    }

    @Before
    public void initTest() {
        privateConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createPrivateConfig() throws Exception {
        int databaseSizeBeforeCreate = privateConfigRepository.findAll().size();
        privateConfigRepository.saveAndFlush(privateConfig);

        // Validate the PrivateConfig in the database
        List<PrivateConfig> privateConfigList = privateConfigRepository.findAll();
        assertThat(privateConfigList).hasSize(databaseSizeBeforeCreate + 1);
        PrivateConfig testPrivateConfig = privateConfigList.get(privateConfigList.size() - 1);
        assertThat(testPrivateConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testPrivateConfig.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testPrivateConfig.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testPrivateConfig.getModifyTime()).isEqualTo(DEFAULT_MODIFY_TIME);
    }

    @Test
    @Transactional
    public void createPrivateConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = privateConfigRepository.findAll().size();

        // Create the PrivateConfig with an existing ID
        privateConfig.setId(1L);
        PrivateConfigDTO privateConfigDTO = privateConfigMapper.toDto(privateConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPrivateConfigMockMvc.perform(post("/api/private-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PrivateConfig in the database
        List<PrivateConfig> privateConfigList = privateConfigRepository.findAll();
        assertThat(privateConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = privateConfigRepository.findAll().size();
        // set the field null
        privateConfig.setKey(null);

        // Create the PrivateConfig, which fails.
        PrivateConfigDTO privateConfigDTO = privateConfigMapper.toDto(privateConfig);

        restPrivateConfigMockMvc.perform(post("/api/private-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigDTO)))
            .andExpect(status().isBadRequest());

        List<PrivateConfig> privateConfigList = privateConfigRepository.findAll();
        assertThat(privateConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = privateConfigRepository.findAll().size();
        // set the field null
        privateConfig.setValue(null);

        // Create the PrivateConfig, which fails.
        PrivateConfigDTO privateConfigDTO = privateConfigMapper.toDto(privateConfig);

        restPrivateConfigMockMvc.perform(post("/api/private-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigDTO)))
            .andExpect(status().isBadRequest());

        List<PrivateConfig> privateConfigList = privateConfigRepository.findAll();
        assertThat(privateConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = privateConfigRepository.findAll().size();
        // set the field null
        privateConfig.setCreateTime(null);

        // Create the PrivateConfig, which fails.
        PrivateConfigDTO privateConfigDTO = privateConfigMapper.toDto(privateConfig);

        restPrivateConfigMockMvc.perform(post("/api/private-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigDTO)))
            .andExpect(status().isBadRequest());

        List<PrivateConfig> privateConfigList = privateConfigRepository.findAll();
        assertThat(privateConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = privateConfigRepository.findAll().size();
        // set the field null
        privateConfig.setModifyTime(null);

        // Create the PrivateConfig, which fails.
        PrivateConfigDTO privateConfigDTO = privateConfigMapper.toDto(privateConfig);

        restPrivateConfigMockMvc.perform(post("/api/private-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigDTO)))
            .andExpect(status().isBadRequest());

        List<PrivateConfig> privateConfigList = privateConfigRepository.findAll();
        assertThat(privateConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPrivateConfigs() throws Exception {
        // Initialize the database
        privateConfigRepository.saveAndFlush(privateConfig);

        // Get all the privateConfigList
        restPrivateConfigMockMvc.perform(get("/api/private-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(privateConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].modifyTime").value(hasItem(DEFAULT_MODIFY_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getPrivateConfig() throws Exception {
        // Initialize the database
        privateConfigRepository.saveAndFlush(privateConfig);

        // Get the privateConfig
        restPrivateConfigMockMvc.perform(get("/api/private-configs/{id}", privateConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(privateConfig.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.modifyTime").value(DEFAULT_MODIFY_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPrivateConfig() throws Exception {
        // Get the privateConfig
        restPrivateConfigMockMvc.perform(get("/api/private-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrivateConfig() throws Exception {
        // Initialize the database
        privateConfigRepository.saveAndFlush(privateConfig);
        int databaseSizeBeforeUpdate = privateConfigRepository.findAll().size();

        // Update the privateConfig
        PrivateConfig updatedPrivateConfig = privateConfigRepository.findOne(privateConfig.getId());
        // Disconnect from session so that the updates on updatedPrivateConfig are not directly saved in db
        em.detach(updatedPrivateConfig);
        updatedPrivateConfig
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .createTime(UPDATED_CREATE_TIME)
            .modifyTime(UPDATED_MODIFY_TIME);
        privateConfigRepository.saveAndFlush(updatedPrivateConfig);
        PrivateConfigDTO privateConfigDTO = privateConfigMapper.toDto(updatedPrivateConfig);

        restPrivateConfigMockMvc.perform(put("/api/private-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PrivateConfig in the database
        List<PrivateConfig> privateConfigList = privateConfigRepository.findAll();
        assertThat(privateConfigList).hasSize(databaseSizeBeforeUpdate);
        
        PrivateConfig testPrivateConfig = privateConfigList.get(privateConfigList.size() - 1);
        assertThat(testPrivateConfig.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testPrivateConfig.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testPrivateConfig.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testPrivateConfig.getModifyTime()).isEqualTo(UPDATED_MODIFY_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingPrivateConfig() throws Exception {
        int databaseSizeBeforeUpdate = privateConfigRepository.findAll().size();
        privateConfigRepository.saveAndFlush(privateConfig);
        // Create the PrivateConfig
        PrivateConfigDTO privateConfigDTO = privateConfigMapper.toDto(privateConfig);
        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPrivateConfigMockMvc.perform(put("/api/private-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(privateConfigDTO)))
            .andExpect(status().isBadRequest());
        
        List<PrivateConfig> privateConfiglist = privateConfigRepository.findAll();
        assertThat(privateConfiglist).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePrivateConfig() throws Exception {
        // Initialize the database
        int databaseSizeBeforeDelete = privateConfigRepository.findAll().size();

        privateConfigRepository.saveAndFlush(privateConfig);
        // Validate the database is empty
        List<PrivateConfig> privateConfigList = privateConfigRepository.findAll();
        assertThat(privateConfigList).hasSize(databaseSizeBeforeDelete +1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrivateConfig.class);
        PrivateConfig privateConfig1 = new PrivateConfig();
        privateConfig1.setId(1L);
        PrivateConfig privateConfig2 = new PrivateConfig();
        privateConfig2.setId(privateConfig1.getId());
        assertThat(privateConfig1).isEqualTo(privateConfig2);
        privateConfig2.setId(2L);
        assertThat(privateConfig1).isNotEqualTo(privateConfig2);
        privateConfig1.setId(null);
        assertThat(privateConfig1).isNotEqualTo(privateConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrivateConfigDTO.class);
        PrivateConfigDTO privateConfigDTO1 = new PrivateConfigDTO();
        privateConfigDTO1.setId(1L);
        PrivateConfigDTO privateConfigDTO2 = new PrivateConfigDTO();
        assertThat(privateConfigDTO1).isNotEqualTo(privateConfigDTO2);
        privateConfigDTO2.setId(privateConfigDTO1.getId());
        assertThat(privateConfigDTO1).isEqualTo(privateConfigDTO2);
        privateConfigDTO2.setId(2L);
        assertThat(privateConfigDTO1).isNotEqualTo(privateConfigDTO2);
        privateConfigDTO1.setId(null);
        assertThat(privateConfigDTO1).isNotEqualTo(privateConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(privateConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(privateConfigMapper.fromId(null)).isNull();
    }
}

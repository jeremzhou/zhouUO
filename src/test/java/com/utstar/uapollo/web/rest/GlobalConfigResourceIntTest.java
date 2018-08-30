package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.GlobalConfig;
import com.utstar.uapollo.repository.GlobalConfigRepository;
import com.utstar.uapollo.service.GlobalConfigService;
import com.utstar.uapollo.service.dto.GlobalConfigDTO;
import com.utstar.uapollo.service.mapper.GlobalConfigMapper;
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
 * Test class for the GlobalConfigResource REST controller.
 *
 * @see GlobalConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class GlobalConfigResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    private static final Long DEFAULT_MODIFY_TIME = 1L;
    private static final Long UPDATED_MODIFY_TIME = 2L;

    @Autowired
    private GlobalConfigRepository globalConfigRepository;

    @Autowired
    private GlobalConfigMapper globalConfigMapper;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restGlobalConfigMockMvc;

    private GlobalConfig globalConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GlobalConfigResource globalConfigResource = new GlobalConfigResource(globalConfigService);
        this.restGlobalConfigMockMvc = MockMvcBuilders.standaloneSetup(globalConfigResource)
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
    public static GlobalConfig createEntity(EntityManager em) {
        GlobalConfig globalConfig = new GlobalConfig()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .createTime(DEFAULT_CREATE_TIME)
            .modifyTime(DEFAULT_MODIFY_TIME);
        return globalConfig;
    }

    @Before
    public void initTest() {
        globalConfig = createEntity(em);
    }


    @Test
    @Transactional
    public void createGlobalConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = globalConfigRepository.findAll().size();

        // Create the GlobalConfig with an existing ID
        globalConfig.setId(1L);
        GlobalConfigDTO globalConfigDTO = globalConfigMapper.toDto(globalConfig);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGlobalConfigMockMvc.perform(post("/api/global-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GlobalConfig in the database
        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalConfigRepository.findAll().size();
        // set the field null
        globalConfig.setKey(null);

        // Create the GlobalConfig, which fails.
        GlobalConfigDTO globalConfigDTO = globalConfigMapper.toDto(globalConfig);

        restGlobalConfigMockMvc.perform(post("/api/global-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigDTO)))
            .andExpect(status().isBadRequest());

        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalConfigRepository.findAll().size();
        // set the field null
        globalConfig.setValue(null);

        // Create the GlobalConfig, which fails.
        GlobalConfigDTO globalConfigDTO = globalConfigMapper.toDto(globalConfig);

        restGlobalConfigMockMvc.perform(post("/api/global-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigDTO)))
            .andExpect(status().isBadRequest());

        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalConfigRepository.findAll().size();
        // set the field null
        globalConfig.setCreateTime(null);

        // Create the GlobalConfig, which fails.
        GlobalConfigDTO globalConfigDTO = globalConfigMapper.toDto(globalConfig);

        restGlobalConfigMockMvc.perform(post("/api/global-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigDTO)))
            .andExpect(status().isBadRequest());

        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = globalConfigRepository.findAll().size();
        // set the field null
        globalConfig.setModifyTime(null);

        // Create the GlobalConfig, which fails.
        GlobalConfigDTO globalConfigDTO = globalConfigMapper.toDto(globalConfig);

        restGlobalConfigMockMvc.perform(post("/api/global-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigDTO)))
            .andExpect(status().isBadRequest());

        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllGlobalConfigs() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get all the globalConfigList
        restGlobalConfigMockMvc.perform(get("/api/global-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(globalConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].modifyTime").value(hasItem(DEFAULT_MODIFY_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getGlobalConfig() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);

        // Get the globalConfig
        restGlobalConfigMockMvc.perform(get("/api/global-configs/{id}", globalConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(globalConfig.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.modifyTime").value(DEFAULT_MODIFY_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingGlobalConfig() throws Exception {
        // Get the globalConfig
        restGlobalConfigMockMvc.perform(get("/api/global-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGlobalConfig() throws Exception {
        // Initialize the database
        globalConfigRepository.saveAndFlush(globalConfig);
        int databaseSizeBeforeUpdate = globalConfigRepository.findAll().size();

        // Update the globalConfig
        GlobalConfig updatedGlobalConfig = globalConfigRepository.findOne(globalConfig.getId());
        // Disconnect from session so that the updates on updatedGlobalConfig are not directly saved in db
        em.detach(updatedGlobalConfig);
        updatedGlobalConfig
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .createTime(UPDATED_CREATE_TIME)
            .modifyTime(UPDATED_MODIFY_TIME);
       


        // Validate the GlobalConfig in the database
        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeUpdate);
        GlobalConfig testGlobalConfig = globalConfigList.get(globalConfigList.size() - 1);
        assertThat(testGlobalConfig.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testGlobalConfig.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testGlobalConfig.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testGlobalConfig.getModifyTime()).isEqualTo(DEFAULT_MODIFY_TIME);
    }

   @Test
    @Transactional
    public void updateNonExistingGlobalConfig() throws Exception {
	   globalConfigRepository.saveAndFlush(globalConfig);
	   int databaseSizeBeforeUpdate = globalConfigRepository.findAll().size();
        
        
        // Create the GlobalConfig
        GlobalConfigDTO globalConfigDTO = globalConfigMapper.toDto(globalConfig);
        
        // If the entity doesn't have an ID, it will be created instead of just being updated
        restGlobalConfigMockMvc.perform(put("/api/global-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(globalConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GlobalConfig in the database
        List<GlobalConfig> globalConfigList = globalConfigRepository.findAll();
        assertThat(globalConfigList).hasSize(databaseSizeBeforeUpdate );
    }


    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalConfig.class);
        GlobalConfig globalConfig1 = new GlobalConfig();
        globalConfig1.setId(1L);
        GlobalConfig globalConfig2 = new GlobalConfig();
        globalConfig2.setId(globalConfig1.getId());
        assertThat(globalConfig1).isEqualTo(globalConfig2);
        globalConfig2.setId(2L);
        assertThat(globalConfig1).isNotEqualTo(globalConfig2);
        globalConfig1.setId(null);
        assertThat(globalConfig1).isNotEqualTo(globalConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GlobalConfigDTO.class);
        GlobalConfigDTO globalConfigDTO1 = new GlobalConfigDTO();
        globalConfigDTO1.setId(1L);
        GlobalConfigDTO globalConfigDTO2 = new GlobalConfigDTO();
        assertThat(globalConfigDTO1).isNotEqualTo(globalConfigDTO2);
        globalConfigDTO2.setId(globalConfigDTO1.getId());
        assertThat(globalConfigDTO1).isEqualTo(globalConfigDTO2);
        globalConfigDTO2.setId(2L);
        assertThat(globalConfigDTO1).isNotEqualTo(globalConfigDTO2);
        globalConfigDTO1.setId(null);
        assertThat(globalConfigDTO1).isNotEqualTo(globalConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(globalConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(globalConfigMapper.fromId(null)).isNull();
    }
}

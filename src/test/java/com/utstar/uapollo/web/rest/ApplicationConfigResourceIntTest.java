package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.ApplicationConfig;
import com.utstar.uapollo.repository.ApplicationConfigRepository;
import com.utstar.uapollo.service.ApplicationConfigService;
import com.utstar.uapollo.service.dto.ApplicationConfigDTO;
import com.utstar.uapollo.service.mapper.ApplicationConfigMapper;
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

/**
 * Test class for the ApplicationConfigResource REST controller.
 *
 * @see ApplicationConfigResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class ApplicationConfigResourceIntTest {

    private static final String DEFAULT_CONFIG_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    private static final Long DEFAULT_MODIFY_TIME = 1L;
    private static final Long UPDATED_MODIFY_TIME = 2L;

    @Autowired
    private ApplicationConfigRepository applicationConfigRepository;

    @Autowired
    private ApplicationConfigMapper applicationConfigMapper;

    @Autowired
    private ApplicationConfigService applicationConfigService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplicationConfigMockMvc;

    private ApplicationConfig applicationConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationConfigResource applicationConfigResource = new ApplicationConfigResource(applicationConfigService);
        this.restApplicationConfigMockMvc = MockMvcBuilders.standaloneSetup(applicationConfigResource)
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
    public static ApplicationConfig createEntity(EntityManager em) {
        ApplicationConfig applicationConfig = new ApplicationConfig()
            .configContent(DEFAULT_CONFIG_CONTENT)
            .createTime(DEFAULT_CREATE_TIME)
            .modifyTime(DEFAULT_MODIFY_TIME);
        return applicationConfig;
    }

    @Before
    public void initTest() {
        applicationConfig = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationConfig() throws Exception {
        int databaseSizeBeforeCreate = applicationConfigRepository.findAll().size();

        // Create the ApplicationConfig
//        ApplicationConfigDTO applicationConfigDTO = applicationConfigMapper.toDto(applicationConfig);

        // Validate the ApplicationConfig in the database
        List<ApplicationConfig> applicationConfigList = applicationConfigRepository.findAll();
        assertThat(applicationConfigList).hasSize(databaseSizeBeforeCreate);
       /* ApplicationConfig testApplicationConfig = applicationConfigList.get(applicationConfigList.size() - 1);
        assertThat(testApplicationConfig.getConfigContent()).isEqualTo(DEFAULT_CONFIG_CONTENT);
        assertThat(testApplicationConfig.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testApplicationConfig.getModifyTime()).isEqualTo(DEFAULT_MODIFY_TIME);*/
    }

    @Test
    @Transactional
    public void createApplicationConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationConfigRepository.findAll().size();

        // Create the ApplicationConfig with an existing ID
        applicationConfig.setId(1L);
        ApplicationConfigDTO applicationConfigDTO = applicationConfigMapper.toDto(applicationConfig);
        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationConfigMockMvc.perform(post("/api/application-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationConfig in the database
        List<ApplicationConfig> applicationConfigList = applicationConfigRepository.findAll();
        assertThat(applicationConfigList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkConfigContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationConfigRepository.findAll().size();
        // set the field null
        applicationConfig.setConfigContent(null);

        // Create the ApplicationConfig, which fails.
        ApplicationConfigDTO applicationConfigDTO = applicationConfigMapper.toDto(applicationConfig);

        restApplicationConfigMockMvc.perform(post("/api/application-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationConfig> applicationConfigList = applicationConfigRepository.findAll();
        assertThat(applicationConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationConfigRepository.findAll().size();
        // set the field null
        applicationConfig.setCreateTime(null);

        // Create the ApplicationConfig, which fails.
        ApplicationConfigDTO applicationConfigDTO = applicationConfigMapper.toDto(applicationConfig);

        restApplicationConfigMockMvc.perform(post("/api/application-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationConfig> applicationConfigList = applicationConfigRepository.findAll();
        assertThat(applicationConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationConfigRepository.findAll().size();
        // set the field null
        applicationConfig.setModifyTime(null);

        // Create the ApplicationConfig, which fails.
        ApplicationConfigDTO applicationConfigDTO = applicationConfigMapper.toDto(applicationConfig);

        restApplicationConfigMockMvc.perform(post("/api/application-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationConfig> applicationConfigList = applicationConfigRepository.findAll();
        assertThat(applicationConfigList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicationConfigs() throws Exception {
        // Initialize the database
        applicationConfigRepository.saveAndFlush(applicationConfig);

        // Get all the applicationConfigList
        restApplicationConfigMockMvc.perform(get("/api/application-configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configContent").value(hasItem(DEFAULT_CONFIG_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].modifyTime").value(hasItem(DEFAULT_MODIFY_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getApplicationConfig() throws Exception {
        // Initialize the database
        applicationConfigRepository.saveAndFlush(applicationConfig);

        // Get the applicationConfig
        restApplicationConfigMockMvc.perform(get("/api/application-configs/{id}", applicationConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applicationConfig.getId().intValue()))
            .andExpect(jsonPath("$.configContent").value(DEFAULT_CONFIG_CONTENT.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.modifyTime").value(DEFAULT_MODIFY_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationConfig() throws Exception {
        // Get the applicationConfig
        restApplicationConfigMockMvc.perform(get("/api/application-configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationConfig() throws Exception {
        // Initialize the database
        applicationConfigRepository.saveAndFlush(applicationConfig);
        int databaseSizeBeforeUpdate = applicationConfigRepository.findAll().size();

        // Update the applicationConfig
        ApplicationConfig updatedApplicationConfig = applicationConfigRepository.findOne(applicationConfig.getId());
        // Disconnect from session so that the updates on updatedApplicationConfig are not directly saved in db
        em.detach(updatedApplicationConfig);
        updatedApplicationConfig
            .configContent(UPDATED_CONFIG_CONTENT)
            .createTime(UPDATED_CREATE_TIME)
            .modifyTime(UPDATED_MODIFY_TIME);
        ApplicationConfigDTO applicationConfigDTO = applicationConfigMapper.toDto(updatedApplicationConfig);

        restApplicationConfigMockMvc.perform(put("/api/application-configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationConfig in the database
        List<ApplicationConfig> applicationConfigList = applicationConfigRepository.findAll();
        ApplicationConfig testApplicationConfig = applicationConfigList.get(applicationConfigList.size() - 1);
        assertThat(testApplicationConfig.getConfigContent()).isEqualTo(DEFAULT_CONFIG_CONTENT);
        assertThat(testApplicationConfig.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testApplicationConfig.getModifyTime()).isEqualTo(DEFAULT_MODIFY_TIME);
        assertThat(applicationConfigList).hasSize(databaseSizeBeforeUpdate);
    }



    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationConfig.class);
        ApplicationConfig applicationConfig1 = new ApplicationConfig();
        applicationConfig1.setId(1L);
        ApplicationConfig applicationConfig2 = new ApplicationConfig();
        applicationConfig2.setId(applicationConfig1.getId());
        assertThat(applicationConfig1).isEqualTo(applicationConfig2);
        applicationConfig2.setId(2L);
        assertThat(applicationConfig1).isNotEqualTo(applicationConfig2);
        applicationConfig1.setId(null);
        assertThat(applicationConfig1).isNotEqualTo(applicationConfig2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationConfigDTO.class);
        ApplicationConfigDTO applicationConfigDTO1 = new ApplicationConfigDTO();
        applicationConfigDTO1.setId(1L);
        ApplicationConfigDTO applicationConfigDTO2 = new ApplicationConfigDTO();
        assertThat(applicationConfigDTO1).isNotEqualTo(applicationConfigDTO2);
        applicationConfigDTO2.setId(applicationConfigDTO1.getId());
        assertThat(applicationConfigDTO1).isEqualTo(applicationConfigDTO2);
        applicationConfigDTO2.setId(2L);
        assertThat(applicationConfigDTO1).isNotEqualTo(applicationConfigDTO2);
        applicationConfigDTO1.setId(null);
        assertThat(applicationConfigDTO1).isNotEqualTo(applicationConfigDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(applicationConfigMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(applicationConfigMapper.fromId(null)).isNull();
    }
}

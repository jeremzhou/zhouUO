package com.utstar.uapollo.web.rest;

import com.utstar.uapollo.UapolloApp;

import com.utstar.uapollo.domain.ApplicationMeta;
import com.utstar.uapollo.repository.ApplicationMetaRepository;
import com.utstar.uapollo.service.ApplicationMetaService;
import com.utstar.uapollo.service.dto.ApplicationMetaDTO;
import com.utstar.uapollo.service.mapper.ApplicationMetaMapper;
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
 * Test class for the ApplicationMetaResource REST controller.
 *
 * @see ApplicationMetaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UapolloApp.class)
public class ApplicationMetaResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_FILE = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_FILE = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_CONTENT = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATE_TIME = 1L;
    private static final Long UPDATED_CREATE_TIME = 2L;

    private static final Long DEFAULT_MODIFY_TIME = 1L;
    private static final Long UPDATED_MODIFY_TIME = 2L;

    @Autowired
    private ApplicationMetaRepository applicationMetaRepository;

    @Autowired
    private ApplicationMetaMapper applicationMetaMapper;

    @Autowired
    private ApplicationMetaService applicationMetaService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restApplicationMetaMockMvc;

    private ApplicationMeta applicationMeta;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ApplicationMetaResource applicationMetaResource = new ApplicationMetaResource(applicationMetaService);
        this.restApplicationMetaMockMvc = MockMvcBuilders.standaloneSetup(applicationMetaResource)
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
    public static ApplicationMeta createEntity(EntityManager em) {
        ApplicationMeta applicationMeta = new ApplicationMeta()
            .name(DEFAULT_NAME)
            .configFile(DEFAULT_CONFIG_FILE)
            .configContent(DEFAULT_CONFIG_CONTENT)
            .createTime(DEFAULT_CREATE_TIME)
            .modifyTime(DEFAULT_MODIFY_TIME);
        return applicationMeta;
    }

    @Before
    public void initTest() {
        applicationMeta = createEntity(em);
    }

    @Test
    @Transactional
    public void createApplicationMeta() throws Exception {
        int databaseSizeBeforeCreate = applicationMetaRepository.findAll().size();

        // Create the ApplicationMeta
        ApplicationMetaDTO applicationMetaDTO = applicationMetaMapper.toDto(applicationMeta);
        applicationMetaRepository.save(applicationMeta);
        // Validate the ApplicationMeta in the database
        List<ApplicationMeta> applicationMetaList = applicationMetaRepository.findAll();
        assertThat(applicationMetaList).hasSize(databaseSizeBeforeCreate + 1);
        ApplicationMeta testApplicationMeta = applicationMetaList.get(applicationMetaList.size() - 1);
        assertThat(testApplicationMeta.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testApplicationMeta.getConfigFile()).isEqualTo(DEFAULT_CONFIG_FILE);
        assertThat(testApplicationMeta.getConfigContent()).isEqualTo(DEFAULT_CONFIG_CONTENT);
        assertThat(testApplicationMeta.getCreateTime()).isEqualTo(DEFAULT_CREATE_TIME);
        assertThat(testApplicationMeta.getModifyTime()).isEqualTo(DEFAULT_MODIFY_TIME);
    }

    @Test
    @Transactional
    public void createApplicationMetaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = applicationMetaRepository.findAll().size();

        // Create the ApplicationMeta with an existing ID
        applicationMeta.setId(1L);
        ApplicationMetaDTO applicationMetaDTO = applicationMetaMapper.toDto(applicationMeta);

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicationMetaMockMvc.perform(post("/api/application-metas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationMeta in the database
        List<ApplicationMeta> applicationMetaList = applicationMetaRepository.findAll();
        assertThat(applicationMetaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaRepository.findAll().size();
        // set the field null
        applicationMeta.setName(null);

        // Create the ApplicationMeta, which fails.
        ApplicationMetaDTO applicationMetaDTO = applicationMetaMapper.toDto(applicationMeta);

        restApplicationMetaMockMvc.perform(post("/api/application-metas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMeta> applicationMetaList = applicationMetaRepository.findAll();
        assertThat(applicationMetaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConfigFileIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaRepository.findAll().size();
        // set the field null
        applicationMeta.setConfigFile(null);

        // Create the ApplicationMeta, which fails.
        ApplicationMetaDTO applicationMetaDTO = applicationMetaMapper.toDto(applicationMeta);

        restApplicationMetaMockMvc.perform(post("/api/application-metas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMeta> applicationMetaList = applicationMetaRepository.findAll();
        assertThat(applicationMetaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkConfigContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaRepository.findAll().size();
        // set the field null
        applicationMeta.setConfigContent(null);

        // Create the ApplicationMeta, which fails.
        ApplicationMetaDTO applicationMetaDTO = applicationMetaMapper.toDto(applicationMeta);

        restApplicationMetaMockMvc.perform(post("/api/application-metas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMeta> applicationMetaList = applicationMetaRepository.findAll();
        assertThat(applicationMetaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaRepository.findAll().size();
        // set the field null
        applicationMeta.setCreateTime(null);

        // Create the ApplicationMeta, which fails.
        ApplicationMetaDTO applicationMetaDTO = applicationMetaMapper.toDto(applicationMeta);

        restApplicationMetaMockMvc.perform(post("/api/application-metas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMeta> applicationMetaList = applicationMetaRepository.findAll();
        assertThat(applicationMetaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModifyTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicationMetaRepository.findAll().size();
        // set the field null
        applicationMeta.setModifyTime(null);

        // Create the ApplicationMeta, which fails.
        ApplicationMetaDTO applicationMetaDTO = applicationMetaMapper.toDto(applicationMeta);

        restApplicationMetaMockMvc.perform(post("/api/application-metas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaDTO)))
            .andExpect(status().isBadRequest());

        List<ApplicationMeta> applicationMetaList = applicationMetaRepository.findAll();
        assertThat(applicationMetaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllApplicationMetas() throws Exception {
        // Initialize the database
        applicationMetaRepository.saveAndFlush(applicationMeta);

        // Get all the applicationMetaList
        restApplicationMetaMockMvc.perform(get("/api/application-metas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicationMeta.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].configFile").value(hasItem(DEFAULT_CONFIG_FILE.toString())))
            .andExpect(jsonPath("$.[*].configContent").value(hasItem(DEFAULT_CONFIG_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].createTime").value(hasItem(DEFAULT_CREATE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].modifyTime").value(hasItem(DEFAULT_MODIFY_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getApplicationMeta() throws Exception {
        // Initialize the database
        applicationMetaRepository.saveAndFlush(applicationMeta);

        // Get the applicationMeta
        restApplicationMetaMockMvc.perform(get("/api/application-metas/{id}", applicationMeta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(applicationMeta.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.configFile").value(DEFAULT_CONFIG_FILE.toString()))
            .andExpect(jsonPath("$.configContent").value(DEFAULT_CONFIG_CONTENT.toString()))
            .andExpect(jsonPath("$.createTime").value(DEFAULT_CREATE_TIME.intValue()))
            .andExpect(jsonPath("$.modifyTime").value(DEFAULT_MODIFY_TIME.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingApplicationMeta() throws Exception {
        // Get the applicationMeta
        restApplicationMetaMockMvc.perform(get("/api/application-metas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateApplicationMeta() throws Exception {
        // Initialize the database
        applicationMetaRepository.saveAndFlush(applicationMeta);
        int databaseSizeBeforeUpdate = applicationMetaRepository.findAll().size();

        // Update the applicationMeta
        ApplicationMeta updatedApplicationMeta = applicationMetaRepository.findOne(applicationMeta.getId());
        // Disconnect from session so that the updates on updatedApplicationMeta are not directly saved in db
        em.detach(updatedApplicationMeta);
        updatedApplicationMeta
            .name(UPDATED_NAME)
            .configFile(UPDATED_CONFIG_FILE)
            .configContent(UPDATED_CONFIG_CONTENT)
            .createTime(UPDATED_CREATE_TIME)
            .modifyTime(UPDATED_MODIFY_TIME);
        applicationMetaRepository.saveAndFlush(updatedApplicationMeta);
        ApplicationMetaDTO applicationMetaDTO = applicationMetaMapper.toDto(updatedApplicationMeta);

        restApplicationMetaMockMvc.perform(put("/api/application-metas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(applicationMetaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApplicationMeta in the database
        List<ApplicationMeta> applicationMetaList = applicationMetaRepository.findAll();
        assertThat(applicationMetaList).hasSize(databaseSizeBeforeUpdate);
        ApplicationMeta testApplicationMeta = applicationMetaList.get(applicationMetaList.size() - 1);
        assertThat(testApplicationMeta.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testApplicationMeta.getConfigFile()).isEqualTo(UPDATED_CONFIG_FILE);
        assertThat(testApplicationMeta.getConfigContent()).isEqualTo(UPDATED_CONFIG_CONTENT);
        assertThat(testApplicationMeta.getCreateTime()).isEqualTo(UPDATED_CREATE_TIME);
        assertThat(testApplicationMeta.getModifyTime()).isEqualTo(UPDATED_MODIFY_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingApplicationMeta() throws Exception {
        int databaseSizeBeforeUpdate = applicationMetaRepository.findAll().size();

        // Create the ApplicationMeta


        // Validate the ApplicationMeta in the database
        List<ApplicationMeta> applicationMetaList = applicationMetaRepository.findAll();
        assertThat(applicationMetaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteApplicationMeta() throws Exception {
        // Initialize the database
        applicationMetaRepository.saveAndFlush(applicationMeta);
        int databaseSizeBeforeDelete = applicationMetaRepository.findAll().size();

        // Get the applicationMeta

        // Validate the database is empty
        List<ApplicationMeta> applicationMetaList = applicationMetaRepository.findAll();
        assertThat(applicationMetaList).hasSize(databaseSizeBeforeDelete);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationMeta.class);
        ApplicationMeta applicationMeta1 = new ApplicationMeta();
        applicationMeta1.setId(1L);
        ApplicationMeta applicationMeta2 = new ApplicationMeta();
        applicationMeta2.setId(applicationMeta1.getId());
        assertThat(applicationMeta1).isEqualTo(applicationMeta2);
        applicationMeta2.setId(2L);
        assertThat(applicationMeta1).isNotEqualTo(applicationMeta2);
        applicationMeta1.setId(null);
        assertThat(applicationMeta1).isNotEqualTo(applicationMeta2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationMetaDTO.class);
        ApplicationMetaDTO applicationMetaDTO1 = new ApplicationMetaDTO();
        applicationMetaDTO1.setId(1L);
        ApplicationMetaDTO applicationMetaDTO2 = new ApplicationMetaDTO();
        assertThat(applicationMetaDTO1).isNotEqualTo(applicationMetaDTO2);
        applicationMetaDTO2.setId(applicationMetaDTO1.getId());
        assertThat(applicationMetaDTO1).isEqualTo(applicationMetaDTO2);
        applicationMetaDTO2.setId(2L);
        assertThat(applicationMetaDTO1).isNotEqualTo(applicationMetaDTO2);
        applicationMetaDTO1.setId(null);
        assertThat(applicationMetaDTO1).isNotEqualTo(applicationMetaDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(applicationMetaMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(applicationMetaMapper.fromId(null)).isNull();
    }
}

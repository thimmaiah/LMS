package com.learnnow.web.rest;

import com.learnnow.TestApp;
import com.learnnow.domain.Profile;
import com.learnnow.domain.User;
import com.learnnow.repository.ProfileRepository;
import com.learnnow.repository.search.ProfileSearchRepository;
import com.learnnow.service.ProfileService;
import com.learnnow.service.dto.ProfileDTO;
import com.learnnow.service.mapper.ProfileMapper;
import com.learnnow.web.rest.errors.ExceptionTranslator;
import com.learnnow.service.dto.ProfileCriteria;
import com.learnnow.service.ProfileQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.learnnow.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.learnnow.domain.enumeration.SmeLevel;
/**
 * Integration tests for the {@link ProfileResource} REST controller.
 */
@SpringBootTest(classes = TestApp.class)
public class ProfileResourceIT {

    private static final Integer DEFAULT_POINTS = 0;
    private static final Integer UPDATED_POINTS = 1;
    private static final Integer SMALLER_POINTS = 0 - 1;

    private static final SmeLevel DEFAULT_SME_LEVEL = SmeLevel.Expert;
    private static final SmeLevel UPDATED_SME_LEVEL = SmeLevel.Intermediate;

    private static final String DEFAULT_SKILLS = "AAAAAAAAAA";
    private static final String UPDATED_SKILLS = "BBBBBBBBBB";

    private static final String DEFAULT_EXPERT_IN = "AAAAAAAAAA";
    private static final String UPDATED_EXPERT_IN = "BBBBBBBBBB";

    private static final String DEFAULT_SHADOWING_IN = "AAAAAAAAAA";
    private static final String UPDATED_SHADOWING_IN = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private ProfileService profileService;

    /**
     * This repository is mocked in the com.learnnow.repository.search test package.
     *
     * @see com.learnnow.repository.search.ProfileSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProfileSearchRepository mockProfileSearchRepository;

    @Autowired
    private ProfileQueryService profileQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restProfileMockMvc;

    private Profile profile;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfileResource profileResource = new ProfileResource(profileService, profileQueryService);
        this.restProfileMockMvc = MockMvcBuilders.standaloneSetup(profileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .points(DEFAULT_POINTS)
            .smeLevel(DEFAULT_SME_LEVEL)
            .skills(DEFAULT_SKILLS)
            .expertIn(DEFAULT_EXPERT_IN)
            .shadowingIn(DEFAULT_SHADOWING_IN)
            .city(DEFAULT_CITY)
            .location(DEFAULT_LOCATION);
        return profile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createUpdatedEntity(EntityManager em) {
        Profile profile = new Profile()
            .points(UPDATED_POINTS)
            .smeLevel(UPDATED_SME_LEVEL)
            .skills(UPDATED_SKILLS)
            .expertIn(UPDATED_EXPERT_IN)
            .shadowingIn(UPDATED_SHADOWING_IN)
            .city(UPDATED_CITY)
            .location(UPDATED_LOCATION);
        return profile;
    }

    @BeforeEach
    public void initTest() {
        profile = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testProfile.getSmeLevel()).isEqualTo(DEFAULT_SME_LEVEL);
        assertThat(testProfile.getSkills()).isEqualTo(DEFAULT_SKILLS);
        assertThat(testProfile.getExpertIn()).isEqualTo(DEFAULT_EXPERT_IN);
        assertThat(testProfile.getShadowingIn()).isEqualTo(DEFAULT_SHADOWING_IN);
        assertThat(testProfile.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testProfile.getLocation()).isEqualTo(DEFAULT_LOCATION);

        // Validate the Profile in Elasticsearch
        verify(mockProfileSearchRepository, times(1)).save(testProfile);
    }

    @Test
    @Transactional
    public void createProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile with an existing ID
        profile.setId(1L);
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate);

        // Validate the Profile in Elasticsearch
        verify(mockProfileSearchRepository, times(0)).save(profile);
    }


    @Test
    @Transactional
    public void checkSkillsIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setSkills(null);

        // Create the Profile, which fails.
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].smeLevel").value(hasItem(DEFAULT_SME_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].skills").value(hasItem(DEFAULT_SKILLS)))
            .andExpect(jsonPath("$.[*].expertIn").value(hasItem(DEFAULT_EXPERT_IN)))
            .andExpect(jsonPath("$.[*].shadowingIn").value(hasItem(DEFAULT_SHADOWING_IN)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }
    
    @Test
    @Transactional
    public void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.smeLevel").value(DEFAULT_SME_LEVEL.toString()))
            .andExpect(jsonPath("$.skills").value(DEFAULT_SKILLS))
            .andExpect(jsonPath("$.expertIn").value(DEFAULT_EXPERT_IN))
            .andExpect(jsonPath("$.shadowingIn").value(DEFAULT_SHADOWING_IN))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }


    @Test
    @Transactional
    public void getProfilesByIdFiltering() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        Long id = profile.getId();

        defaultProfileShouldBeFound("id.equals=" + id);
        defaultProfileShouldNotBeFound("id.notEquals=" + id);

        defaultProfileShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProfileShouldNotBeFound("id.greaterThan=" + id);

        defaultProfileShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProfileShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProfilesByPointsIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where points equals to DEFAULT_POINTS
        defaultProfileShouldBeFound("points.equals=" + DEFAULT_POINTS);

        // Get all the profileList where points equals to UPDATED_POINTS
        defaultProfileShouldNotBeFound("points.equals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllProfilesByPointsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where points not equals to DEFAULT_POINTS
        defaultProfileShouldNotBeFound("points.notEquals=" + DEFAULT_POINTS);

        // Get all the profileList where points not equals to UPDATED_POINTS
        defaultProfileShouldBeFound("points.notEquals=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllProfilesByPointsIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where points in DEFAULT_POINTS or UPDATED_POINTS
        defaultProfileShouldBeFound("points.in=" + DEFAULT_POINTS + "," + UPDATED_POINTS);

        // Get all the profileList where points equals to UPDATED_POINTS
        defaultProfileShouldNotBeFound("points.in=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllProfilesByPointsIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where points is not null
        defaultProfileShouldBeFound("points.specified=true");

        // Get all the profileList where points is null
        defaultProfileShouldNotBeFound("points.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByPointsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where points is greater than or equal to DEFAULT_POINTS
        defaultProfileShouldBeFound("points.greaterThanOrEqual=" + DEFAULT_POINTS);

        // Get all the profileList where points is greater than or equal to UPDATED_POINTS
        defaultProfileShouldNotBeFound("points.greaterThanOrEqual=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllProfilesByPointsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where points is less than or equal to DEFAULT_POINTS
        defaultProfileShouldBeFound("points.lessThanOrEqual=" + DEFAULT_POINTS);

        // Get all the profileList where points is less than or equal to SMALLER_POINTS
        defaultProfileShouldNotBeFound("points.lessThanOrEqual=" + SMALLER_POINTS);
    }

    @Test
    @Transactional
    public void getAllProfilesByPointsIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where points is less than DEFAULT_POINTS
        defaultProfileShouldNotBeFound("points.lessThan=" + DEFAULT_POINTS);

        // Get all the profileList where points is less than UPDATED_POINTS
        defaultProfileShouldBeFound("points.lessThan=" + UPDATED_POINTS);
    }

    @Test
    @Transactional
    public void getAllProfilesByPointsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where points is greater than DEFAULT_POINTS
        defaultProfileShouldNotBeFound("points.greaterThan=" + DEFAULT_POINTS);

        // Get all the profileList where points is greater than SMALLER_POINTS
        defaultProfileShouldBeFound("points.greaterThan=" + SMALLER_POINTS);
    }


    @Test
    @Transactional
    public void getAllProfilesBySmeLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where smeLevel equals to DEFAULT_SME_LEVEL
        defaultProfileShouldBeFound("smeLevel.equals=" + DEFAULT_SME_LEVEL);

        // Get all the profileList where smeLevel equals to UPDATED_SME_LEVEL
        defaultProfileShouldNotBeFound("smeLevel.equals=" + UPDATED_SME_LEVEL);
    }

    @Test
    @Transactional
    public void getAllProfilesBySmeLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where smeLevel not equals to DEFAULT_SME_LEVEL
        defaultProfileShouldNotBeFound("smeLevel.notEquals=" + DEFAULT_SME_LEVEL);

        // Get all the profileList where smeLevel not equals to UPDATED_SME_LEVEL
        defaultProfileShouldBeFound("smeLevel.notEquals=" + UPDATED_SME_LEVEL);
    }

    @Test
    @Transactional
    public void getAllProfilesBySmeLevelIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where smeLevel in DEFAULT_SME_LEVEL or UPDATED_SME_LEVEL
        defaultProfileShouldBeFound("smeLevel.in=" + DEFAULT_SME_LEVEL + "," + UPDATED_SME_LEVEL);

        // Get all the profileList where smeLevel equals to UPDATED_SME_LEVEL
        defaultProfileShouldNotBeFound("smeLevel.in=" + UPDATED_SME_LEVEL);
    }

    @Test
    @Transactional
    public void getAllProfilesBySmeLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where smeLevel is not null
        defaultProfileShouldBeFound("smeLevel.specified=true");

        // Get all the profileList where smeLevel is null
        defaultProfileShouldNotBeFound("smeLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesBySkillsIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where skills equals to DEFAULT_SKILLS
        defaultProfileShouldBeFound("skills.equals=" + DEFAULT_SKILLS);

        // Get all the profileList where skills equals to UPDATED_SKILLS
        defaultProfileShouldNotBeFound("skills.equals=" + UPDATED_SKILLS);
    }

    @Test
    @Transactional
    public void getAllProfilesBySkillsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where skills not equals to DEFAULT_SKILLS
        defaultProfileShouldNotBeFound("skills.notEquals=" + DEFAULT_SKILLS);

        // Get all the profileList where skills not equals to UPDATED_SKILLS
        defaultProfileShouldBeFound("skills.notEquals=" + UPDATED_SKILLS);
    }

    @Test
    @Transactional
    public void getAllProfilesBySkillsIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where skills in DEFAULT_SKILLS or UPDATED_SKILLS
        defaultProfileShouldBeFound("skills.in=" + DEFAULT_SKILLS + "," + UPDATED_SKILLS);

        // Get all the profileList where skills equals to UPDATED_SKILLS
        defaultProfileShouldNotBeFound("skills.in=" + UPDATED_SKILLS);
    }

    @Test
    @Transactional
    public void getAllProfilesBySkillsIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where skills is not null
        defaultProfileShouldBeFound("skills.specified=true");

        // Get all the profileList where skills is null
        defaultProfileShouldNotBeFound("skills.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilesBySkillsContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where skills contains DEFAULT_SKILLS
        defaultProfileShouldBeFound("skills.contains=" + DEFAULT_SKILLS);

        // Get all the profileList where skills contains UPDATED_SKILLS
        defaultProfileShouldNotBeFound("skills.contains=" + UPDATED_SKILLS);
    }

    @Test
    @Transactional
    public void getAllProfilesBySkillsNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where skills does not contain DEFAULT_SKILLS
        defaultProfileShouldNotBeFound("skills.doesNotContain=" + DEFAULT_SKILLS);

        // Get all the profileList where skills does not contain UPDATED_SKILLS
        defaultProfileShouldBeFound("skills.doesNotContain=" + UPDATED_SKILLS);
    }


    @Test
    @Transactional
    public void getAllProfilesByExpertInIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where expertIn equals to DEFAULT_EXPERT_IN
        defaultProfileShouldBeFound("expertIn.equals=" + DEFAULT_EXPERT_IN);

        // Get all the profileList where expertIn equals to UPDATED_EXPERT_IN
        defaultProfileShouldNotBeFound("expertIn.equals=" + UPDATED_EXPERT_IN);
    }

    @Test
    @Transactional
    public void getAllProfilesByExpertInIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where expertIn not equals to DEFAULT_EXPERT_IN
        defaultProfileShouldNotBeFound("expertIn.notEquals=" + DEFAULT_EXPERT_IN);

        // Get all the profileList where expertIn not equals to UPDATED_EXPERT_IN
        defaultProfileShouldBeFound("expertIn.notEquals=" + UPDATED_EXPERT_IN);
    }

    @Test
    @Transactional
    public void getAllProfilesByExpertInIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where expertIn in DEFAULT_EXPERT_IN or UPDATED_EXPERT_IN
        defaultProfileShouldBeFound("expertIn.in=" + DEFAULT_EXPERT_IN + "," + UPDATED_EXPERT_IN);

        // Get all the profileList where expertIn equals to UPDATED_EXPERT_IN
        defaultProfileShouldNotBeFound("expertIn.in=" + UPDATED_EXPERT_IN);
    }

    @Test
    @Transactional
    public void getAllProfilesByExpertInIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where expertIn is not null
        defaultProfileShouldBeFound("expertIn.specified=true");

        // Get all the profileList where expertIn is null
        defaultProfileShouldNotBeFound("expertIn.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilesByExpertInContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where expertIn contains DEFAULT_EXPERT_IN
        defaultProfileShouldBeFound("expertIn.contains=" + DEFAULT_EXPERT_IN);

        // Get all the profileList where expertIn contains UPDATED_EXPERT_IN
        defaultProfileShouldNotBeFound("expertIn.contains=" + UPDATED_EXPERT_IN);
    }

    @Test
    @Transactional
    public void getAllProfilesByExpertInNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where expertIn does not contain DEFAULT_EXPERT_IN
        defaultProfileShouldNotBeFound("expertIn.doesNotContain=" + DEFAULT_EXPERT_IN);

        // Get all the profileList where expertIn does not contain UPDATED_EXPERT_IN
        defaultProfileShouldBeFound("expertIn.doesNotContain=" + UPDATED_EXPERT_IN);
    }


    @Test
    @Transactional
    public void getAllProfilesByShadowingInIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where shadowingIn equals to DEFAULT_SHADOWING_IN
        defaultProfileShouldBeFound("shadowingIn.equals=" + DEFAULT_SHADOWING_IN);

        // Get all the profileList where shadowingIn equals to UPDATED_SHADOWING_IN
        defaultProfileShouldNotBeFound("shadowingIn.equals=" + UPDATED_SHADOWING_IN);
    }

    @Test
    @Transactional
    public void getAllProfilesByShadowingInIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where shadowingIn not equals to DEFAULT_SHADOWING_IN
        defaultProfileShouldNotBeFound("shadowingIn.notEquals=" + DEFAULT_SHADOWING_IN);

        // Get all the profileList where shadowingIn not equals to UPDATED_SHADOWING_IN
        defaultProfileShouldBeFound("shadowingIn.notEquals=" + UPDATED_SHADOWING_IN);
    }

    @Test
    @Transactional
    public void getAllProfilesByShadowingInIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where shadowingIn in DEFAULT_SHADOWING_IN or UPDATED_SHADOWING_IN
        defaultProfileShouldBeFound("shadowingIn.in=" + DEFAULT_SHADOWING_IN + "," + UPDATED_SHADOWING_IN);

        // Get all the profileList where shadowingIn equals to UPDATED_SHADOWING_IN
        defaultProfileShouldNotBeFound("shadowingIn.in=" + UPDATED_SHADOWING_IN);
    }

    @Test
    @Transactional
    public void getAllProfilesByShadowingInIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where shadowingIn is not null
        defaultProfileShouldBeFound("shadowingIn.specified=true");

        // Get all the profileList where shadowingIn is null
        defaultProfileShouldNotBeFound("shadowingIn.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilesByShadowingInContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where shadowingIn contains DEFAULT_SHADOWING_IN
        defaultProfileShouldBeFound("shadowingIn.contains=" + DEFAULT_SHADOWING_IN);

        // Get all the profileList where shadowingIn contains UPDATED_SHADOWING_IN
        defaultProfileShouldNotBeFound("shadowingIn.contains=" + UPDATED_SHADOWING_IN);
    }

    @Test
    @Transactional
    public void getAllProfilesByShadowingInNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where shadowingIn does not contain DEFAULT_SHADOWING_IN
        defaultProfileShouldNotBeFound("shadowingIn.doesNotContain=" + DEFAULT_SHADOWING_IN);

        // Get all the profileList where shadowingIn does not contain UPDATED_SHADOWING_IN
        defaultProfileShouldBeFound("shadowingIn.doesNotContain=" + UPDATED_SHADOWING_IN);
    }


    @Test
    @Transactional
    public void getAllProfilesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city equals to DEFAULT_CITY
        defaultProfileShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the profileList where city equals to UPDATED_CITY
        defaultProfileShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city not equals to DEFAULT_CITY
        defaultProfileShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the profileList where city not equals to UPDATED_CITY
        defaultProfileShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city in DEFAULT_CITY or UPDATED_CITY
        defaultProfileShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the profileList where city equals to UPDATED_CITY
        defaultProfileShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city is not null
        defaultProfileShouldBeFound("city.specified=true");

        // Get all the profileList where city is null
        defaultProfileShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilesByCityContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city contains DEFAULT_CITY
        defaultProfileShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the profileList where city contains UPDATED_CITY
        defaultProfileShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllProfilesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where city does not contain DEFAULT_CITY
        defaultProfileShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the profileList where city does not contain UPDATED_CITY
        defaultProfileShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllProfilesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where location equals to DEFAULT_LOCATION
        defaultProfileShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the profileList where location equals to UPDATED_LOCATION
        defaultProfileShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllProfilesByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where location not equals to DEFAULT_LOCATION
        defaultProfileShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the profileList where location not equals to UPDATED_LOCATION
        defaultProfileShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllProfilesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultProfileShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the profileList where location equals to UPDATED_LOCATION
        defaultProfileShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllProfilesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where location is not null
        defaultProfileShouldBeFound("location.specified=true");

        // Get all the profileList where location is null
        defaultProfileShouldNotBeFound("location.specified=false");
    }
                @Test
    @Transactional
    public void getAllProfilesByLocationContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where location contains DEFAULT_LOCATION
        defaultProfileShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the profileList where location contains UPDATED_LOCATION
        defaultProfileShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllProfilesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where location does not contain DEFAULT_LOCATION
        defaultProfileShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the profileList where location does not contain UPDATED_LOCATION
        defaultProfileShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }


    @Test
    @Transactional
    public void getAllProfilesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        profile.setUser(user);
        profileRepository.saveAndFlush(profile);
        Long userId = user.getId();

        // Get all the profileList where user equals to userId
        defaultProfileShouldBeFound("userId.equals=" + userId);

        // Get all the profileList where user equals to userId + 1
        defaultProfileShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProfileShouldBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].smeLevel").value(hasItem(DEFAULT_SME_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].skills").value(hasItem(DEFAULT_SKILLS)))
            .andExpect(jsonPath("$.[*].expertIn").value(hasItem(DEFAULT_EXPERT_IN)))
            .andExpect(jsonPath("$.[*].shadowingIn").value(hasItem(DEFAULT_SHADOWING_IN)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));

        // Check, that the count call also returns 1
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProfileShouldNotBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).get();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .points(UPDATED_POINTS)
            .smeLevel(UPDATED_SME_LEVEL)
            .skills(UPDATED_SKILLS)
            .expertIn(UPDATED_EXPERT_IN)
            .shadowingIn(UPDATED_SHADOWING_IN)
            .city(UPDATED_CITY)
            .location(UPDATED_LOCATION);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);

        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testProfile.getSmeLevel()).isEqualTo(UPDATED_SME_LEVEL);
        assertThat(testProfile.getSkills()).isEqualTo(UPDATED_SKILLS);
        assertThat(testProfile.getExpertIn()).isEqualTo(UPDATED_EXPERT_IN);
        assertThat(testProfile.getShadowingIn()).isEqualTo(UPDATED_SHADOWING_IN);
        assertThat(testProfile.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testProfile.getLocation()).isEqualTo(UPDATED_LOCATION);

        // Validate the Profile in Elasticsearch
        verify(mockProfileSearchRepository, times(1)).save(testProfile);
    }

    @Test
    @Transactional
    public void updateNonExistingProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Profile in Elasticsearch
        verify(mockProfileSearchRepository, times(0)).save(profile);
    }

    @Test
    @Transactional
    public void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Delete the profile
        restProfileMockMvc.perform(delete("/api/profiles/{id}", profile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Profile in Elasticsearch
        verify(mockProfileSearchRepository, times(1)).deleteById(profile.getId());
    }

    @Test
    @Transactional
    public void searchProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);
        when(mockProfileSearchRepository.search(queryStringQuery("id:" + profile.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(profile), PageRequest.of(0, 1), 1));
        // Search the profile
        restProfileMockMvc.perform(get("/api/_search/profiles?query=id:" + profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].smeLevel").value(hasItem(DEFAULT_SME_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].skills").value(hasItem(DEFAULT_SKILLS)))
            .andExpect(jsonPath("$.[*].expertIn").value(hasItem(DEFAULT_EXPERT_IN)))
            .andExpect(jsonPath("$.[*].shadowingIn").value(hasItem(DEFAULT_SHADOWING_IN)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }
}

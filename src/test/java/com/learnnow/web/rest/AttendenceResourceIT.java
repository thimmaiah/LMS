package com.learnnow.web.rest;

import com.learnnow.TestApp;
import com.learnnow.domain.Attendence;
import com.learnnow.domain.Course;
import com.learnnow.domain.User;
import com.learnnow.repository.AttendenceRepository;
import com.learnnow.repository.search.AttendenceSearchRepository;
import com.learnnow.service.AttendenceService;
import com.learnnow.service.dto.AttendenceDTO;
import com.learnnow.service.mapper.AttendenceMapper;
import com.learnnow.web.rest.errors.ExceptionTranslator;
import com.learnnow.service.dto.AttendenceCriteria;
import com.learnnow.service.AttendenceQueryService;

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

/**
 * Integration tests for the {@link AttendenceResource} REST controller.
 */
@SpringBootTest(classes = TestApp.class)
public class AttendenceResourceIT {

    private static final Boolean DEFAULT_ATTENDENDED = false;
    private static final Boolean UPDATED_ATTENDENDED = true;

    private static final Integer DEFAULT_DAY = 1;
    private static final Integer UPDATED_DAY = 2;
    private static final Integer SMALLER_DAY = 1 - 1;

    private static final Integer DEFAULT_RATING = 0;
    private static final Integer UPDATED_RATING = 1;
    private static final Integer SMALLER_RATING = 0 - 1;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    @Autowired
    private AttendenceRepository attendenceRepository;

    @Autowired
    private AttendenceMapper attendenceMapper;

    @Autowired
    private AttendenceService attendenceService;

    /**
     * This repository is mocked in the com.learnnow.repository.search test package.
     *
     * @see com.learnnow.repository.search.AttendenceSearchRepositoryMockConfiguration
     */
    @Autowired
    private AttendenceSearchRepository mockAttendenceSearchRepository;

    @Autowired
    private AttendenceQueryService attendenceQueryService;

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

    private MockMvc restAttendenceMockMvc;

    private Attendence attendence;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AttendenceResource attendenceResource = new AttendenceResource(attendenceService, attendenceQueryService);
        this.restAttendenceMockMvc = MockMvcBuilders.standaloneSetup(attendenceResource)
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
    public static Attendence createEntity(EntityManager em) {
        Attendence attendence = new Attendence()
            .attendended(DEFAULT_ATTENDENDED)
            .day(DEFAULT_DAY)
            .rating(DEFAULT_RATING)
            .comments(DEFAULT_COMMENTS);
        return attendence;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attendence createUpdatedEntity(EntityManager em) {
        Attendence attendence = new Attendence()
            .attendended(UPDATED_ATTENDENDED)
            .day(UPDATED_DAY)
            .rating(UPDATED_RATING)
            .comments(UPDATED_COMMENTS);
        return attendence;
    }

    @BeforeEach
    public void initTest() {
        attendence = createEntity(em);
    }

    @Test
    @Transactional
    public void createAttendence() throws Exception {
        int databaseSizeBeforeCreate = attendenceRepository.findAll().size();

        // Create the Attendence
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(attendence);
        restAttendenceMockMvc.perform(post("/api/attendences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attendenceDTO)))
            .andExpect(status().isCreated());

        // Validate the Attendence in the database
        List<Attendence> attendenceList = attendenceRepository.findAll();
        assertThat(attendenceList).hasSize(databaseSizeBeforeCreate + 1);
        Attendence testAttendence = attendenceList.get(attendenceList.size() - 1);
        assertThat(testAttendence.isAttendended()).isEqualTo(DEFAULT_ATTENDENDED);
        assertThat(testAttendence.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testAttendence.getRating()).isEqualTo(DEFAULT_RATING);
        assertThat(testAttendence.getComments()).isEqualTo(DEFAULT_COMMENTS);

        // Validate the Attendence in Elasticsearch
        verify(mockAttendenceSearchRepository, times(1)).save(testAttendence);
    }

    @Test
    @Transactional
    public void createAttendenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = attendenceRepository.findAll().size();

        // Create the Attendence with an existing ID
        attendence.setId(1L);
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(attendence);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAttendenceMockMvc.perform(post("/api/attendences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attendenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Attendence in the database
        List<Attendence> attendenceList = attendenceRepository.findAll();
        assertThat(attendenceList).hasSize(databaseSizeBeforeCreate);

        // Validate the Attendence in Elasticsearch
        verify(mockAttendenceSearchRepository, times(0)).save(attendence);
    }


    @Test
    @Transactional
    public void getAllAttendences() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList
        restAttendenceMockMvc.perform(get("/api/attendences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attendence.getId().intValue())))
            .andExpect(jsonPath("$.[*].attendended").value(hasItem(DEFAULT_ATTENDENDED.booleanValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }
    
    @Test
    @Transactional
    public void getAttendence() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get the attendence
        restAttendenceMockMvc.perform(get("/api/attendences/{id}", attendence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(attendence.getId().intValue()))
            .andExpect(jsonPath("$.attendended").value(DEFAULT_ATTENDENDED.booleanValue()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS));
    }


    @Test
    @Transactional
    public void getAttendencesByIdFiltering() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        Long id = attendence.getId();

        defaultAttendenceShouldBeFound("id.equals=" + id);
        defaultAttendenceShouldNotBeFound("id.notEquals=" + id);

        defaultAttendenceShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAttendenceShouldNotBeFound("id.greaterThan=" + id);

        defaultAttendenceShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAttendenceShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAttendencesByAttendendedIsEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where attendended equals to DEFAULT_ATTENDENDED
        defaultAttendenceShouldBeFound("attendended.equals=" + DEFAULT_ATTENDENDED);

        // Get all the attendenceList where attendended equals to UPDATED_ATTENDENDED
        defaultAttendenceShouldNotBeFound("attendended.equals=" + UPDATED_ATTENDENDED);
    }

    @Test
    @Transactional
    public void getAllAttendencesByAttendendedIsNotEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where attendended not equals to DEFAULT_ATTENDENDED
        defaultAttendenceShouldNotBeFound("attendended.notEquals=" + DEFAULT_ATTENDENDED);

        // Get all the attendenceList where attendended not equals to UPDATED_ATTENDENDED
        defaultAttendenceShouldBeFound("attendended.notEquals=" + UPDATED_ATTENDENDED);
    }

    @Test
    @Transactional
    public void getAllAttendencesByAttendendedIsInShouldWork() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where attendended in DEFAULT_ATTENDENDED or UPDATED_ATTENDENDED
        defaultAttendenceShouldBeFound("attendended.in=" + DEFAULT_ATTENDENDED + "," + UPDATED_ATTENDENDED);

        // Get all the attendenceList where attendended equals to UPDATED_ATTENDENDED
        defaultAttendenceShouldNotBeFound("attendended.in=" + UPDATED_ATTENDENDED);
    }

    @Test
    @Transactional
    public void getAllAttendencesByAttendendedIsNullOrNotNull() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where attendended is not null
        defaultAttendenceShouldBeFound("attendended.specified=true");

        // Get all the attendenceList where attendended is null
        defaultAttendenceShouldNotBeFound("attendended.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttendencesByDayIsEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where day equals to DEFAULT_DAY
        defaultAttendenceShouldBeFound("day.equals=" + DEFAULT_DAY);

        // Get all the attendenceList where day equals to UPDATED_DAY
        defaultAttendenceShouldNotBeFound("day.equals=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllAttendencesByDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where day not equals to DEFAULT_DAY
        defaultAttendenceShouldNotBeFound("day.notEquals=" + DEFAULT_DAY);

        // Get all the attendenceList where day not equals to UPDATED_DAY
        defaultAttendenceShouldBeFound("day.notEquals=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllAttendencesByDayIsInShouldWork() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where day in DEFAULT_DAY or UPDATED_DAY
        defaultAttendenceShouldBeFound("day.in=" + DEFAULT_DAY + "," + UPDATED_DAY);

        // Get all the attendenceList where day equals to UPDATED_DAY
        defaultAttendenceShouldNotBeFound("day.in=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllAttendencesByDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where day is not null
        defaultAttendenceShouldBeFound("day.specified=true");

        // Get all the attendenceList where day is null
        defaultAttendenceShouldNotBeFound("day.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttendencesByDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where day is greater than or equal to DEFAULT_DAY
        defaultAttendenceShouldBeFound("day.greaterThanOrEqual=" + DEFAULT_DAY);

        // Get all the attendenceList where day is greater than or equal to (DEFAULT_DAY + 1)
        defaultAttendenceShouldNotBeFound("day.greaterThanOrEqual=" + (DEFAULT_DAY + 1));
    }

    @Test
    @Transactional
    public void getAllAttendencesByDayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where day is less than or equal to DEFAULT_DAY
        defaultAttendenceShouldBeFound("day.lessThanOrEqual=" + DEFAULT_DAY);

        // Get all the attendenceList where day is less than or equal to SMALLER_DAY
        defaultAttendenceShouldNotBeFound("day.lessThanOrEqual=" + SMALLER_DAY);
    }

    @Test
    @Transactional
    public void getAllAttendencesByDayIsLessThanSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where day is less than DEFAULT_DAY
        defaultAttendenceShouldNotBeFound("day.lessThan=" + DEFAULT_DAY);

        // Get all the attendenceList where day is less than (DEFAULT_DAY + 1)
        defaultAttendenceShouldBeFound("day.lessThan=" + (DEFAULT_DAY + 1));
    }

    @Test
    @Transactional
    public void getAllAttendencesByDayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where day is greater than DEFAULT_DAY
        defaultAttendenceShouldNotBeFound("day.greaterThan=" + DEFAULT_DAY);

        // Get all the attendenceList where day is greater than SMALLER_DAY
        defaultAttendenceShouldBeFound("day.greaterThan=" + SMALLER_DAY);
    }


    @Test
    @Transactional
    public void getAllAttendencesByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where rating equals to DEFAULT_RATING
        defaultAttendenceShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the attendenceList where rating equals to UPDATED_RATING
        defaultAttendenceShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllAttendencesByRatingIsNotEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where rating not equals to DEFAULT_RATING
        defaultAttendenceShouldNotBeFound("rating.notEquals=" + DEFAULT_RATING);

        // Get all the attendenceList where rating not equals to UPDATED_RATING
        defaultAttendenceShouldBeFound("rating.notEquals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllAttendencesByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultAttendenceShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the attendenceList where rating equals to UPDATED_RATING
        defaultAttendenceShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    public void getAllAttendencesByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where rating is not null
        defaultAttendenceShouldBeFound("rating.specified=true");

        // Get all the attendenceList where rating is null
        defaultAttendenceShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    public void getAllAttendencesByRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where rating is greater than or equal to DEFAULT_RATING
        defaultAttendenceShouldBeFound("rating.greaterThanOrEqual=" + DEFAULT_RATING);

        // Get all the attendenceList where rating is greater than or equal to (DEFAULT_RATING + 1)
        defaultAttendenceShouldNotBeFound("rating.greaterThanOrEqual=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    public void getAllAttendencesByRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where rating is less than or equal to DEFAULT_RATING
        defaultAttendenceShouldBeFound("rating.lessThanOrEqual=" + DEFAULT_RATING);

        // Get all the attendenceList where rating is less than or equal to SMALLER_RATING
        defaultAttendenceShouldNotBeFound("rating.lessThanOrEqual=" + SMALLER_RATING);
    }

    @Test
    @Transactional
    public void getAllAttendencesByRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where rating is less than DEFAULT_RATING
        defaultAttendenceShouldNotBeFound("rating.lessThan=" + DEFAULT_RATING);

        // Get all the attendenceList where rating is less than (DEFAULT_RATING + 1)
        defaultAttendenceShouldBeFound("rating.lessThan=" + (DEFAULT_RATING + 1));
    }

    @Test
    @Transactional
    public void getAllAttendencesByRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where rating is greater than DEFAULT_RATING
        defaultAttendenceShouldNotBeFound("rating.greaterThan=" + DEFAULT_RATING);

        // Get all the attendenceList where rating is greater than SMALLER_RATING
        defaultAttendenceShouldBeFound("rating.greaterThan=" + SMALLER_RATING);
    }


    @Test
    @Transactional
    public void getAllAttendencesByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where comments equals to DEFAULT_COMMENTS
        defaultAttendenceShouldBeFound("comments.equals=" + DEFAULT_COMMENTS);

        // Get all the attendenceList where comments equals to UPDATED_COMMENTS
        defaultAttendenceShouldNotBeFound("comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllAttendencesByCommentsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where comments not equals to DEFAULT_COMMENTS
        defaultAttendenceShouldNotBeFound("comments.notEquals=" + DEFAULT_COMMENTS);

        // Get all the attendenceList where comments not equals to UPDATED_COMMENTS
        defaultAttendenceShouldBeFound("comments.notEquals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllAttendencesByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where comments in DEFAULT_COMMENTS or UPDATED_COMMENTS
        defaultAttendenceShouldBeFound("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS);

        // Get all the attendenceList where comments equals to UPDATED_COMMENTS
        defaultAttendenceShouldNotBeFound("comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllAttendencesByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where comments is not null
        defaultAttendenceShouldBeFound("comments.specified=true");

        // Get all the attendenceList where comments is null
        defaultAttendenceShouldNotBeFound("comments.specified=false");
    }
                @Test
    @Transactional
    public void getAllAttendencesByCommentsContainsSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where comments contains DEFAULT_COMMENTS
        defaultAttendenceShouldBeFound("comments.contains=" + DEFAULT_COMMENTS);

        // Get all the attendenceList where comments contains UPDATED_COMMENTS
        defaultAttendenceShouldNotBeFound("comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    public void getAllAttendencesByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        // Get all the attendenceList where comments does not contain DEFAULT_COMMENTS
        defaultAttendenceShouldNotBeFound("comments.doesNotContain=" + DEFAULT_COMMENTS);

        // Get all the attendenceList where comments does not contain UPDATED_COMMENTS
        defaultAttendenceShouldBeFound("comments.doesNotContain=" + UPDATED_COMMENTS);
    }


    @Test
    @Transactional
    public void getAllAttendencesByCourseIsEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);
        Course course = CourseResourceIT.createEntity(em);
        em.persist(course);
        em.flush();
        attendence.setCourse(course);
        attendenceRepository.saveAndFlush(attendence);
        Long courseId = course.getId();

        // Get all the attendenceList where course equals to courseId
        defaultAttendenceShouldBeFound("courseId.equals=" + courseId);

        // Get all the attendenceList where course equals to courseId + 1
        defaultAttendenceShouldNotBeFound("courseId.equals=" + (courseId + 1));
    }


    @Test
    @Transactional
    public void getAllAttendencesByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        attendence.setUser(user);
        attendenceRepository.saveAndFlush(attendence);
        Long userId = user.getId();

        // Get all the attendenceList where user equals to userId
        defaultAttendenceShouldBeFound("userId.equals=" + userId);

        // Get all the attendenceList where user equals to userId + 1
        defaultAttendenceShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAttendenceShouldBeFound(String filter) throws Exception {
        restAttendenceMockMvc.perform(get("/api/attendences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attendence.getId().intValue())))
            .andExpect(jsonPath("$.[*].attendended").value(hasItem(DEFAULT_ATTENDENDED.booleanValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));

        // Check, that the count call also returns 1
        restAttendenceMockMvc.perform(get("/api/attendences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAttendenceShouldNotBeFound(String filter) throws Exception {
        restAttendenceMockMvc.perform(get("/api/attendences?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAttendenceMockMvc.perform(get("/api/attendences/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAttendence() throws Exception {
        // Get the attendence
        restAttendenceMockMvc.perform(get("/api/attendences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAttendence() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        int databaseSizeBeforeUpdate = attendenceRepository.findAll().size();

        // Update the attendence
        Attendence updatedAttendence = attendenceRepository.findById(attendence.getId()).get();
        // Disconnect from session so that the updates on updatedAttendence are not directly saved in db
        em.detach(updatedAttendence);
        updatedAttendence
            .attendended(UPDATED_ATTENDENDED)
            .day(UPDATED_DAY)
            .rating(UPDATED_RATING)
            .comments(UPDATED_COMMENTS);
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(updatedAttendence);

        restAttendenceMockMvc.perform(put("/api/attendences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attendenceDTO)))
            .andExpect(status().isOk());

        // Validate the Attendence in the database
        List<Attendence> attendenceList = attendenceRepository.findAll();
        assertThat(attendenceList).hasSize(databaseSizeBeforeUpdate);
        Attendence testAttendence = attendenceList.get(attendenceList.size() - 1);
        assertThat(testAttendence.isAttendended()).isEqualTo(UPDATED_ATTENDENDED);
        assertThat(testAttendence.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testAttendence.getRating()).isEqualTo(UPDATED_RATING);
        assertThat(testAttendence.getComments()).isEqualTo(UPDATED_COMMENTS);

        // Validate the Attendence in Elasticsearch
        verify(mockAttendenceSearchRepository, times(1)).save(testAttendence);
    }

    @Test
    @Transactional
    public void updateNonExistingAttendence() throws Exception {
        int databaseSizeBeforeUpdate = attendenceRepository.findAll().size();

        // Create the Attendence
        AttendenceDTO attendenceDTO = attendenceMapper.toDto(attendence);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAttendenceMockMvc.perform(put("/api/attendences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(attendenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Attendence in the database
        List<Attendence> attendenceList = attendenceRepository.findAll();
        assertThat(attendenceList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Attendence in Elasticsearch
        verify(mockAttendenceSearchRepository, times(0)).save(attendence);
    }

    @Test
    @Transactional
    public void deleteAttendence() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);

        int databaseSizeBeforeDelete = attendenceRepository.findAll().size();

        // Delete the attendence
        restAttendenceMockMvc.perform(delete("/api/attendences/{id}", attendence.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Attendence> attendenceList = attendenceRepository.findAll();
        assertThat(attendenceList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Attendence in Elasticsearch
        verify(mockAttendenceSearchRepository, times(1)).deleteById(attendence.getId());
    }

    @Test
    @Transactional
    public void searchAttendence() throws Exception {
        // Initialize the database
        attendenceRepository.saveAndFlush(attendence);
        when(mockAttendenceSearchRepository.search(queryStringQuery("id:" + attendence.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(attendence), PageRequest.of(0, 1), 1));
        // Search the attendence
        restAttendenceMockMvc.perform(get("/api/_search/attendences?query=id:" + attendence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(attendence.getId().intValue())))
            .andExpect(jsonPath("$.[*].attendended").value(hasItem(DEFAULT_ATTENDENDED.booleanValue())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)));
    }
}

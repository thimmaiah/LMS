package com.learnnow.web.rest;

import com.learnnow.TestApp;
import com.learnnow.domain.Course;
import com.learnnow.domain.Profile;
import com.learnnow.domain.Company;
import com.learnnow.repository.CourseRepository;
import com.learnnow.repository.search.CourseSearchRepository;
import com.learnnow.service.CourseService;
import com.learnnow.service.dto.CourseDTO;
import com.learnnow.service.mapper.CourseMapper;
import com.learnnow.web.rest.errors.ExceptionTranslator;
import com.learnnow.service.dto.CourseCriteria;
import com.learnnow.service.CourseQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
 * Integration tests for the {@link CourseResource} REST controller.
 */
@SpringBootTest(classes = TestApp.class)
public class CourseResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Float DEFAULT_DURATION_IN_DAYS = 0F;
    private static final Float UPDATED_DURATION_IN_DAYS = 1F;
    private static final Float SMALLER_DURATION_IN_DAYS = 0F - 1F;

    private static final Float DEFAULT_HOURS_PER_DAY = 0F;
    private static final Float UPDATED_HOURS_PER_DAY = 1F;
    private static final Float SMALLER_HOURS_PER_DAY = 0F - 1F;

    private static final String DEFAULT_SURVEY_LINK = "AAAAAAAAAA";
    private static final String UPDATED_SURVEY_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private CourseRepository courseRepository;

    @Mock
    private CourseRepository courseRepositoryMock;

    @Autowired
    private CourseMapper courseMapper;

    @Mock
    private CourseService courseServiceMock;

    @Autowired
    private CourseService courseService;

    /**
     * This repository is mocked in the com.learnnow.repository.search test package.
     *
     * @see com.learnnow.repository.search.CourseSearchRepositoryMockConfiguration
     */
    @Autowired
    private CourseSearchRepository mockCourseSearchRepository;

    @Autowired
    private CourseQueryService courseQueryService;

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

    private MockMvc restCourseMockMvc;

    private Course course;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseResource courseResource = new CourseResource(courseService, courseQueryService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
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
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .name(DEFAULT_NAME)
            .durationInDays(DEFAULT_DURATION_IN_DAYS)
            .hoursPerDay(DEFAULT_HOURS_PER_DAY)
            .surveyLink(DEFAULT_SURVEY_LINK)
            .tags(DEFAULT_TAGS)
            .city(DEFAULT_CITY)
            .location(DEFAULT_LOCATION)
            .startDate(DEFAULT_START_DATE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return course;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course()
            .name(UPDATED_NAME)
            .durationInDays(UPDATED_DURATION_IN_DAYS)
            .hoursPerDay(UPDATED_HOURS_PER_DAY)
            .surveyLink(UPDATED_SURVEY_LINK)
            .tags(UPDATED_TAGS)
            .city(UPDATED_CITY)
            .location(UPDATED_LOCATION)
            .startDate(UPDATED_START_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        return course;
    }

    @BeforeEach
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getDurationInDays()).isEqualTo(DEFAULT_DURATION_IN_DAYS);
        assertThat(testCourse.getHoursPerDay()).isEqualTo(DEFAULT_HOURS_PER_DAY);
        assertThat(testCourse.getSurveyLink()).isEqualTo(DEFAULT_SURVEY_LINK);
        assertThat(testCourse.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testCourse.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCourse.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testCourse.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCourse.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCourse.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);

        // Validate the Course in Elasticsearch
        verify(mockCourseSearchRepository, times(1)).save(testCourse);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);
        CourseDTO courseDTO = courseMapper.toDto(course);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Course in Elasticsearch
        verify(mockCourseSearchRepository, times(0)).save(course);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setName(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationInDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setDurationInDays(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHoursPerDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setHoursPerDay(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setStartDate(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setCreatedAt(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseRepository.findAll().size();
        // set the field null
        course.setUpdatedAt(null);

        // Create the Course, which fails.
        CourseDTO courseDTO = courseMapper.toDto(course);

        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].durationInDays").value(hasItem(DEFAULT_DURATION_IN_DAYS.doubleValue())))
            .andExpect(jsonPath("$.[*].hoursPerDay").value(hasItem(DEFAULT_HOURS_PER_DAY.doubleValue())))
            .andExpect(jsonPath("$.[*].surveyLink").value(hasItem(DEFAULT_SURVEY_LINK)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllCoursesWithEagerRelationshipsIsEnabled() throws Exception {
        CourseResource courseResource = new CourseResource(courseServiceMock, courseQueryService);
        when(courseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCourseMockMvc.perform(get("/api/courses?eagerload=true"))
        .andExpect(status().isOk());

        verify(courseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllCoursesWithEagerRelationshipsIsNotEnabled() throws Exception {
        CourseResource courseResource = new CourseResource(courseServiceMock, courseQueryService);
            when(courseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restCourseMockMvc.perform(get("/api/courses?eagerload=true"))
        .andExpect(status().isOk());

            verify(courseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.durationInDays").value(DEFAULT_DURATION_IN_DAYS.doubleValue()))
            .andExpect(jsonPath("$.hoursPerDay").value(DEFAULT_HOURS_PER_DAY.doubleValue()))
            .andExpect(jsonPath("$.surveyLink").value(DEFAULT_SURVEY_LINK))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }


    @Test
    @Transactional
    public void getCoursesByIdFiltering() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        Long id = course.getId();

        defaultCourseShouldBeFound("id.equals=" + id);
        defaultCourseShouldNotBeFound("id.notEquals=" + id);

        defaultCourseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.greaterThan=" + id);

        defaultCourseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCourseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCoursesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name equals to DEFAULT_NAME
        defaultCourseShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name not equals to DEFAULT_NAME
        defaultCourseShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the courseList where name not equals to UPDATED_NAME
        defaultCourseShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCourseShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name is not null
        defaultCourseShouldBeFound("name.specified=true");

        // Get all the courseList where name is null
        defaultCourseShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesByNameContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name contains DEFAULT_NAME
        defaultCourseShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the courseList where name contains UPDATED_NAME
        defaultCourseShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name does not contain DEFAULT_NAME
        defaultCourseShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the courseList where name does not contain UPDATED_NAME
        defaultCourseShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllCoursesByDurationInDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where durationInDays equals to DEFAULT_DURATION_IN_DAYS
        defaultCourseShouldBeFound("durationInDays.equals=" + DEFAULT_DURATION_IN_DAYS);

        // Get all the courseList where durationInDays equals to UPDATED_DURATION_IN_DAYS
        defaultCourseShouldNotBeFound("durationInDays.equals=" + UPDATED_DURATION_IN_DAYS);
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationInDaysIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where durationInDays not equals to DEFAULT_DURATION_IN_DAYS
        defaultCourseShouldNotBeFound("durationInDays.notEquals=" + DEFAULT_DURATION_IN_DAYS);

        // Get all the courseList where durationInDays not equals to UPDATED_DURATION_IN_DAYS
        defaultCourseShouldBeFound("durationInDays.notEquals=" + UPDATED_DURATION_IN_DAYS);
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationInDaysIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where durationInDays in DEFAULT_DURATION_IN_DAYS or UPDATED_DURATION_IN_DAYS
        defaultCourseShouldBeFound("durationInDays.in=" + DEFAULT_DURATION_IN_DAYS + "," + UPDATED_DURATION_IN_DAYS);

        // Get all the courseList where durationInDays equals to UPDATED_DURATION_IN_DAYS
        defaultCourseShouldNotBeFound("durationInDays.in=" + UPDATED_DURATION_IN_DAYS);
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationInDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where durationInDays is not null
        defaultCourseShouldBeFound("durationInDays.specified=true");

        // Get all the courseList where durationInDays is null
        defaultCourseShouldNotBeFound("durationInDays.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationInDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where durationInDays is greater than or equal to DEFAULT_DURATION_IN_DAYS
        defaultCourseShouldBeFound("durationInDays.greaterThanOrEqual=" + DEFAULT_DURATION_IN_DAYS);

        // Get all the courseList where durationInDays is greater than or equal to (DEFAULT_DURATION_IN_DAYS + 1)
        defaultCourseShouldNotBeFound("durationInDays.greaterThanOrEqual=" + (DEFAULT_DURATION_IN_DAYS + 1));
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationInDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where durationInDays is less than or equal to DEFAULT_DURATION_IN_DAYS
        defaultCourseShouldBeFound("durationInDays.lessThanOrEqual=" + DEFAULT_DURATION_IN_DAYS);

        // Get all the courseList where durationInDays is less than or equal to SMALLER_DURATION_IN_DAYS
        defaultCourseShouldNotBeFound("durationInDays.lessThanOrEqual=" + SMALLER_DURATION_IN_DAYS);
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationInDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where durationInDays is less than DEFAULT_DURATION_IN_DAYS
        defaultCourseShouldNotBeFound("durationInDays.lessThan=" + DEFAULT_DURATION_IN_DAYS);

        // Get all the courseList where durationInDays is less than (DEFAULT_DURATION_IN_DAYS + 1)
        defaultCourseShouldBeFound("durationInDays.lessThan=" + (DEFAULT_DURATION_IN_DAYS + 1));
    }

    @Test
    @Transactional
    public void getAllCoursesByDurationInDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where durationInDays is greater than DEFAULT_DURATION_IN_DAYS
        defaultCourseShouldNotBeFound("durationInDays.greaterThan=" + DEFAULT_DURATION_IN_DAYS);

        // Get all the courseList where durationInDays is greater than SMALLER_DURATION_IN_DAYS
        defaultCourseShouldBeFound("durationInDays.greaterThan=" + SMALLER_DURATION_IN_DAYS);
    }


    @Test
    @Transactional
    public void getAllCoursesByHoursPerDayIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hoursPerDay equals to DEFAULT_HOURS_PER_DAY
        defaultCourseShouldBeFound("hoursPerDay.equals=" + DEFAULT_HOURS_PER_DAY);

        // Get all the courseList where hoursPerDay equals to UPDATED_HOURS_PER_DAY
        defaultCourseShouldNotBeFound("hoursPerDay.equals=" + UPDATED_HOURS_PER_DAY);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursPerDayIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hoursPerDay not equals to DEFAULT_HOURS_PER_DAY
        defaultCourseShouldNotBeFound("hoursPerDay.notEquals=" + DEFAULT_HOURS_PER_DAY);

        // Get all the courseList where hoursPerDay not equals to UPDATED_HOURS_PER_DAY
        defaultCourseShouldBeFound("hoursPerDay.notEquals=" + UPDATED_HOURS_PER_DAY);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursPerDayIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hoursPerDay in DEFAULT_HOURS_PER_DAY or UPDATED_HOURS_PER_DAY
        defaultCourseShouldBeFound("hoursPerDay.in=" + DEFAULT_HOURS_PER_DAY + "," + UPDATED_HOURS_PER_DAY);

        // Get all the courseList where hoursPerDay equals to UPDATED_HOURS_PER_DAY
        defaultCourseShouldNotBeFound("hoursPerDay.in=" + UPDATED_HOURS_PER_DAY);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursPerDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hoursPerDay is not null
        defaultCourseShouldBeFound("hoursPerDay.specified=true");

        // Get all the courseList where hoursPerDay is null
        defaultCourseShouldNotBeFound("hoursPerDay.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursPerDayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hoursPerDay is greater than or equal to DEFAULT_HOURS_PER_DAY
        defaultCourseShouldBeFound("hoursPerDay.greaterThanOrEqual=" + DEFAULT_HOURS_PER_DAY);

        // Get all the courseList where hoursPerDay is greater than or equal to (DEFAULT_HOURS_PER_DAY + 1)
        defaultCourseShouldNotBeFound("hoursPerDay.greaterThanOrEqual=" + (DEFAULT_HOURS_PER_DAY + 1));
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursPerDayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hoursPerDay is less than or equal to DEFAULT_HOURS_PER_DAY
        defaultCourseShouldBeFound("hoursPerDay.lessThanOrEqual=" + DEFAULT_HOURS_PER_DAY);

        // Get all the courseList where hoursPerDay is less than or equal to SMALLER_HOURS_PER_DAY
        defaultCourseShouldNotBeFound("hoursPerDay.lessThanOrEqual=" + SMALLER_HOURS_PER_DAY);
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursPerDayIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hoursPerDay is less than DEFAULT_HOURS_PER_DAY
        defaultCourseShouldNotBeFound("hoursPerDay.lessThan=" + DEFAULT_HOURS_PER_DAY);

        // Get all the courseList where hoursPerDay is less than (DEFAULT_HOURS_PER_DAY + 1)
        defaultCourseShouldBeFound("hoursPerDay.lessThan=" + (DEFAULT_HOURS_PER_DAY + 1));
    }

    @Test
    @Transactional
    public void getAllCoursesByHoursPerDayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where hoursPerDay is greater than DEFAULT_HOURS_PER_DAY
        defaultCourseShouldNotBeFound("hoursPerDay.greaterThan=" + DEFAULT_HOURS_PER_DAY);

        // Get all the courseList where hoursPerDay is greater than SMALLER_HOURS_PER_DAY
        defaultCourseShouldBeFound("hoursPerDay.greaterThan=" + SMALLER_HOURS_PER_DAY);
    }


    @Test
    @Transactional
    public void getAllCoursesBySurveyLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where surveyLink equals to DEFAULT_SURVEY_LINK
        defaultCourseShouldBeFound("surveyLink.equals=" + DEFAULT_SURVEY_LINK);

        // Get all the courseList where surveyLink equals to UPDATED_SURVEY_LINK
        defaultCourseShouldNotBeFound("surveyLink.equals=" + UPDATED_SURVEY_LINK);
    }

    @Test
    @Transactional
    public void getAllCoursesBySurveyLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where surveyLink not equals to DEFAULT_SURVEY_LINK
        defaultCourseShouldNotBeFound("surveyLink.notEquals=" + DEFAULT_SURVEY_LINK);

        // Get all the courseList where surveyLink not equals to UPDATED_SURVEY_LINK
        defaultCourseShouldBeFound("surveyLink.notEquals=" + UPDATED_SURVEY_LINK);
    }

    @Test
    @Transactional
    public void getAllCoursesBySurveyLinkIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where surveyLink in DEFAULT_SURVEY_LINK or UPDATED_SURVEY_LINK
        defaultCourseShouldBeFound("surveyLink.in=" + DEFAULT_SURVEY_LINK + "," + UPDATED_SURVEY_LINK);

        // Get all the courseList where surveyLink equals to UPDATED_SURVEY_LINK
        defaultCourseShouldNotBeFound("surveyLink.in=" + UPDATED_SURVEY_LINK);
    }

    @Test
    @Transactional
    public void getAllCoursesBySurveyLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where surveyLink is not null
        defaultCourseShouldBeFound("surveyLink.specified=true");

        // Get all the courseList where surveyLink is null
        defaultCourseShouldNotBeFound("surveyLink.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesBySurveyLinkContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where surveyLink contains DEFAULT_SURVEY_LINK
        defaultCourseShouldBeFound("surveyLink.contains=" + DEFAULT_SURVEY_LINK);

        // Get all the courseList where surveyLink contains UPDATED_SURVEY_LINK
        defaultCourseShouldNotBeFound("surveyLink.contains=" + UPDATED_SURVEY_LINK);
    }

    @Test
    @Transactional
    public void getAllCoursesBySurveyLinkNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where surveyLink does not contain DEFAULT_SURVEY_LINK
        defaultCourseShouldNotBeFound("surveyLink.doesNotContain=" + DEFAULT_SURVEY_LINK);

        // Get all the courseList where surveyLink does not contain UPDATED_SURVEY_LINK
        defaultCourseShouldBeFound("surveyLink.doesNotContain=" + UPDATED_SURVEY_LINK);
    }


    @Test
    @Transactional
    public void getAllCoursesByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where tags equals to DEFAULT_TAGS
        defaultCourseShouldBeFound("tags.equals=" + DEFAULT_TAGS);

        // Get all the courseList where tags equals to UPDATED_TAGS
        defaultCourseShouldNotBeFound("tags.equals=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    public void getAllCoursesByTagsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where tags not equals to DEFAULT_TAGS
        defaultCourseShouldNotBeFound("tags.notEquals=" + DEFAULT_TAGS);

        // Get all the courseList where tags not equals to UPDATED_TAGS
        defaultCourseShouldBeFound("tags.notEquals=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    public void getAllCoursesByTagsIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where tags in DEFAULT_TAGS or UPDATED_TAGS
        defaultCourseShouldBeFound("tags.in=" + DEFAULT_TAGS + "," + UPDATED_TAGS);

        // Get all the courseList where tags equals to UPDATED_TAGS
        defaultCourseShouldNotBeFound("tags.in=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    public void getAllCoursesByTagsIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where tags is not null
        defaultCourseShouldBeFound("tags.specified=true");

        // Get all the courseList where tags is null
        defaultCourseShouldNotBeFound("tags.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesByTagsContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where tags contains DEFAULT_TAGS
        defaultCourseShouldBeFound("tags.contains=" + DEFAULT_TAGS);

        // Get all the courseList where tags contains UPDATED_TAGS
        defaultCourseShouldNotBeFound("tags.contains=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    public void getAllCoursesByTagsNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where tags does not contain DEFAULT_TAGS
        defaultCourseShouldNotBeFound("tags.doesNotContain=" + DEFAULT_TAGS);

        // Get all the courseList where tags does not contain UPDATED_TAGS
        defaultCourseShouldBeFound("tags.doesNotContain=" + UPDATED_TAGS);
    }


    @Test
    @Transactional
    public void getAllCoursesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where city equals to DEFAULT_CITY
        defaultCourseShouldBeFound("city.equals=" + DEFAULT_CITY);

        // Get all the courseList where city equals to UPDATED_CITY
        defaultCourseShouldNotBeFound("city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCoursesByCityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where city not equals to DEFAULT_CITY
        defaultCourseShouldNotBeFound("city.notEquals=" + DEFAULT_CITY);

        // Get all the courseList where city not equals to UPDATED_CITY
        defaultCourseShouldBeFound("city.notEquals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCoursesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where city in DEFAULT_CITY or UPDATED_CITY
        defaultCourseShouldBeFound("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY);

        // Get all the courseList where city equals to UPDATED_CITY
        defaultCourseShouldNotBeFound("city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCoursesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where city is not null
        defaultCourseShouldBeFound("city.specified=true");

        // Get all the courseList where city is null
        defaultCourseShouldNotBeFound("city.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesByCityContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where city contains DEFAULT_CITY
        defaultCourseShouldBeFound("city.contains=" + DEFAULT_CITY);

        // Get all the courseList where city contains UPDATED_CITY
        defaultCourseShouldNotBeFound("city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    public void getAllCoursesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where city does not contain DEFAULT_CITY
        defaultCourseShouldNotBeFound("city.doesNotContain=" + DEFAULT_CITY);

        // Get all the courseList where city does not contain UPDATED_CITY
        defaultCourseShouldBeFound("city.doesNotContain=" + UPDATED_CITY);
    }


    @Test
    @Transactional
    public void getAllCoursesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where location equals to DEFAULT_LOCATION
        defaultCourseShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the courseList where location equals to UPDATED_LOCATION
        defaultCourseShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByLocationIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where location not equals to DEFAULT_LOCATION
        defaultCourseShouldNotBeFound("location.notEquals=" + DEFAULT_LOCATION);

        // Get all the courseList where location not equals to UPDATED_LOCATION
        defaultCourseShouldBeFound("location.notEquals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultCourseShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the courseList where location equals to UPDATED_LOCATION
        defaultCourseShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where location is not null
        defaultCourseShouldBeFound("location.specified=true");

        // Get all the courseList where location is null
        defaultCourseShouldNotBeFound("location.specified=false");
    }
                @Test
    @Transactional
    public void getAllCoursesByLocationContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where location contains DEFAULT_LOCATION
        defaultCourseShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the courseList where location contains UPDATED_LOCATION
        defaultCourseShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllCoursesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where location does not contain DEFAULT_LOCATION
        defaultCourseShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the courseList where location does not contain UPDATED_LOCATION
        defaultCourseShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }


    @Test
    @Transactional
    public void getAllCoursesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate equals to DEFAULT_START_DATE
        defaultCourseShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate equals to UPDATED_START_DATE
        defaultCourseShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate not equals to DEFAULT_START_DATE
        defaultCourseShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the courseList where startDate not equals to UPDATED_START_DATE
        defaultCourseShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultCourseShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the courseList where startDate equals to UPDATED_START_DATE
        defaultCourseShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllCoursesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where startDate is not null
        defaultCourseShouldBeFound("startDate.specified=true");

        // Get all the courseList where startDate is null
        defaultCourseShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdAt equals to DEFAULT_CREATED_AT
        defaultCourseShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the courseList where createdAt equals to UPDATED_CREATED_AT
        defaultCourseShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCoursesByCreatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdAt not equals to DEFAULT_CREATED_AT
        defaultCourseShouldNotBeFound("createdAt.notEquals=" + DEFAULT_CREATED_AT);

        // Get all the courseList where createdAt not equals to UPDATED_CREATED_AT
        defaultCourseShouldBeFound("createdAt.notEquals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCoursesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultCourseShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the courseList where createdAt equals to UPDATED_CREATED_AT
        defaultCourseShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllCoursesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where createdAt is not null
        defaultCourseShouldBeFound("createdAt.specified=true");

        // Get all the courseList where createdAt is null
        defaultCourseShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where updatedAt equals to DEFAULT_UPDATED_AT
        defaultCourseShouldBeFound("updatedAt.equals=" + DEFAULT_UPDATED_AT);

        // Get all the courseList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCourseShouldNotBeFound("updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCoursesByUpdatedAtIsNotEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where updatedAt not equals to DEFAULT_UPDATED_AT
        defaultCourseShouldNotBeFound("updatedAt.notEquals=" + DEFAULT_UPDATED_AT);

        // Get all the courseList where updatedAt not equals to UPDATED_UPDATED_AT
        defaultCourseShouldBeFound("updatedAt.notEquals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCoursesByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where updatedAt in DEFAULT_UPDATED_AT or UPDATED_UPDATED_AT
        defaultCourseShouldBeFound("updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT);

        // Get all the courseList where updatedAt equals to UPDATED_UPDATED_AT
        defaultCourseShouldNotBeFound("updatedAt.in=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    public void getAllCoursesByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where updatedAt is not null
        defaultCourseShouldBeFound("updatedAt.specified=true");

        // Get all the courseList where updatedAt is null
        defaultCourseShouldNotBeFound("updatedAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesBySmeIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Profile sme = ProfileResourceIT.createEntity(em);
        em.persist(sme);
        em.flush();
        course.addSme(sme);
        courseRepository.saveAndFlush(course);
        Long smeId = sme.getId();

        // Get all the courseList where sme equals to smeId
        defaultCourseShouldBeFound("smeId.equals=" + smeId);

        // Get all the courseList where sme equals to smeId + 1
        defaultCourseShouldNotBeFound("smeId.equals=" + (smeId + 1));
    }


    @Test
    @Transactional
    public void getAllCoursesByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        Company company = CompanyResourceIT.createEntity(em);
        em.persist(company);
        em.flush();
        course.setCompany(company);
        courseRepository.saveAndFlush(course);
        Long companyId = company.getId();

        // Get all the courseList where company equals to companyId
        defaultCourseShouldBeFound("companyId.equals=" + companyId);

        // Get all the courseList where company equals to companyId + 1
        defaultCourseShouldNotBeFound("companyId.equals=" + (companyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCourseShouldBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].durationInDays").value(hasItem(DEFAULT_DURATION_IN_DAYS.doubleValue())))
            .andExpect(jsonPath("$.[*].hoursPerDay").value(hasItem(DEFAULT_HOURS_PER_DAY.doubleValue())))
            .andExpect(jsonPath("$.[*].surveyLink").value(hasItem(DEFAULT_SURVEY_LINK)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCourseShouldNotBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCourseMockMvc.perform(get("/api/courses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .name(UPDATED_NAME)
            .durationInDays(UPDATED_DURATION_IN_DAYS)
            .hoursPerDay(UPDATED_HOURS_PER_DAY)
            .surveyLink(UPDATED_SURVEY_LINK)
            .tags(UPDATED_TAGS)
            .city(UPDATED_CITY)
            .location(UPDATED_LOCATION)
            .startDate(UPDATED_START_DATE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        CourseDTO courseDTO = courseMapper.toDto(updatedCourse);

        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getDurationInDays()).isEqualTo(UPDATED_DURATION_IN_DAYS);
        assertThat(testCourse.getHoursPerDay()).isEqualTo(UPDATED_HOURS_PER_DAY);
        assertThat(testCourse.getSurveyLink()).isEqualTo(UPDATED_SURVEY_LINK);
        assertThat(testCourse.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testCourse.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCourse.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testCourse.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCourse.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCourse.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the Course in Elasticsearch
        verify(mockCourseSearchRepository, times(1)).save(testCourse);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course
        CourseDTO courseDTO = courseMapper.toDto(course);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Course in Elasticsearch
        verify(mockCourseSearchRepository, times(0)).save(course);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Delete the course
        restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Course in Elasticsearch
        verify(mockCourseSearchRepository, times(1)).deleteById(course.getId());
    }

    @Test
    @Transactional
    public void searchCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);
        when(mockCourseSearchRepository.search(queryStringQuery("id:" + course.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(course), PageRequest.of(0, 1), 1));
        // Search the course
        restCourseMockMvc.perform(get("/api/_search/courses?query=id:" + course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].durationInDays").value(hasItem(DEFAULT_DURATION_IN_DAYS.doubleValue())))
            .andExpect(jsonPath("$.[*].hoursPerDay").value(hasItem(DEFAULT_HOURS_PER_DAY.doubleValue())))
            .andExpect(jsonPath("$.[*].surveyLink").value(hasItem(DEFAULT_SURVEY_LINK)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }
}

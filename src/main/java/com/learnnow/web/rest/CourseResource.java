package com.learnnow.web.rest;

import com.learnnow.service.CourseService;
import com.learnnow.service.UserService;
import com.learnnow.web.rest.errors.BadRequestAlertException;
import com.learnnow.service.dto.CourseDTO;
import com.learnnow.service.dto.UserDTO;
import com.learnnow.service.dto.CourseCriteria;
import com.learnnow.service.CourseQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.learnnow.domain.Course}.
 */
@RestController
@RequestMapping("/api")
public class CourseResource {

    private final Logger log = LoggerFactory.getLogger(CourseResource.class);

    private static final String ENTITY_NAME = "course";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseService courseService;

    private final CourseQueryService courseQueryService;

    private final UserService userService;

    public CourseResource(CourseService courseService, CourseQueryService courseQueryService, UserService userService) {
        this.courseService = courseService;
        this.courseQueryService = courseQueryService;
        this.userService = userService;
    }

    /**
     * {@code POST  /courses} : Create a new course.
     *
     * @param courseDTO the courseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseDTO, or with status {@code 400 (Bad Request)} if the course has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO) throws URISyntaxException {
        log.debug("REST request to save Course : {}", courseDTO);
        if (courseDTO.getId() != null) {
            throw new BadRequestAlertException("A new course cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CourseDTO result = courseService.save(courseDTO);
        return ResponseEntity.created(new URI("/api/courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /courses} : Updates an existing course.
     *
     * @param courseDTO the courseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseDTO,
     * or with status {@code 400 (Bad Request)} if the courseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/courses")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<CourseDTO> updateCourse(@Valid @RequestBody CourseDTO courseDTO) throws URISyntaxException {
        log.debug("REST request to update Course : {}", courseDTO);
        if (courseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CourseDTO result = courseService.save(courseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, courseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /courses} : get all the courses.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courses in body.
     */
    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getAllCourses(CourseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Courses by criteria: {}", criteria);
        Page<CourseDTO> page = courseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /courses/count} : count all the courses.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/courses/count")
    public ResponseEntity<Long> countCourses(CourseCriteria criteria) {
        log.debug("REST request to count Courses by criteria: {}", criteria);
        return ResponseEntity.ok().body(courseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /courses/:id} : get the "id" course.
     *
     * @param id the id of the courseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDTO> getCourse(@PathVariable Long id) {
        log.debug("REST request to get Course : {}", id);
        Optional<CourseDTO> courseDTO = courseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseDTO);
    }

    /**
     * {@code DELETE  /courses/:id} : delete the "id" course.
     *
     * @param id the id of the courseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/courses/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        log.debug("REST request to delete Course : {}", id);
        courseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/courses?query=:query} : search for the course corresponding
     * to the query.
     *
     * @param query the query of the course search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/courses")
    public ResponseEntity<List<CourseDTO>> searchCourses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Courses for query {}", query);
        Page<CourseDTO> page = courseService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code PUT  /courses} : Updates an existing course.
     *
     * @param courseDTO the courseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseDTO,
     * or with status {@code 400 (Bad Request)} if the courseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/courses/{id}/addSme/{login}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<CourseDTO> addSmeToCourse(@PathVariable Long id, @PathVariable String login) throws URISyntaxException {
        log.debug("REST request to add SME {} to Course {}", login, id);
        Optional<CourseDTO> courseDTO = courseService.findOne(id);
        courseDTO.ifPresent(course -> {
            Optional<com.learnnow.domain.User> user = userService.findOne(login); 
            user.ifPresent(sme -> {
                course.getSmes().add(new UserDTO(sme));
                courseService.save(course);
            });
        });
        return ResponseUtil.wrapOrNotFound(courseDTO);
    }
}

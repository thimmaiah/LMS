package com.learnnow.web.rest;

import com.learnnow.service.AttendenceService;
import com.learnnow.service.UserService;
import com.learnnow.web.rest.errors.BadRequestAlertException;
import com.learnnow.service.dto.AttendenceDTO;
import com.learnnow.service.mapper.AttendenceMapper;
import com.learnnow.service.dto.AttendenceCriteria;
import com.learnnow.domain.Attendence;
import com.learnnow.domain.User;
import com.learnnow.security.AuthoritiesConstants;
import com.learnnow.security.SecurityUtils;
import com.learnnow.service.AttendenceQueryService;

import io.github.jhipster.service.filter.LongFilter;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.learnnow.domain.Attendence}.
 */
@RestController
@RequestMapping("/api")
public class AttendenceResource {

    private final Logger log = LoggerFactory.getLogger(AttendenceResource.class);

    private static final String ENTITY_NAME = "attendence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttendenceService attendenceService;

    private final AttendenceQueryService attendenceQueryService;

    private final UserService userService;

    private final AttendenceMapper attendenceMapper;

    public AttendenceResource(AttendenceService attendenceService, 
        AttendenceQueryService attendenceQueryService,
        AttendenceMapper attendenceMapper,
        UserService userService) {
        this.attendenceService = attendenceService;
        this.attendenceQueryService = attendenceQueryService;
        this.userService = userService;
        this.attendenceMapper = attendenceMapper;
    }

    /**
     * {@code POST  /attendences} : Create a new attendence.
     *
     * @param attendenceDTO the attendenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attendenceDTO, or with status {@code 400 (Bad Request)} if the attendence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attendences")
    public ResponseEntity<AttendenceDTO> createAttendence(@Valid @RequestBody AttendenceDTO attendenceDTO) throws URISyntaxException {
        log.debug("REST request to save Attendence : {}", attendenceDTO);
        if (attendenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new attendence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AttendenceDTO result = attendenceService.save(attendenceDTO);
        return ResponseEntity.created(new URI("/api/attendences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /attendences} : Updates an existing attendence.
     *
     * @param attendenceDTO the attendenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attendenceDTO,
     * or with status {@code 400 (Bad Request)} if the attendenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attendenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attendences")
    public ResponseEntity<AttendenceDTO> updateAttendence(@Valid @RequestBody AttendenceDTO attendenceDTO) throws URISyntaxException {
        log.debug("REST request to update Attendence : {}", attendenceDTO);
        if (attendenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        Attendence attendence = attendenceMapper.toEntity(attendenceDTO);
        
        if  (attendence.getUser() != null &&
            !attendence.getUser().getLogin().equals(SecurityUtils.getCurrentUserLogin().orElse(""))) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        AttendenceDTO result = attendenceService.save(attendenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, attendenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /attendences} : get all the attendences.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attendences in body.
     */
    @GetMapping("/attendences")
    public ResponseEntity<List<AttendenceDTO>> getAllAttendences(AttendenceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Attendences by criteria: {}", criteria);
        Page<AttendenceDTO> page;
        page = attendenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /attendences/count} : count all the attendences.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/attendences/count")
    public ResponseEntity<Long> countAttendences(AttendenceCriteria criteria) {
        log.debug("REST request to count Attendences by criteria: {}", criteria);
        return ResponseEntity.ok().body(attendenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /attendences/:id} : get the "id" attendence.
     *
     * @param id the id of the attendenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attendenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attendences/{id}")
    public ResponseEntity<AttendenceDTO> getAttendence(@PathVariable Long id) {
        log.debug("REST request to get Attendence : {}", id);
        Optional<AttendenceDTO> attendenceDTO = attendenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(attendenceDTO);
    }

    /**
     * {@code DELETE  /attendences/:id} : delete the "id" attendence.
     *
     * @param id the id of the attendenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attendences/{id}")
    public ResponseEntity<Void> deleteAttendence(@PathVariable Long id) {
        log.debug("REST request to delete Attendence : {}", id);
        attendenceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/attendences?query=:query} : search for the attendence corresponding
     * to the query.
     *
     * @param query the query of the attendence search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/attendences")
    public ResponseEntity<List<AttendenceDTO>> searchAttendences(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Attendences for query {}", query);
        Page<AttendenceDTO> page = attendenceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}

package com.learnnow.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.learnnow.domain.Attendence;
import com.learnnow.domain.*; // for static metamodels
import com.learnnow.repository.AttendenceRepository;
import com.learnnow.repository.search.AttendenceSearchRepository;
import com.learnnow.service.dto.AttendenceCriteria;
import com.learnnow.service.dto.AttendenceDTO;
import com.learnnow.service.mapper.AttendenceMapper;

/**
 * Service for executing complex queries for {@link Attendence} entities in the database.
 * The main input is a {@link AttendenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AttendenceDTO} or a {@link Page} of {@link AttendenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AttendenceQueryService extends QueryService<Attendence> {

    private final Logger log = LoggerFactory.getLogger(AttendenceQueryService.class);

    private final AttendenceRepository attendenceRepository;

    private final AttendenceMapper attendenceMapper;

    private final AttendenceSearchRepository attendenceSearchRepository;

    public AttendenceQueryService(AttendenceRepository attendenceRepository, AttendenceMapper attendenceMapper, AttendenceSearchRepository attendenceSearchRepository) {
        this.attendenceRepository = attendenceRepository;
        this.attendenceMapper = attendenceMapper;
        this.attendenceSearchRepository = attendenceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AttendenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AttendenceDTO> findByCriteria(AttendenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Attendence> specification = createSpecification(criteria);
        return attendenceMapper.toDto(attendenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AttendenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AttendenceDTO> findByCriteria(AttendenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Attendence> specification = createSpecification(criteria);
        return attendenceRepository.findAll(specification, page)
            .map(attendenceMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<AttendenceDTO> findByCurrentUser(AttendenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Attendence> specification = createSpecification(criteria);
        return attendenceRepository.findByUserIsCurrentUser(specification, page)
            .map(attendenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AttendenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Attendence> specification = createSpecification(criteria);
        return attendenceRepository.count(specification);
    }

    /**
     * Function to convert {@link AttendenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Attendence> createSpecification(AttendenceCriteria criteria) {
        Specification<Attendence> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Attendence_.id));
            }
            if (criteria.getAttendended() != null) {
                specification = specification.and(buildSpecification(criteria.getAttendended(), Attendence_.attendended));
            }
            if (criteria.getDay() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDay(), Attendence_.day));
            }
            if (criteria.getRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRating(), Attendence_.rating));
            }
            if (criteria.getComments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComments(), Attendence_.comments));
            }
            if (criteria.getCourseId() != null) {
                specification = specification.and(buildSpecification(criteria.getCourseId(),
                    root -> root.join(Attendence_.course, JoinType.LEFT).get(Course_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Attendence_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}

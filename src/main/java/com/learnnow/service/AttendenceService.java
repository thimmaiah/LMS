package com.learnnow.service;

import com.learnnow.domain.Attendence;
import com.learnnow.repository.AttendenceRepository;
import com.learnnow.repository.search.AttendenceSearchRepository;
import com.learnnow.service.dto.AttendenceDTO;
import com.learnnow.service.mapper.AttendenceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Attendence}.
 */
@Service
@Transactional
public class AttendenceService {

    private final Logger log = LoggerFactory.getLogger(AttendenceService.class);

    private final AttendenceRepository attendenceRepository;

    private final AttendenceMapper attendenceMapper;

    private final AttendenceSearchRepository attendenceSearchRepository;

    public AttendenceService(AttendenceRepository attendenceRepository, AttendenceMapper attendenceMapper, AttendenceSearchRepository attendenceSearchRepository) {
        this.attendenceRepository = attendenceRepository;
        this.attendenceMapper = attendenceMapper;
        this.attendenceSearchRepository = attendenceSearchRepository;
    }

    /**
     * Save a attendence.
     *
     * @param attendenceDTO the entity to save.
     * @return the persisted entity.
     */
    public AttendenceDTO save(AttendenceDTO attendenceDTO) {
        log.debug("Request to save Attendence : {}", attendenceDTO);
        Attendence attendence = attendenceMapper.toEntity(attendenceDTO);
        attendence = attendenceRepository.save(attendence);
        AttendenceDTO result = attendenceMapper.toDto(attendence);
        attendenceSearchRepository.save(attendence);
        return result;
    }

    /**
     * Get all the attendences.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AttendenceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Attendences");
        return attendenceRepository.findAll(pageable)
            .map(attendenceMapper::toDto);
    }


    /**
     * Get one attendence by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AttendenceDTO> findOne(Long id) {
        log.debug("Request to get Attendence : {}", id);
        return attendenceRepository.findById(id)
            .map(attendenceMapper::toDto);
    }

    /**
     * Delete the attendence by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Attendence : {}", id);
        attendenceRepository.deleteById(id);
        attendenceSearchRepository.deleteById(id);
    }

    /**
     * Search for the attendence corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AttendenceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Attendences for query {}", query);
        return attendenceSearchRepository.search(queryStringQuery(query), pageable)
            .map(attendenceMapper::toDto);
    }
}

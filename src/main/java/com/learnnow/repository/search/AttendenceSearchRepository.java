package com.learnnow.repository.search;
import com.learnnow.domain.Attendence;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Attendence} entity.
 */
public interface AttendenceSearchRepository extends ElasticsearchRepository<Attendence, Long> {
}

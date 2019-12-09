package com.learnnow.repository.search;
import com.learnnow.domain.Profile;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Profile} entity.
 */
public interface ProfileSearchRepository extends ElasticsearchRepository<Profile, Long> {
}

package com.learnnow.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CourseSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CourseSearchRepositoryMockConfiguration {

    @MockBean
    private CourseSearchRepository mockCourseSearchRepository;

}

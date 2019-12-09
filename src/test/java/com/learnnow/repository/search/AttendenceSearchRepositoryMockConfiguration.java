package com.learnnow.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AttendenceSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AttendenceSearchRepositoryMockConfiguration {

    @MockBean
    private AttendenceSearchRepository mockAttendenceSearchRepository;

}

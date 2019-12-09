package com.learnnow.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;


public class AttendenceMapperTest {

    private AttendenceMapper attendenceMapper;

    @BeforeEach
    public void setUp() {
        attendenceMapper = new AttendenceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 2L;
        assertThat(attendenceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(attendenceMapper.fromId(null)).isNull();
    }
}

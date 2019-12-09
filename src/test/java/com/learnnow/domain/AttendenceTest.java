package com.learnnow.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.learnnow.web.rest.TestUtil;

public class AttendenceTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attendence.class);
        Attendence attendence1 = new Attendence();
        attendence1.setId(1L);
        Attendence attendence2 = new Attendence();
        attendence2.setId(attendence1.getId());
        assertThat(attendence1).isEqualTo(attendence2);
        attendence2.setId(2L);
        assertThat(attendence1).isNotEqualTo(attendence2);
        attendence1.setId(null);
        assertThat(attendence1).isNotEqualTo(attendence2);
    }
}

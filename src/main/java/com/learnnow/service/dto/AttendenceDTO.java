package com.learnnow.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.learnnow.domain.Attendence} entity.
 */
public class AttendenceDTO implements Serializable {

    private Long id;

    private Boolean attendended;

    @Min(value = 1)
    @Max(value = 20)
    private Integer day;

    @Min(value = 0)
    @Max(value = 5)
    private Integer rating;

    private String comments;


    private Long courseId;

    private String courseName;

    private Long userId;

    private String userLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAttendended() {
        return attendended;
    }

    public void setAttendended(Boolean attendended) {
        this.attendended = attendended;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AttendenceDTO attendenceDTO = (AttendenceDTO) o;
        if (attendenceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), attendenceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AttendenceDTO{" +
            "id=" + getId() +
            ", attendended='" + isAttendended() + "'" +
            ", day=" + getDay() +
            ", rating=" + getRating() +
            ", comments='" + getComments() + "'" +
            ", course=" + getCourseId() +
            ", course='" + getCourseName() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            "}";
    }
}

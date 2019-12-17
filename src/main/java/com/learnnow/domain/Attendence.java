package com.learnnow.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

/**
 * A Attendence.
 */
@Entity
@Table(name = "attendence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "attendence")
public class Attendence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "attendended")
    private Boolean attendended;

    @Min(value = 1)
    @Max(value = 20)
    @Column(name = "day")
    private Integer day;

    @Min(value = 0)
    @Max(value = 5)
    @Column(name = "rating")
    private Integer rating;

    @Column(name = "comments")
    private String comments;

    @ManyToOne
    @JsonIgnore
    private Course course;

    @ManyToOne
    @JsonIgnore
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAttendended() {
        return attendended;
    }

    public Attendence attendended(Boolean attendended) {
        this.attendended = attendended;
        return this;
    }

    public void setAttendended(Boolean attendended) {
        this.attendended = attendended;
    }

    public Integer getDay() {
        return day;
    }

    public Attendence day(Integer day) {
        this.day = day;
        return this;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getRating() {
        return rating;
    }

    public Attendence rating(Integer rating) {
        this.rating = rating;
        return this;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public Attendence comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Course getCourse() {
        return course;
    }

    public Attendence course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public Attendence user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attendence)) {
            return false;
        }
        return id != null && id.equals(((Attendence) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Attendence{" +
            "id=" + getId() +
            ", attendended='" + isAttendended() + "'" +
            ", day=" + getDay() +
            ", rating=" + getRating() +
            ", comments='" + getComments() + "'" +
            "}";
    }
}

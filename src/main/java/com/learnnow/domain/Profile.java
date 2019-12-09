package com.learnnow.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.learnnow.domain.enumeration.SmeLevel;

/**
 * A Profile.
 */
@Entity
@Table(name = "profile")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "profile")
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Min(value = 0)
    @Column(name = "points")
    private Integer points;

    @Enumerated(EnumType.STRING)
    @Column(name = "sme_level")
    private SmeLevel smeLevel;

    @NotNull
    @Column(name = "skills", nullable = false)
    private String skills;

    @Column(name = "expert_in")
    private String expertIn;

    @Column(name = "shadowing_in")
    private String shadowingIn;

    @Column(name = "city")
    private String city;

    @Column(name = "location")
    private String location;

    @OneToOne
    @JoinColumn(unique = true)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("profiles")
    @JsonIgnore
    private Company company;

    @ManyToMany(mappedBy = "smes")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Course> courses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public Profile points(Integer points) {
        this.points = points;
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public SmeLevel getSmeLevel() {
        return smeLevel;
    }

    public Profile smeLevel(SmeLevel smeLevel) {
        this.smeLevel = smeLevel;
        return this;
    }

    public void setSmeLevel(SmeLevel smeLevel) {
        this.smeLevel = smeLevel;
    }

    public String getSkills() {
        return skills;
    }

    public Profile skills(String skills) {
        this.skills = skills;
        return this;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getExpertIn() {
        return expertIn;
    }

    public Profile expertIn(String expertIn) {
        this.expertIn = expertIn;
        return this;
    }

    public void setExpertIn(String expertIn) {
        this.expertIn = expertIn;
    }

    public String getShadowingIn() {
        return shadowingIn;
    }

    public Profile shadowingIn(String shadowingIn) {
        this.shadowingIn = shadowingIn;
        return this;
    }

    public void setShadowingIn(String shadowingIn) {
        this.shadowingIn = shadowingIn;
    }

    public String getCity() {
        return city;
    }

    public Profile city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public Profile location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

    public Profile user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Company getCompany() {
        return company;
    }

    public Profile company(Company company) {
        this.company = company;
        return this;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public Profile courses(Set<Course> courses) {
        this.courses = courses;
        return this;
    }

    public Profile addCourse(Course course) {
        this.courses.add(course);
        course.getSmes().add(this);
        return this;
    }

    public Profile removeCourse(Course course) {
        this.courses.remove(course);
        course.getSmes().remove(this);
        return this;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Profile)) {
            return false;
        }
        return id != null && id.equals(((Profile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Profile{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", smeLevel='" + getSmeLevel() + "'" +
            ", skills='" + getSkills() + "'" +
            ", expertIn='" + getExpertIn() + "'" +
            ", shadowingIn='" + getShadowingIn() + "'" +
            ", city='" + getCity() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}

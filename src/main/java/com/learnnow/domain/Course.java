package com.learnnow.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "course")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    @Column(name = "duration_in_days", nullable = false)
    private Float durationInDays;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "24")
    @Column(name = "hours_per_day", nullable = false)
    private Float hoursPerDay;

    @Column(name = "survey_link")
    private String surveyLink;

    @Column(name = "tags")
    private String tags;

    @Column(name = "city")
    private String city;

    @Column(name = "location")
    private String location;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Lob
    @Column(name = "pre_requisites")
    private String preRequisites;

    @Lob
    @Column(name = "objectives")
    private String objectives;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "course_sme",
               joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "sme_id", referencedColumnName = "id"))
    private Set<User> smes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Course name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getDurationInDays() {
        return durationInDays;
    }

    public Course durationInDays(Float durationInDays) {
        this.durationInDays = durationInDays;
        return this;
    }

    public void setDurationInDays(Float durationInDays) {
        this.durationInDays = durationInDays;
    }

    public Float getHoursPerDay() {
        return hoursPerDay;
    }

    public Course hoursPerDay(Float hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
        return this;
    }

    public void setHoursPerDay(Float hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public String getSurveyLink() {
        return surveyLink;
    }

    public Course surveyLink(String surveyLink) {
        this.surveyLink = surveyLink;
        return this;
    }

    public void setSurveyLink(String surveyLink) {
        this.surveyLink = surveyLink;
    }

    public String getTags() {
        return tags;
    }

    public Course tags(String tags) {
        this.tags = tags;
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCity() {
        return city;
    }

    public Course city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public Course location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Course startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Course createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Course updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPreRequisites() {
        return preRequisites;
    }

    public Course preRequisites(String preRequisites) {
        this.preRequisites = preRequisites;
        return this;
    }

    public void setPreRequisites(String preRequisites) {
        this.preRequisites = preRequisites;
    }

    public String getObjectives() {
        return objectives;
    }

    public Course objectives(String objectives) {
        this.objectives = objectives;
        return this;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public Set<User> getSmes() {
        return smes;
    }

    public Course smes(Set<User> users) {
        this.smes = users;
        return this;
    }

    public Course addSme(User user) {
        this.smes.add(user);
        return this;
    }

    public Course removeSme(User user) {
        this.smes.remove(user);
        return this;
    }

    public void setSmes(Set<User> users) {
        this.smes = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", durationInDays=" + getDurationInDays() +
            ", hoursPerDay=" + getHoursPerDay() +
            ", surveyLink='" + getSurveyLink() + "'" +
            ", tags='" + getTags() + "'" +
            ", city='" + getCity() + "'" +
            ", location='" + getLocation() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", preRequisites='" + getPreRequisites() + "'" +
            ", objectives='" + getObjectives() + "'" +
            "}";
    }
}

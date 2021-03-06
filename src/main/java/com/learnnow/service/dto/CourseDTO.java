package com.learnnow.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.learnnow.domain.Course} entity.
 */
public class CourseDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "20")
    private Float durationInDays;

    @NotNull
    @DecimalMin(value = "0")
    @DecimalMax(value = "24")
    private Float hoursPerDay;

    private String surveyLink;

    private String tags;

    private String city;

    private String location;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant createdAt;

    @NotNull
    private Instant updatedAt;

    @Lob
    private String preRequisites;

    @Lob
    private String objectives;


    private Set<UserDTO> smes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(Float durationInDays) {
        this.durationInDays = durationInDays;
    }

    public Float getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(Float hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public String getSurveyLink() {
        return surveyLink;
    }

    public void setSurveyLink(String surveyLink) {
        this.surveyLink = surveyLink;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPreRequisites() {
        return preRequisites;
    }

    public void setPreRequisites(String preRequisites) {
        this.preRequisites = preRequisites;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public Set<UserDTO> getSmes() {
        return smes;
    }

    public void setSmes(Set<UserDTO> users) {
        this.smes = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CourseDTO courseDTO = (CourseDTO) o;
        if (courseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), courseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CourseDTO{" +
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

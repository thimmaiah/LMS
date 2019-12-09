package com.learnnow.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.learnnow.domain.Course} entity. This class is used
 * in {@link com.learnnow.web.rest.CourseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /courses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CourseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private FloatFilter durationInDays;

    private FloatFilter hoursPerDay;

    private StringFilter surveyLink;

    private StringFilter tags;

    private StringFilter city;

    private StringFilter location;

    private InstantFilter startDate;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter smeId;

    private LongFilter companyId;

    public CourseCriteria(){
    }

    public CourseCriteria(CourseCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.durationInDays = other.durationInDays == null ? null : other.durationInDays.copy();
        this.hoursPerDay = other.hoursPerDay == null ? null : other.hoursPerDay.copy();
        this.surveyLink = other.surveyLink == null ? null : other.surveyLink.copy();
        this.tags = other.tags == null ? null : other.tags.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.smeId = other.smeId == null ? null : other.smeId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
    }

    @Override
    public CourseCriteria copy() {
        return new CourseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public FloatFilter getDurationInDays() {
        return durationInDays;
    }

    public void setDurationInDays(FloatFilter durationInDays) {
        this.durationInDays = durationInDays;
    }

    public FloatFilter getHoursPerDay() {
        return hoursPerDay;
    }

    public void setHoursPerDay(FloatFilter hoursPerDay) {
        this.hoursPerDay = hoursPerDay;
    }

    public StringFilter getSurveyLink() {
        return surveyLink;
    }

    public void setSurveyLink(StringFilter surveyLink) {
        this.surveyLink = surveyLink;
    }

    public StringFilter getTags() {
        return tags;
    }

    public void setTags(StringFilter tags) {
        this.tags = tags;
    }

    public StringFilter getCity() {
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getSmeId() {
        return smeId;
    }

    public void setSmeId(LongFilter smeId) {
        this.smeId = smeId;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CourseCriteria that = (CourseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(durationInDays, that.durationInDays) &&
            Objects.equals(hoursPerDay, that.hoursPerDay) &&
            Objects.equals(surveyLink, that.surveyLink) &&
            Objects.equals(tags, that.tags) &&
            Objects.equals(city, that.city) &&
            Objects.equals(location, that.location) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(smeId, that.smeId) &&
            Objects.equals(companyId, that.companyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        durationInDays,
        hoursPerDay,
        surveyLink,
        tags,
        city,
        location,
        startDate,
        createdAt,
        updatedAt,
        smeId,
        companyId
        );
    }

    @Override
    public String toString() {
        return "CourseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (durationInDays != null ? "durationInDays=" + durationInDays + ", " : "") +
                (hoursPerDay != null ? "hoursPerDay=" + hoursPerDay + ", " : "") +
                (surveyLink != null ? "surveyLink=" + surveyLink + ", " : "") +
                (tags != null ? "tags=" + tags + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (smeId != null ? "smeId=" + smeId + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
            "}";
    }

}

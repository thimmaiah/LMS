package com.learnnow.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.learnnow.domain.enumeration.SmeLevel;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.learnnow.domain.Profile} entity. This class is used
 * in {@link com.learnnow.web.rest.ProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProfileCriteria implements Serializable, Criteria {
    /**
     * Class for filtering SmeLevel
     */
    public static class SmeLevelFilter extends Filter<SmeLevel> {

        public SmeLevelFilter() {
        }

        public SmeLevelFilter(SmeLevelFilter filter) {
            super(filter);
        }

        @Override
        public SmeLevelFilter copy() {
            return new SmeLevelFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter points;

    private SmeLevelFilter smeLevel;

    private StringFilter skills;

    private StringFilter expertIn;

    private StringFilter shadowingIn;

    private StringFilter city;

    private StringFilter location;

    private LongFilter userId;

    private LongFilter companyId;

    private LongFilter courseId;

    public ProfileCriteria(){
    }

    public ProfileCriteria(ProfileCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.points = other.points == null ? null : other.points.copy();
        this.smeLevel = other.smeLevel == null ? null : other.smeLevel.copy();
        this.skills = other.skills == null ? null : other.skills.copy();
        this.expertIn = other.expertIn == null ? null : other.expertIn.copy();
        this.shadowingIn = other.shadowingIn == null ? null : other.shadowingIn.copy();
        this.city = other.city == null ? null : other.city.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.companyId = other.companyId == null ? null : other.companyId.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
    }

    @Override
    public ProfileCriteria copy() {
        return new ProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getPoints() {
        return points;
    }

    public void setPoints(IntegerFilter points) {
        this.points = points;
    }

    public SmeLevelFilter getSmeLevel() {
        return smeLevel;
    }

    public void setSmeLevel(SmeLevelFilter smeLevel) {
        this.smeLevel = smeLevel;
    }

    public StringFilter getSkills() {
        return skills;
    }

    public void setSkills(StringFilter skills) {
        this.skills = skills;
    }

    public StringFilter getExpertIn() {
        return expertIn;
    }

    public void setExpertIn(StringFilter expertIn) {
        this.expertIn = expertIn;
    }

    public StringFilter getShadowingIn() {
        return shadowingIn;
    }

    public void setShadowingIn(StringFilter shadowingIn) {
        this.shadowingIn = shadowingIn;
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

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCompanyId() {
        return companyId;
    }

    public void setCompanyId(LongFilter companyId) {
        this.companyId = companyId;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProfileCriteria that = (ProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(points, that.points) &&
            Objects.equals(smeLevel, that.smeLevel) &&
            Objects.equals(skills, that.skills) &&
            Objects.equals(expertIn, that.expertIn) &&
            Objects.equals(shadowingIn, that.shadowingIn) &&
            Objects.equals(city, that.city) &&
            Objects.equals(location, that.location) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(companyId, that.companyId) &&
            Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        points,
        smeLevel,
        skills,
        expertIn,
        shadowingIn,
        city,
        location,
        userId,
        companyId,
        courseId
        );
    }

    @Override
    public String toString() {
        return "ProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (points != null ? "points=" + points + ", " : "") +
                (smeLevel != null ? "smeLevel=" + smeLevel + ", " : "") +
                (skills != null ? "skills=" + skills + ", " : "") +
                (expertIn != null ? "expertIn=" + expertIn + ", " : "") +
                (shadowingIn != null ? "shadowingIn=" + shadowingIn + ", " : "") +
                (city != null ? "city=" + city + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (companyId != null ? "companyId=" + companyId + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
            "}";
    }

}

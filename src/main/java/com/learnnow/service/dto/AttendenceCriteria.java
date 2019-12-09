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

/**
 * Criteria class for the {@link com.learnnow.domain.Attendence} entity. This class is used
 * in {@link com.learnnow.web.rest.AttendenceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /attendences?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AttendenceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter attendended;

    private IntegerFilter day;

    private IntegerFilter rating;

    private StringFilter comments;

    private LongFilter courseId;

    private LongFilter profileId;

    public AttendenceCriteria(){
    }

    public AttendenceCriteria(AttendenceCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.attendended = other.attendended == null ? null : other.attendended.copy();
        this.day = other.day == null ? null : other.day.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.comments = other.comments == null ? null : other.comments.copy();
        this.courseId = other.courseId == null ? null : other.courseId.copy();
        this.profileId = other.profileId == null ? null : other.profileId.copy();
    }

    @Override
    public AttendenceCriteria copy() {
        return new AttendenceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getAttendended() {
        return attendended;
    }

    public void setAttendended(BooleanFilter attendended) {
        this.attendended = attendended;
    }

    public IntegerFilter getDay() {
        return day;
    }

    public void setDay(IntegerFilter day) {
        this.day = day;
    }

    public IntegerFilter getRating() {
        return rating;
    }

    public void setRating(IntegerFilter rating) {
        this.rating = rating;
    }

    public StringFilter getComments() {
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public LongFilter getCourseId() {
        return courseId;
    }

    public void setCourseId(LongFilter courseId) {
        this.courseId = courseId;
    }

    public LongFilter getProfileId() {
        return profileId;
    }

    public void setProfileId(LongFilter profileId) {
        this.profileId = profileId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AttendenceCriteria that = (AttendenceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(attendended, that.attendended) &&
            Objects.equals(day, that.day) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(courseId, that.courseId) &&
            Objects.equals(profileId, that.profileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        attendended,
        day,
        rating,
        comments,
        courseId,
        profileId
        );
    }

    @Override
    public String toString() {
        return "AttendenceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (attendended != null ? "attendended=" + attendended + ", " : "") +
                (day != null ? "day=" + day + ", " : "") +
                (rating != null ? "rating=" + rating + ", " : "") +
                (comments != null ? "comments=" + comments + ", " : "") +
                (courseId != null ? "courseId=" + courseId + ", " : "") +
                (profileId != null ? "profileId=" + profileId + ", " : "") +
            "}";
    }

}

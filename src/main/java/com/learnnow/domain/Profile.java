package com.learnnow.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    @JoinColumn(unique = true)
    private User user;

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

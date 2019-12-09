package com.learnnow.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.learnnow.domain.enumeration.SmeLevel;

/**
 * A DTO for the {@link com.learnnow.domain.Profile} entity.
 */
public class ProfileDTO implements Serializable {

    private Long id;

    @Min(value = 0)
    private Integer points;

    private SmeLevel smeLevel;

    @NotNull
    private String skills;

    private String expertIn;

    private String shadowingIn;

    private String city;

    private String location;


    private Long userId;

    private String userLogin;

    private Long companyId;

    private String companyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public SmeLevel getSmeLevel() {
        return smeLevel;
    }

    public void setSmeLevel(SmeLevel smeLevel) {
        this.smeLevel = smeLevel;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public String getExpertIn() {
        return expertIn;
    }

    public void setExpertIn(String expertIn) {
        this.expertIn = expertIn;
    }

    public String getShadowingIn() {
        return shadowingIn;
    }

    public void setShadowingIn(String shadowingIn) {
        this.shadowingIn = shadowingIn;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProfileDTO profileDTO = (ProfileDTO) o;
        if (profileDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), profileDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
            "id=" + getId() +
            ", points=" + getPoints() +
            ", smeLevel='" + getSmeLevel() + "'" +
            ", skills='" + getSkills() + "'" +
            ", expertIn='" + getExpertIn() + "'" +
            ", shadowingIn='" + getShadowingIn() + "'" +
            ", city='" + getCity() + "'" +
            ", location='" + getLocation() + "'" +
            ", user=" + getUserId() +
            ", user='" + getUserLogin() + "'" +
            ", company=" + getCompanyId() +
            ", company='" + getCompanyName() + "'" +
            "}";
    }
}

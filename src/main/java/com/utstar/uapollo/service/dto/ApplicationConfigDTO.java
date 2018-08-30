package com.utstar.uapollo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the ApplicationConfig entity.
 */
public class ApplicationConfigDTO implements Serializable {

    private Long id;

    @NotNull
    @Lob
    private String configContent;

    @NotNull
    private Long createTime;

    @NotNull
    private Long modifyTime;

    private Long applicationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigContent() {
        return configContent;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApplicationConfigDTO applicationConfigDTO = (ApplicationConfigDTO) o;
        if(applicationConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applicationConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplicationConfigDTO{" +
            "id=" + getId() +
            ", configContent='" + getConfigContent() + "'" +
            ", createTime=" + getCreateTime() +
            ", modifyTime=" + getModifyTime() +
            "}";
    }
}

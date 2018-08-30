package com.utstar.uapollo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A DTO for the ApplicationConfigHistory entity.
 */
public class ApplicationConfigHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long applicationId;

    @NotNull
    private Long applicationConfigid;

    @NotNull
    @Lob
    private String configContent;

    @NotNull
    private Operation operation;

    @NotNull
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getApplicationConfigid() {
        return applicationConfigid;
    }

    public void setApplicationConfigid(Long applicationConfigid) {
        this.applicationConfigid = applicationConfigid;
    }

    public String getConfigContent() {
        return configContent;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ApplicationConfigHistoryDTO applicationConfigHistoryDTO = (ApplicationConfigHistoryDTO) o;
        if(applicationConfigHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applicationConfigHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplicationConfigHistoryDTO{" +
            "id=" + getId() +
            ", applicationId=" + getApplicationId() +
            ", applicationConfigid=" + getApplicationConfigid() +
            ", configContent='" + getConfigContent() + "'" +
            ", operation='" + getOperation() + "'" +
            ", createTime=" + getCreateTime() +
            "}";
    }
}

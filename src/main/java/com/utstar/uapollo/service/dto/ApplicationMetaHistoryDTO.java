package com.utstar.uapollo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A DTO for the ApplicationMetaHistory entity.
 */
public class ApplicationMetaHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long projectId;

    @NotNull
    private Long applicationMetaId;

    @NotNull
    @Size(max = 32)
    private String name;

    @NotNull
    @Size(max = 32)
    private String configFile;

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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getApplicationMetaId() {
        return applicationMetaId;
    }

    public void setApplicationMetaId(Long applicationMetaId) {
        this.applicationMetaId = applicationMetaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
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

        ApplicationMetaHistoryDTO applicationMetaHistoryDTO = (ApplicationMetaHistoryDTO) o;
        if(applicationMetaHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applicationMetaHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplicationMetaHistoryDTO{" +
            "id=" + getId() +
            ", projectId=" + getProjectId() +
            ", applicationMetaId=" + getApplicationMetaId() +
            ", name='" + getName() + "'" +
            ", configFile='" + getConfigFile() + "'" +
            ", configContent='" + getConfigContent() + "'" +
            ", operation='" + getOperation() + "'" +
            ", createTime=" + getCreateTime() +
            "}";
    }
}

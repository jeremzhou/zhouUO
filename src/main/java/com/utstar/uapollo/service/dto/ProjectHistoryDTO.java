package com.utstar.uapollo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A DTO for the ProjectHistory entity.
 */
public class ProjectHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long projectId;

    @NotNull
    @Size(max = 32)
    private String name;

    @NotNull
    private Integer userId;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

        ProjectHistoryDTO projectHistoryDTO = (ProjectHistoryDTO) o;
        if(projectHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), projectHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProjectHistoryDTO{" +
            "id=" + getId() +
            ", projectId=" + getProjectId() +
            ", name='" + getName() + "'" +
            ", userId=" + getUserId() +
            ", operation='" + getOperation() + "'" +
            ", createTime=" + getCreateTime() +
            "}";
    }
}

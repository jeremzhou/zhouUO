package com.utstar.uapollo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A DTO for the NodeHistory entity.
 */
public class NodeHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long projectId;

    @NotNull
    private Long nodeId;

    @NotNull
    @Size(max = 32)
    private String name;

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

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        NodeHistoryDTO nodeHistoryDTO = (NodeHistoryDTO) o;
        if(nodeHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), nodeHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NodeHistoryDTO{" +
            "id=" + getId() +
            ", projectId=" + getProjectId() +
            ", nodeId=" + getNodeId() +
            ", name='" + getName() + "'" +
            ", operation='" + getOperation() + "'" +
            ", createTime=" + getCreateTime() +
            "}";
    }
}

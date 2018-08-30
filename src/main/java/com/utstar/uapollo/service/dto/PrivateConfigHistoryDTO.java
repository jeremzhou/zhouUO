package com.utstar.uapollo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A DTO for the PrivateConfigHistory entity.
 */
public class PrivateConfigHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long applicationId;

    @NotNull
    private Long privateConfigId;

    @NotNull
    private String key;

    @NotNull
    private String value;

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

    public Long getPrivateConfigId() {
        return privateConfigId;
    }

    public void setPrivateConfigId(Long privateConfigId) {
        this.privateConfigId = privateConfigId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

        PrivateConfigHistoryDTO privateConfigHistoryDTO = (PrivateConfigHistoryDTO) o;
        if(privateConfigHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), privateConfigHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PrivateConfigHistoryDTO{" +
            "id=" + getId() +
            ", applicationId=" + getApplicationId() +
            ", privateConfigId=" + getPrivateConfigId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", operation='" + getOperation() + "'" +
            ", createTime=" + getCreateTime() +
            "}";
    }
}

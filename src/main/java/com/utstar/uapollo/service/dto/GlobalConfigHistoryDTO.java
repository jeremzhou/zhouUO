package com.utstar.uapollo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A DTO for the GlobalConfigHistory entity.
 */
public class GlobalConfigHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long applicationMetaId;

    @NotNull
    private Long globalConfigId;

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

    public Long getApplicationMetaId() {
        return applicationMetaId;
    }

    public void setApplicationMetaId(Long applicationMetaId) {
        this.applicationMetaId = applicationMetaId;
    }

    public Long getGlobalConfigId() {
        return globalConfigId;
    }

    public void setGlobalConfigId(Long globalConfigId) {
        this.globalConfigId = globalConfigId;
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

        GlobalConfigHistoryDTO globalConfigHistoryDTO = (GlobalConfigHistoryDTO) o;
        if(globalConfigHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), globalConfigHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GlobalConfigHistoryDTO{" +
            "id=" + getId() +
            ", applicationMetaId=" + getApplicationMetaId() +
            ", globalConfigId=" + getGlobalConfigId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", operation='" + getOperation() + "'" +
            ", createTime=" + getCreateTime() +
            "}";
    }
}

package com.utstar.uapollo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the GlobalConfig entity.
 */
public class GlobalConfigDTO implements Serializable {

    private Long id;

    @NotNull
    private String key;

    @NotNull
    private String value;

    @NotNull
    private Long createTime;

    @NotNull
    private Long modifyTime;

    private Long applicationMetaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getApplicationMetaId() {
        return applicationMetaId;
    }

    public void setApplicationMetaId(Long applicationMetaId) {
        this.applicationMetaId = applicationMetaId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GlobalConfigDTO globalConfigDTO = (GlobalConfigDTO) o;
        if(globalConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), globalConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GlobalConfigDTO{" +
            "id=" + getId() +
            ", key='" + getKey() + "'" +
            ", value='" + getValue() + "'" +
            ", createTime=" + getCreateTime() +
            ", modifyTime=" + getModifyTime() +
            "}";
    }
}

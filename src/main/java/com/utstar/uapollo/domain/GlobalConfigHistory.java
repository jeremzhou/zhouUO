package com.utstar.uapollo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A GlobalConfigHistory.
 */
@Entity
@Table(name = "global_config_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GlobalConfigHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "application_meta_id", nullable = false)
    private Long applicationMetaId;

    @NotNull
    @Column(name = "global_config_id", nullable = false)
    private Long globalConfigId;

    @NotNull
    @Column(name = "jhi_key", nullable = false)
    private String key;

    @NotNull
    @Column(name = "jhi_value", nullable = false)
    private String value;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false)
    private Operation operation;

    @NotNull
    @Column(name = "create_time", nullable = false)
    private Long createTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApplicationMetaId() {
        return applicationMetaId;
    }

    public GlobalConfigHistory applicationMetaId(Long applicationMetaId) {
        this.applicationMetaId = applicationMetaId;
        return this;
    }

    public void setApplicationMetaId(Long applicationMetaId) {
        this.applicationMetaId = applicationMetaId;
    }

    public Long getGlobalConfigId() {
        return globalConfigId;
    }

    public GlobalConfigHistory globalConfigId(Long globalConfigId) {
        this.globalConfigId = globalConfigId;
        return this;
    }

    public void setGlobalConfigId(Long globalConfigId) {
        this.globalConfigId = globalConfigId;
    }

    public String getKey() {
        return key;
    }

    public GlobalConfigHistory key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public GlobalConfigHistory value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Operation getOperation() {
        return operation;
    }

    public GlobalConfigHistory operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public GlobalConfigHistory createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GlobalConfigHistory globalConfigHistory = (GlobalConfigHistory) o;
        if (globalConfigHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), globalConfigHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GlobalConfigHistory{" +
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

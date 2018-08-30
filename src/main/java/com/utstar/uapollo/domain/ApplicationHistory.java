package com.utstar.uapollo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A ApplicationHistory.
 */
@Entity
@Table(name = "application_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApplicationHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "application_meta_id", nullable = false)
    private Long applicationMetaId;

    @NotNull
    @Column(name = "server_id", nullable = false)
    private Long serverId;

    @NotNull
    @Column(name = "application_id", nullable = false)
    private Long applicationId;

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

    public ApplicationHistory applicationMetaId(Long applicationMetaId) {
        this.applicationMetaId = applicationMetaId;
        return this;
    }

    public void setApplicationMetaId(Long applicationMetaId) {
        this.applicationMetaId = applicationMetaId;
    }

    public Long getServerId() {
        return serverId;
    }

    public ApplicationHistory serverId(Long serverId) {
        this.serverId = serverId;
        return this;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public ApplicationHistory applicationId(Long applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Operation getOperation() {
        return operation;
    }

    public ApplicationHistory operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ApplicationHistory createTime(Long createTime) {
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
        ApplicationHistory applicationHistory = (ApplicationHistory) o;
        if (applicationHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applicationHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplicationHistory{" +
            "id=" + getId() +
            ", applicationMetaId=" + getApplicationMetaId() +
            ", serverId=" + getServerId() +
            ", applicationId=" + getApplicationId() +
            ", operation='" + getOperation() + "'" +
            ", createTime=" + getCreateTime() +
            "}";
    }
}

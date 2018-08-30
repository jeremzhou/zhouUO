package com.utstar.uapollo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A ApplicationConfigHistory.
 */
@Entity
@Table(name = "application_config_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApplicationConfigHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "application_id", nullable = false)
    private Long applicationId;

    @NotNull
    @Column(name = "application_configid", nullable = false)
    private Long applicationConfigid;

    @NotNull
    @Lob
    @Column(name = "config_content", nullable = false)
    private String configContent;

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

    public Long getApplicationId() {
        return applicationId;
    }

    public ApplicationConfigHistory applicationId(Long applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getApplicationConfigid() {
        return applicationConfigid;
    }

    public ApplicationConfigHistory applicationConfigid(Long applicationConfigid) {
        this.applicationConfigid = applicationConfigid;
        return this;
    }

    public void setApplicationConfigid(Long applicationConfigid) {
        this.applicationConfigid = applicationConfigid;
    }

    public String getConfigContent() {
        return configContent;
    }

    public ApplicationConfigHistory configContent(String configContent) {
        this.configContent = configContent;
        return this;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    public Operation getOperation() {
        return operation;
    }

    public ApplicationConfigHistory operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ApplicationConfigHistory createTime(Long createTime) {
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
        ApplicationConfigHistory applicationConfigHistory = (ApplicationConfigHistory) o;
        if (applicationConfigHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applicationConfigHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplicationConfigHistory{" +
            "id=" + getId() +
            ", applicationId=" + getApplicationId() +
            ", applicationConfigid=" + getApplicationConfigid() +
            ", configContent='" + getConfigContent() + "'" +
            ", operation='" + getOperation() + "'" +
            ", createTime=" + getCreateTime() +
            "}";
    }
}

package com.utstar.uapollo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A ApplicationMetaHistory.
 */
@Entity
@Table(name = "application_meta_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApplicationMetaHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @NotNull
    @Column(name = "application_meta_id", nullable = false)
    private Long applicationMetaId;

    @NotNull
    @Size(max = 32)
    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @NotNull
    @Size(max = 32)
    @Column(name = "config_file", length = 32, nullable = false)
    private String configFile;

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

    public Long getProjectId() {
        return projectId;
    }

    public ApplicationMetaHistory projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getApplicationMetaId() {
        return applicationMetaId;
    }

    public ApplicationMetaHistory applicationMetaId(Long applicationMetaId) {
        this.applicationMetaId = applicationMetaId;
        return this;
    }

    public void setApplicationMetaId(Long applicationMetaId) {
        this.applicationMetaId = applicationMetaId;
    }

    public String getName() {
        return name;
    }

    public ApplicationMetaHistory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigFile() {
        return configFile;
    }

    public ApplicationMetaHistory configFile(String configFile) {
        this.configFile = configFile;
        return this;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getConfigContent() {
        return configContent;
    }

    public ApplicationMetaHistory configContent(String configContent) {
        this.configContent = configContent;
        return this;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    public Operation getOperation() {
        return operation;
    }

    public ApplicationMetaHistory operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ApplicationMetaHistory createTime(Long createTime) {
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
        ApplicationMetaHistory applicationMetaHistory = (ApplicationMetaHistory) o;
        if (applicationMetaHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applicationMetaHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplicationMetaHistory{" +
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

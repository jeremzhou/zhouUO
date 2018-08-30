package com.utstar.uapollo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ApplicationMeta.
 */
@Entity
@Table(name = "application_meta")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApplicationMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @Column(name = "create_time", nullable = false)
    private Long createTime;

    @NotNull
    @Column(name = "modify_time", nullable = false)
    private Long modifyTime;

    @ManyToOne
    private Project project;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ApplicationMeta name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigFile() {
        return configFile;
    }

    public ApplicationMeta configFile(String configFile) {
        this.configFile = configFile;
        return this;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;
    }

    public String getConfigContent() {
        return configContent;
    }

    public ApplicationMeta configContent(String configContent) {
        this.configContent = configContent;
        return this;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ApplicationMeta createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public ApplicationMeta modifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Project getProject() {
        return project;
    }

    public ApplicationMeta project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
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
        ApplicationMeta applicationMeta = (ApplicationMeta) o;
        if (applicationMeta.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applicationMeta.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplicationMeta{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", configFile='" + getConfigFile() + "'" +
            ", configContent='" + getConfigContent() + "'" +
            ", createTime=" + getCreateTime() +
            ", modifyTime=" + getModifyTime() +
            "}";
    }
}

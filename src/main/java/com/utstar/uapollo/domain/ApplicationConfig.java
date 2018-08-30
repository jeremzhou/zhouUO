package com.utstar.uapollo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ApplicationConfig.
 */
@Entity
@Table(name = "application_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApplicationConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private Application application;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigContent() {
        return configContent;
    }

    public ApplicationConfig configContent(String configContent) {
        this.configContent = configContent;
        return this;
    }

    public void setConfigContent(String configContent) {
        this.configContent = configContent;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ApplicationConfig createTime(Long createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public ApplicationConfig modifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
        return this;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Application getApplication() {
        return application;
    }

    public ApplicationConfig application(Application application) {
        this.application = application;
        return this;
    }

    public void setApplication(Application application) {
        this.application = application;
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
        ApplicationConfig applicationConfig = (ApplicationConfig) o;
        if (applicationConfig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), applicationConfig.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ApplicationConfig{" +
            "id=" + getId() +
            ", configContent='" + getConfigContent() + "'" +
            ", createTime=" + getCreateTime() +
            ", modifyTime=" + getModifyTime() +
            "}";
    }
}

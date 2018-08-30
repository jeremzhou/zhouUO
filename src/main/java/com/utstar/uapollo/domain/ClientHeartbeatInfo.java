package com.utstar.uapollo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A ClientHeartbeatInfo.
 */
@Entity
@Table(name = "client_heartbeat_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClientHeartbeatInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 32)
    @Column(name = "ip", length = 32, nullable = false)
    private String ip;

    @NotNull
    @Size(max = 32)
    @Column(name = "application_meta_name", length = 32, nullable = false)
    private String applicationMetaName;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private Long updateTime;

    @NotNull
    @Column(name = "update_version", nullable = false)
    private Long updateVersion;

    @NotNull
    @Column(name = "heartbeat_time", nullable = false)
    private Long heartbeatTime;

    @NotNull
    @Column(name = "heartbeat_version", nullable = false)
    private Long heartbeatVersion;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public ClientHeartbeatInfo ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getApplicationMetaName() {
        return applicationMetaName;
    }

    public ClientHeartbeatInfo applicationMetaName(String applicationMetaName) {
        this.applicationMetaName = applicationMetaName;
        return this;
    }

    public void setApplicationMetaName(String applicationMetaName) {
        this.applicationMetaName = applicationMetaName;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public ClientHeartbeatInfo updateTime(Long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateVersion() {
        return updateVersion;
    }

    public ClientHeartbeatInfo updateVersion(Long updateVersion) {
        this.updateVersion = updateVersion;
        return this;
    }

    public void setUpdateVersion(Long updateVersion) {
        this.updateVersion = updateVersion;
    }

    public Long getHeartbeatTime() {
        return heartbeatTime;
    }

    public ClientHeartbeatInfo heartbeatTime(Long heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
        return this;
    }

    public void setHeartbeatTime(Long heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }

    public Long getHeartbeatVersion() {
        return heartbeatVersion;
    }

    public ClientHeartbeatInfo heartbeatVersion(Long heartbeatVersion) {
        this.heartbeatVersion = heartbeatVersion;
        return this;
    }

    public void setHeartbeatVersion(Long heartbeatVersion) {
        this.heartbeatVersion = heartbeatVersion;
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
        ClientHeartbeatInfo clientHeartbeatInfo = (ClientHeartbeatInfo) o;
        if (clientHeartbeatInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clientHeartbeatInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClientHeartbeatInfo{" +
            "id=" + getId() +
            ", ip='" + getIp() + "'" +
            ", applicationMetaName='" + getApplicationMetaName() + "'" +
            ", updateTime=" + getUpdateTime() +
            ", updateVersion=" + getUpdateVersion() +
            ", heartbeatTime=" + getHeartbeatTime() +
            ", heartbeatVersion=" + getHeartbeatVersion() +
            "}";
    }
}

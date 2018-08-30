package com.utstar.uapollo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ClientHeartbeatInfo entity.
 */
public class ClientHeartbeatInfoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 32)
    private String ip;

    @NotNull
    @Size(max = 32)
    private String applicationMetaName;

    @NotNull
    private Long updateTime;

    @NotNull
    private Long updateVersion;

    @NotNull
    private Long heartbeatTime;

    @NotNull
    private Long heartbeatVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getApplicationMetaName() {
        return applicationMetaName;
    }

    public void setApplicationMetaName(String applicationMetaName) {
        this.applicationMetaName = applicationMetaName;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdateVersion() {
        return updateVersion;
    }

    public void setUpdateVersion(Long updateVersion) {
        this.updateVersion = updateVersion;
    }

    public Long getHeartbeatTime() {
        return heartbeatTime;
    }

    public void setHeartbeatTime(Long heartbeatTime) {
        this.heartbeatTime = heartbeatTime;
    }

    public Long getHeartbeatVersion() {
        return heartbeatVersion;
    }

    public void setHeartbeatVersion(Long heartbeatVersion) {
        this.heartbeatVersion = heartbeatVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientHeartbeatInfoDTO clientHeartbeatInfoDTO = (ClientHeartbeatInfoDTO) o;
        if(clientHeartbeatInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clientHeartbeatInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClientHeartbeatInfoDTO{" +
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

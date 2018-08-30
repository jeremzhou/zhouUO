package com.utstar.uapollo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Server entity.
 */
public class ServerDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 32)
    private String ip;

    @NotNull
    private Long createTime;

    @NotNull
    private Long modifyTime;

    private Long nodeId;

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

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServerDTO serverDTO = (ServerDTO) o;
        if(serverDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serverDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServerDTO{" +
            "id=" + getId() +
            ", ip='" + getIp() + "'" +
            ", createTime=" + getCreateTime() +
            ", modifyTime=" + getModifyTime() +
            "}";
    }
}

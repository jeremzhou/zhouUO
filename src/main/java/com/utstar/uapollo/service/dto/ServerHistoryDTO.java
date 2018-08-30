package com.utstar.uapollo.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A DTO for the ServerHistory entity.
 */
public class ServerHistoryDTO implements Serializable {

    private Long id;

    @NotNull
    private Long nodeId;

    @NotNull
    private Long serverId;

    @NotNull
    @Size(max = 32)
    private String ip;

    @NotNull
    private Operation operation;

    @NotNull
    private Long createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ServerHistoryDTO serverHistoryDTO = (ServerHistoryDTO) o;
        if(serverHistoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serverHistoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServerHistoryDTO{" +
            "id=" + getId() +
            ", nodeId=" + getNodeId() +
            ", serverId=" + getServerId() +
            ", ip='" + getIp() + "'" +
            ", operation='" + getOperation() + "'" +
            ", createTime=" + getCreateTime() +
            "}";
    }
}

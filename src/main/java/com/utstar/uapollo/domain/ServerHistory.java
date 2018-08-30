package com.utstar.uapollo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.utstar.uapollo.domain.enumeration.Operation;

/**
 * A ServerHistory.
 */
@Entity
@Table(name = "server_history")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ServerHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "node_id", nullable = false)
    private Long nodeId;

    @NotNull
    @Column(name = "server_id", nullable = false)
    private Long serverId;

    @NotNull
    @Size(max = 32)
    @Column(name = "ip", length = 32, nullable = false)
    private String ip;

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

    public Long getNodeId() {
        return nodeId;
    }

    public ServerHistory nodeId(Long nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getServerId() {
        return serverId;
    }

    public ServerHistory serverId(Long serverId) {
        this.serverId = serverId;
        return this;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getIp() {
        return ip;
    }

    public ServerHistory ip(String ip) {
        this.ip = ip;
        return this;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Operation getOperation() {
        return operation;
    }

    public ServerHistory operation(Operation operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public ServerHistory createTime(Long createTime) {
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
        ServerHistory serverHistory = (ServerHistory) o;
        if (serverHistory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serverHistory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServerHistory{" +
            "id=" + getId() +
            ", nodeId=" + getNodeId() +
            ", serverId=" + getServerId() +
            ", ip='" + getIp() + "'" +
            ", operation='" + getOperation() + "'" +
            ", createTime=" + getCreateTime() +
            "}";
    }
}

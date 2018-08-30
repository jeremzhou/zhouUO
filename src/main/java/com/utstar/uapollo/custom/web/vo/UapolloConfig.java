package com.utstar.uapollo.custom.web.vo;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.utstar.uapollo.custom.config.UapolloConfigConsts;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class UapolloConfig {

    private String appId;

    private String cluster;

    private String namespaceName;

    private Map<String, String> configurations;

    private String releaseKey;

    @JsonIgnore
    private Long notificationId;

    public UapolloConfig() {
    }

    public UapolloConfig(String appId, String cluster, String namespaceName, String releaseKey) {
        this.appId = appId;
        this.cluster = cluster;
        this.namespaceName = namespaceName;
        this.releaseKey = releaseKey;
    }

    public UapolloConfig(String appId, String releaseKey) {
        this.appId = appId;
        this.cluster = UapolloConfigConsts.DEFAULT_CLUSTER;
        this.namespaceName = UapolloConfigConsts.DEFAULT_NAMESPACE;
        this.releaseKey = releaseKey;
    }

    public String getAppId() {
        return appId;
    }

    public String getCluster() {
        return cluster;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public String getReleaseKey() {
        return releaseKey;
    }

    public Map<String, String> getConfigurations() {
        return configurations;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public void setReleaseKey(String releaseKey) {
        this.releaseKey = releaseKey;
    }

    public void setConfigurations(Map<String, String> configurations) {
        this.configurations = configurations;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UapolloConfig{");
        sb.append("appId='").append(appId).append('\'');
        sb.append(", cluster='").append(cluster).append('\'');
        sb.append(", namespaceName='").append(namespaceName).append('\'');
        sb.append(", configurations=").append(configurations);
        sb.append(", releaseKey='").append(releaseKey).append('\'');
        sb.append(", notificationId='").append(notificationId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

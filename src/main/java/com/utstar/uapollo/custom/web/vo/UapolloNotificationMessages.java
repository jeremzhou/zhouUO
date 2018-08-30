package com.utstar.uapollo.custom.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class UapolloNotificationMessages {
    private Map<String, Long> details;

    public UapolloNotificationMessages() {
        this(Maps.<String, Long>newHashMap());
    }

    private UapolloNotificationMessages(Map<String, Long> details) {
        this.details = details;
    }

    public void put(String key, long notificationId) {
        details.put(key, notificationId);
    }

    public Long get(String key) {
        return this.details.get(key);
    }

    public boolean has(String key) {
        return this.details.containsKey(key);
    }

    @JsonIgnore
    public boolean isEmpty() {
        return this.details.isEmpty();
    }

    public Map<String, Long> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Long> details) {
        this.details = details;
    }

    public void mergeFrom(UapolloNotificationMessages source) {
        if (source == null) {
            return;
        }

        for (Map.Entry<String, Long> entry : source.getDetails().entrySet()) {
            // to make sure the notification id always grows bigger
            if (this.has(entry.getKey()) && this.get(entry.getKey()) >= entry.getValue()) {
                continue;
            }
            this.put(entry.getKey(), entry.getValue());
        }
    }

    public UapolloNotificationMessages clone() {
        return new UapolloNotificationMessages(ImmutableMap.copyOf(this.details));
    }

    @Override
    public String toString() {
        return "UapolloNotificationMessages [details=" + details + "]";
    }
}

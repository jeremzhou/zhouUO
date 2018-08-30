package com.utstar.uapollo.custom.web.vo;

/**
 * @author Jason Song(song_s@ctrip.com)
 */
public class UapolloConfigNotification {
    private String namespaceName;
    private Long notificationId;
    private volatile UapolloNotificationMessages messages;

    // for json converter
    public UapolloConfigNotification() {
    }

    public UapolloConfigNotification(String namespaceName, long notificationId) {
        this.namespaceName = namespaceName;
        this.notificationId = notificationId;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNamespaceName(String namespaceName) {
        this.namespaceName = namespaceName;
    }

    public UapolloNotificationMessages getMessages() {
        return messages;
    }

    public void setMessages(UapolloNotificationMessages messages) {
        this.messages = messages;
    }

    public void addMessage(String key, long notificationId) {
        if (this.messages == null) {
            synchronized (this) {
                if (this.messages == null) {
                    this.messages = new UapolloNotificationMessages();
                }
            }
        }
        this.messages.put(key, notificationId);
    }

    @Override
    public String toString() {
        return "UapolloConfigNotification{" + "namespaceName='" + namespaceName + '\''
                + ", notificationId=" + notificationId + '}';
    }
}

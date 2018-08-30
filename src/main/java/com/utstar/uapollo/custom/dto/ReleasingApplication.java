/**
 * created on 2018年5月22日 上午10:24:58
 */
package com.utstar.uapollo.custom.dto;

/**
 * @author UTSC0167
 * @date 2018年5月22日
 *
 */
public final class ReleasingApplication {

    private final Long applicationMetaId;

    private final String applicationName;

    private final Long nodeId;

    private final String nodeName;

    private final String ip;

    private final Long applicationId;

    public ReleasingApplication(Long applicationMetaId, String applicationName, Long nodeId,
            String nodeName, String ip, Long applicationId) {
        super();
        this.applicationMetaId = applicationMetaId;
        this.applicationName = applicationName;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.ip = ip;
        this.applicationId = applicationId;
    }

    public Long getApplicationMetaId() {
        return applicationMetaId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public String getIp() {
        return ip;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    @Override
    public String toString() {
        return "ReleasingApplication [applicationMetaId=" + applicationMetaId + ", applicationName="
                + applicationName + ", nodeId=" + nodeId + ", nodeName=" + nodeName + ", ip=" + ip
                + ", applicationId=" + applicationId + "]";
    }
}

/**
 * created on 2018年5月25日 下午3:03:24
 */
package com.utstar.uapollo.custom.dto;

/**
 * @author UTSC0167
 * @date 2018年5月25日
 *
 */
public final class UapolloClientId {

    private final String ip;
    private final String applicationName;

    public UapolloClientId(String ip, String applicationName) {
        super();
        this.ip = ip;
        this.applicationName = applicationName;
    }

    public String getIp() {
        return ip;
    }

    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((applicationName == null) ? 0 : applicationName.hashCode());
        result = prime * result + ((ip == null) ? 0 : ip.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UapolloClientId other = (UapolloClientId) obj;
        if (applicationName == null) {
            if (other.applicationName != null)
                return false;
        } else if (!applicationName.equals(other.applicationName))
            return false;
        if (ip == null) {
            if (other.ip != null)
                return false;
        } else if (!ip.equals(other.ip))
            return false;
        return true;
    }

    @Override
    public String toString() {
        
        return "[ip=" + ip + ", applicationName=" + applicationName + "]";
    }
}

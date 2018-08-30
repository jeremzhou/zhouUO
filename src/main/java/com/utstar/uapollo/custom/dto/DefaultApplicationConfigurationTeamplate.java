/**
 * created on 2018年5月3日 下午3:55:26
 */
package com.utstar.uapollo.custom.dto;

/**
 * @author UTSC0167
 * @date 2018年5月3日
 *
 */
public final class DefaultApplicationConfigurationTeamplate {

    private final String applicationName;
    private final String configFile;
    private final String configContent;

    public DefaultApplicationConfigurationTeamplate(String applicationName, String configFile,
            String configContent) {
        this.applicationName = applicationName;
        this.configFile = configFile;
        this.configContent = configContent;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getConfigFile() {
        return configFile;
    }

    public String getConfigContent() {
        return configContent;
    }

    @Override
    public String toString() {
        return "DefaultApplicationConfigurationTeamplate [applicationName=" + applicationName
                + ", configFile=" + configFile + ", configContent=" + configContent + "]";
    }
}

/**
 * created on 2018年5月3日 下午3:54:22
 */
package com.utstar.uapollo.custom.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * @author UTSC0167
 * @date 2018年5月3日
 *
 */
public final class DefaultProjectConfigurationTemplate {

    private final String projectName;
    private final List<DefaultApplicationConfigurationTeamplate> DefaultProjectConfigurationList;

    public DefaultProjectConfigurationTemplate(String projectName) {
        this.projectName = projectName;
        this.DefaultProjectConfigurationList = new LinkedList<>();
    }

    public String getProjectName() {
        return projectName;
    }

    public List<DefaultApplicationConfigurationTeamplate> getDefaultProjectConfigurationList() {
        return DefaultProjectConfigurationList;
    }

    @Override
    public String toString() {
        return "DefaultProjectConfigurationTemplate [projectName=" + projectName
                + ", DefaultProjectConfigurationList=" + DefaultProjectConfigurationList + "]";
    }
}

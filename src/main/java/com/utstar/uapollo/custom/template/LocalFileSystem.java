/**
 * created on 2018年5月3日 下午3:50:25
 */
package com.utstar.uapollo.custom.template;

import java.util.List;
import java.util.Optional;

import com.utstar.uapollo.custom.dto.DefaultProjectConfigurationTemplate;

/**
 * @author UTSC0167
 * @date 2018年5月3日
 *
 */
public interface LocalFileSystem {

    Optional<List<DefaultProjectConfigurationTemplate>> readDefaultConfigurationTemplate(
            String templateConfigurationLocation);
}

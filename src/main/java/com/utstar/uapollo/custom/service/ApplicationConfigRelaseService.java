/**
 * created on 2018年8月17日 下午12:28:51
 */
package com.utstar.uapollo.custom.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.utstar.uapollo.custom.web.vo.UapolloConfig;
import com.utstar.uapollo.service.dto.ApplicationConfigDTO;

/**
 * @author UTSC0167
 * @date 2018年8月17日
 *
 */
public interface ApplicationConfigRelaseService {
    
    String tryGetClientIp(HttpServletRequest request);

    Optional<UapolloConfig> updateReleasedApplicationConfigCache(String ip, String applicationName);

    Optional<UapolloConfig> updateReleasedApplicationConfigCache(String ip, String applicationName,
            ApplicationConfigDTO applicationConfigDTO);
}

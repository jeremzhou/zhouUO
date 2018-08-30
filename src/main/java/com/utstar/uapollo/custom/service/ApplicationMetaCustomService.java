/**
 * created on 2018年4月17日 下午12:12:31
 */
package com.utstar.uapollo.custom.service;

import java.util.List;
import java.util.Optional;

import com.utstar.uapollo.custom.dto.ApplicationMetaReloadResult;
import com.utstar.uapollo.service.dto.ApplicationMetaDTO;

/**
 * @author UTSC0167
 * @date 2018年4月17日
 *
 */
public interface ApplicationMetaCustomService {

    Optional<List<ApplicationMetaReloadResult>> reload(String templateConfigurationLocation);

    List<ApplicationMetaDTO> findByProjectId(Long projectId);

    Optional<ApplicationMetaDTO> findByProjectIdAndName(Long projectId, String name);
    
    Optional<ApplicationMetaDTO> findByName(String name);
}

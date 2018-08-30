/**
 * created on 2018年5月22日 上午11:09:46
 */
package com.utstar.uapollo.custom.service;

import java.util.List;

import com.utstar.uapollo.custom.dto.ReleasingResult;

/**
 * @author UTSC0167
 * @date 2018年5月22日
 *
 */
public interface ReleaseConfigService {

    List<ReleasingResult> releaseGlobalConfig(Long applicationMetaId);
    
    List<ReleasingResult> releaseNodeConfig(Long applicationMetaId, Long nodeId);
    
    List<ReleasingResult> releaseApplicationConfig(Long applicationId);
}

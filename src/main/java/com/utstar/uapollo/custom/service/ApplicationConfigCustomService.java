/**
 * created on 2018年5月24日 上午11:05:13
 */
package com.utstar.uapollo.custom.service;

import java.util.Optional;

import com.utstar.uapollo.service.dto.ApplicationConfigDTO;

/**
 * @author UTSC0167
 * @date 2018年5月24日
 *
 */
public interface ApplicationConfigCustomService {

    Optional<ApplicationConfigDTO> findByApplicationIdAndCreateTimeMax(Long applicationId);
}

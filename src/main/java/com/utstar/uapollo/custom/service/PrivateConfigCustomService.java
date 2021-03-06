package com.utstar.uapollo.custom.service;

import java.util.List;
import java.util.Optional;

import com.utstar.uapollo.service.dto.PrivateConfigDTO;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
public interface PrivateConfigCustomService {

    List<PrivateConfigDTO> findByApplicationId(Long applicationId);
    
    Optional<PrivateConfigDTO> findByApplicationIdAndKey(Long applicationId, String key);
}

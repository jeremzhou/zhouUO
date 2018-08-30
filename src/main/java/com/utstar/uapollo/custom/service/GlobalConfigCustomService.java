package com.utstar.uapollo.custom.service;

import java.util.List;
import java.util.Optional;

import com.utstar.uapollo.service.dto.GlobalConfigDTO;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
public interface GlobalConfigCustomService {

    List<GlobalConfigDTO> findByApplicationMetaId(Long applicationMetaId);
    
    Optional<GlobalConfigDTO> findByApplicationMetaIdAndKey(Long applicationMetaId, String key);
}

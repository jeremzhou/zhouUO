package com.utstar.uapollo.custom.service;

import java.util.List;
import java.util.Optional;

import com.utstar.uapollo.custom.view.ApplicationVO;
import com.utstar.uapollo.service.dto.ApplicationDTO;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
public interface ApplicationCustomService {

    List<ApplicationDTO> findByProjectId(Long projectId);

    List<ApplicationDTO> findByApplicationMetaId(Long applicationMetaId);

    Optional<ApplicationDTO> findByApplicationMetaIdAndServerId(Long applicationMetaId,
            Long serverId);

    List<ApplicationDTO> findByApplicationMetaIdAndNodeId(Long applicationMetaId, Long nodeId);
    
    List<ApplicationVO> findAllAboutApplicationInfomation();
}
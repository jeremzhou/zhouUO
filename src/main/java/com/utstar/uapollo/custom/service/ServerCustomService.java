package com.utstar.uapollo.custom.service;

import java.util.List;
import java.util.Optional;

import com.utstar.uapollo.custom.view.ServerVO;
import com.utstar.uapollo.service.dto.ServerDTO;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
public interface ServerCustomService {

    List<ServerDTO> findByProjectId(Long projectId);
    
    List<ServerDTO> findByNodeId(Long nodeId);

    List<ServerDTO> findByApplicationMetaIdAndNodeId(Long applicationMetaId, Long nodeId);

    Optional<ServerDTO> findByApplicationId(Long applicationId);

    Optional<ServerDTO> findByNodeIdAndIp(Long nodeId, String ip);
    
    Optional<ServerDTO> findByIp(String ip);
    
    List<ServerVO> finaAllAboutServerInfomation();
}

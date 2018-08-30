/**
 * created on 2018年4月19日 下午2:23:33
 */
package com.utstar.uapollo.custom.service;

import java.util.List;
import java.util.Optional;

import com.utstar.uapollo.custom.view.NodeVO;
import com.utstar.uapollo.service.dto.NodeDTO;

/**
 * @author UTSC0167
 * @date 2018年4月19日
 *
 */
public interface NodeCustomService {

    List<NodeDTO> findByProjectId(Long projectId);

    List<NodeDTO> findByApplicationMetaId(Long applicationMetaId);

    Optional<NodeDTO> findByApplicationId(Long applicationId);

    Optional<NodeDTO> findByProjectIdAndName(Long projectId, String name);
    
    List<NodeVO> findAllInfomation();
}

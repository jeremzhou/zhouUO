package com.utstar.uapollo.custom.repositry;

import com.utstar.uapollo.custom.view.ServerVO;
import com.utstar.uapollo.domain.Node;
import com.utstar.uapollo.domain.Server;
import com.utstar.uapollo.service.dto.ServerDTO;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
@SuppressWarnings("unused")
@Repository
public interface ServerCustomRepository extends JpaRepository<Server, Long> {

    @Query(value = "select * from server where node_id in ( select id from node where project_id = ?1 )", nativeQuery = true)
    List<Server> findByProjectId(Long projectId);

    @Query(value = "select * from server where node_id = ?2 and node_id in ( select node.id from node, application_meta where node.project_id = application_meta.project_id and application_meta.id = ?1 )", nativeQuery = true)
    List<Server> findByApplicationMetaIdAndNodeId(Long applicationMetaId, Long nodeId);
        
    @Query(value = "select * from server where id in ( select server_id from application where id = ?1 )", nativeQuery = true)
    Optional<Server> findByApplicationId(Long applicationId);
    
    Optional<Server> findByNodeIdAndIp(Long nodeId, String ip);
    
    Optional<Server> findByIp(String ip);
    
    List<Server> findByNodeId(Long nodeId);
    
    @Query(value = "select s.id, s.ip, s.create_time, s.modify_time, n.name as nodeName, p.name as projectName from project p ,node n,server s where n.id = s.node_id and n.project_id = p.id", nativeQuery = true)
    List<Object> findAllInformation();
    
    
}

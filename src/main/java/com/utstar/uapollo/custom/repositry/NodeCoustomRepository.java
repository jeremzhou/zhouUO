package com.utstar.uapollo.custom.repositry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.utstar.uapollo.custom.view.NodeVO;
import com.utstar.uapollo.domain.Node;

/**
 * @author UTSC0167
 * @date 2018年4月19日
 *
 */
@SuppressWarnings("unused")
@Repository
public interface NodeCoustomRepository extends JpaRepository<Node, Long> {

    List<Node> findByProjectId(Long projectId);

    @Query(value = "select * from node where id in ( select distinct s.node_id from server s, application a where s.id = a.server_id and a.application_meta_id = ?1 )", nativeQuery = true)
    List<Node> findByApplicationMetaId(Long applicationMetaId);

    @Query(value = "select * from node where id in ( select distinct s.node_id from server s, application a where s.id = a.server_id and a.id = ?1 )", nativeQuery = true)
    Optional<Node> findByApplicationId(Long applicationId);

    Optional<Node> findByProjectIdAndNameIgnoreCase(Long projectId, String name);
    
    @Query(value = "select n.id , n.name as nodeName, n.create_time,n.modify_time, p.name as projectName from node n ,project p where n.project_id = p.id ", nativeQuery = true)
    List<Object> findAllInformation();
    
}
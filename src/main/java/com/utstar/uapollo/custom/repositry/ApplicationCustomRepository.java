package com.utstar.uapollo.custom.repositry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.utstar.uapollo.custom.view.ApplicationVO;
import com.utstar.uapollo.domain.Application;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationCustomRepository extends JpaRepository<Application, Long> {

    @Query(value = "select * from application where application_meta_id in ( select id from application_meta where project_id = ?1 )", nativeQuery = true)
    List<Application> findByProjectId(Long projectId);

    List<Application> findByApplicationMetaId(Long applicationMetaId);

    Optional<Application> findByApplicationMetaIdAndServerId(Long applicationMetaId, Long serverId);

    @Query(value = "select * from application where application_meta_id = ?1 and server_id in ( select id from server where node_id = ?2)", nativeQuery = true)
    List<Application> findByApplicationMetaIdAndNodeId(Long applicationMetaId, Long nodeId);
    
    @Query(value = "select a.id, a.create_time,a.modify_time, m.name as applicationMetaName,s.ip, n.name as nodeName, p.name as projectName from project p , node n, server s ,application_meta m, application a where a.application_meta_id = m.id and a.server_id = s.id and s.node_id = n.id and n.project_id = p.id", nativeQuery = true)
    List<Object> findAllInfomation();
}

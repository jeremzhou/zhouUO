package com.utstar.uapollo.custom.repositry;

import com.utstar.uapollo.domain.NodeConfig;
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
public interface NodeConfigCustomRepository extends JpaRepository<NodeConfig, Long> {

    List<NodeConfig> findByApplicationMetaId(Long applicationMetaId);

    List<NodeConfig> findByApplicationMetaIdAndNodeId(Long applicationMetaId, Long nodeId);

    @Query(value = "select * from node_config where application_meta_id = ?1 and node_id in ( select s.node_id from server s, application a where s.id = a.server_id and a.id = ?2 );", nativeQuery = true)
    List<NodeConfig> findByApplicationMetaIdAndApplicationId(Long applicationMetaId,
            Long applicationId);

    Optional<NodeConfig> findByApplicationMetaIdAndNodeIdAndKey(Long applicationMetaId, Long nodeId,
            String key);
}
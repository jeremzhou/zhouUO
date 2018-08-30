package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.NodeConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the NodeConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NodeConfigRepository extends JpaRepository<NodeConfig, Long> {

}

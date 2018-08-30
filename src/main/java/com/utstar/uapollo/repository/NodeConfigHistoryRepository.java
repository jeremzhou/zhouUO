package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.NodeConfigHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the NodeConfigHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NodeConfigHistoryRepository extends JpaRepository<NodeConfigHistory, Long> {

}

package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.NodeHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the NodeHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NodeHistoryRepository extends JpaRepository<NodeHistory, Long> {

}

package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.ServerHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ServerHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServerHistoryRepository extends JpaRepository<ServerHistory, Long> {

}

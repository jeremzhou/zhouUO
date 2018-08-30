package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.PrivateConfigHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PrivateConfigHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrivateConfigHistoryRepository extends JpaRepository<PrivateConfigHistory, Long> {

}

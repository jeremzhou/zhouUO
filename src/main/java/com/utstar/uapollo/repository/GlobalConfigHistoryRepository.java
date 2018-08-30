package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.GlobalConfigHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the GlobalConfigHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GlobalConfigHistoryRepository extends JpaRepository<GlobalConfigHistory, Long> {

}

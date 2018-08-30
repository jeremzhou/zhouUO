package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.ApplicationMetaHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ApplicationMetaHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationMetaHistoryRepository extends JpaRepository<ApplicationMetaHistory, Long> {

}

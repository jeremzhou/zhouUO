package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.ApplicationHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ApplicationHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationHistoryRepository extends JpaRepository<ApplicationHistory, Long> {

}

package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.ApplicationConfigHistory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ApplicationConfigHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationConfigHistoryRepository extends JpaRepository<ApplicationConfigHistory, Long> {

}

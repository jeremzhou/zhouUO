package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.ApplicationConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ApplicationConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationConfigRepository extends JpaRepository<ApplicationConfig, Long> {

}

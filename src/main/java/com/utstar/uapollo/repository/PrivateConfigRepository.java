package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.PrivateConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PrivateConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PrivateConfigRepository extends JpaRepository<PrivateConfig, Long> {

}

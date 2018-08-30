package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.GlobalConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the GlobalConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GlobalConfigRepository extends JpaRepository<GlobalConfig, Long> {

}

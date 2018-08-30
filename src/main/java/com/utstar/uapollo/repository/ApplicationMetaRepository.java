package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.ApplicationMeta;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ApplicationMeta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationMetaRepository extends JpaRepository<ApplicationMeta, Long> {

}

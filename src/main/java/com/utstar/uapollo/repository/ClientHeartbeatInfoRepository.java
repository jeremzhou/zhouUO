package com.utstar.uapollo.repository;

import com.utstar.uapollo.domain.ClientHeartbeatInfo;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ClientHeartbeatInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClientHeartbeatInfoRepository extends JpaRepository<ClientHeartbeatInfo, Long> {

}

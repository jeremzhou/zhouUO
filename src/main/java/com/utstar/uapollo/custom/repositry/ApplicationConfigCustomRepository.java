/**
 * created on 2018年5月23日 下午5:38:05
 */
package com.utstar.uapollo.custom.repositry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.utstar.uapollo.domain.ApplicationConfig;

/**
 * @author UTSC0167
 * @date 2018年5月23日
 *
 */
@Repository
public interface ApplicationConfigCustomRepository extends JpaRepository<ApplicationConfig, Long> {

    @Query(value = "select * from application_config where application_id = ?1 and create_time in ( select max(create_time) from application_config where application_id = ?1 )", nativeQuery = true)
    Optional<ApplicationConfig> findByApplicationIdAndCreateTimeMax(Long applicationId);
}

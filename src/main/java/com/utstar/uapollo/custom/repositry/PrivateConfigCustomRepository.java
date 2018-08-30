package com.utstar.uapollo.custom.repositry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utstar.uapollo.domain.PrivateConfig;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
@SuppressWarnings("unused")
@Repository
public interface PrivateConfigCustomRepository extends JpaRepository<PrivateConfig, Long> {

    List<PrivateConfig> findByApplicationId(Long applicationId);

    Optional<PrivateConfig> findByApplicationIdAndKey(Long applicationId, String key);
}

package com.utstar.uapollo.custom.repositry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utstar.uapollo.domain.GlobalConfig;

/**
 * @author UTSC0167
 * @date 2018年4月23日
 *
 */
@SuppressWarnings("unused")
@Repository
public interface GlobalConfigCustomRepository extends JpaRepository<GlobalConfig, Long> {

    List<GlobalConfig> findByApplicationMetaId(Long applicationMetaId);
    
    Optional<GlobalConfig> findByApplicationMetaIdAndKey(Long applicationMetaId, String key);
}

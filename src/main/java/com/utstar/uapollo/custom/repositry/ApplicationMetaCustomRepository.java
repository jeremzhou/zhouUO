package com.utstar.uapollo.custom.repositry;

import com.utstar.uapollo.domain.ApplicationMeta;
import com.utstar.uapollo.domain.Project;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;

/**
 * @author UTSC0167
 * @date 2018年4月17日
 *
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationMetaCustomRepository extends JpaRepository<ApplicationMeta, Long> {

    List<ApplicationMeta> findByProjectId(Long projectId);
    
    Optional<ApplicationMeta> findByProjectIdAndNameIgnoreCase(Long projectId, String name);
    
    
    Optional<ApplicationMeta> findByName(String name);
}

package com.utstar.uapollo.custom.repositry;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.utstar.uapollo.domain.Project;

/**
 * @author UTSC0167
 * @date 2018年4月16日
 *
 */
@SuppressWarnings("unused")
@Repository
public interface ProjectCustomRepository extends JpaRepository<Project, Long> {

    List<Project> findByUserId(Integer userid);

    Optional<Project> findByNameIgnoreCase(String name);
}

package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.repository;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.entity.ApplicationResourceJpaEntity;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.entity.ResourceJpaEntity;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ApplicationResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationResourceRepository extends JpaRepository<ApplicationResourceJpaEntity, Long> {


    Page<ApplicationResourceJpaEntity> findByApplicationCreatedById(Long createdById, Pageable pageable);


    boolean existsByResourcesId(Long resourceId);


    boolean existsByApplicationIdAndResourcesId(Long applicationId, Long resourceId);

    List<ApplicationResourceJpaEntity> findAllByApplicationId(Long applicationId);
    List<ApplicationResource> findByApplicationId(Long applicationId);

    List<List<String>> findCodeSnippetsByApplicationId(Long applicationId);
}
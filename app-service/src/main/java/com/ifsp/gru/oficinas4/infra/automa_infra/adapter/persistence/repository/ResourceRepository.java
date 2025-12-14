package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.repository;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.entity.ResourceJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<ResourceJpaEntity, Long> {

    // Integridade
    boolean existsByResourceTypeId(Long resourceTypeId);

    // Buscas Seguras (Scoped)
    Page<ResourceJpaEntity> findByCreatedById(Long createdById, Pageable pageable);

    Page<ResourceJpaEntity> findByNameContainingIgnoreCaseAndCreatedById(String name, Long createdById, Pageable pageable);

    // Note: ResourceTypeId refere-se ao objeto 'resourceType' na entidade. O Spring entende 'FindByResourceTypeId'.
    Page<ResourceJpaEntity> findByResourceTypeIdAndCreatedById(Long resourceTypeId, Long createdById, Pageable pageable);

    Page<ResourceJpaEntity> findByActiveAndCreatedById(Boolean active, Long createdById, Pageable pageable);
}
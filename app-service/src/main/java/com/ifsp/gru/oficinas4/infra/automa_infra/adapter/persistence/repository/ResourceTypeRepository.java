package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.repository;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.entity.ResourceTypeJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceTypeRepository extends JpaRepository<ResourceTypeJpaEntity, Long> {

    // O Spring gera: WHERE created_by_id = ?
    Page<ResourceTypeJpaEntity> findByCreatedById(Long createdById, Pageable pageable);

    // O Spring gera: WHERE UPPER(name) LIKE UPPER(%?%) AND created_by_id = ?
    Page<ResourceTypeJpaEntity> findByNameContainingIgnoreCaseAndCreatedById(String name, Long createdById, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
    Page<ResourceTypeJpaEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
    boolean existsByNameIgnoreCaseAndCreatedById(String name, Long createdById);
}
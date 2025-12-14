package com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

public interface ApplicationRepositoryPort {

    // CRUD
    Application save(Application application);
    Optional<Application> findById(Long id);
    void delete(Long id);

    Page<Application> findAll(Pageable pageable);
    Page<Application> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Application> findByStatus(String status, Pageable pageable);
    Page<Application> findByCreatedById(Long userId, Pageable pageable);
    Page<Application> findByCreatedByIdAndStatus(Long userId, String status, Pageable pageable);
    Page<Application> findByNameContainingIgnoreCaseAndStatus(String name, String status, Pageable pageable);


    boolean existsByNameAndCreatedById(String name, Long userId);
    Page<Application> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);
    Page<Application> findByLastDeployedAtAfter(LocalDateTime date, Pageable pageable);
    Page<Application> findByLastDeployedAtIsNull(Pageable pageable);


    long countByStatus(String status);
    long countByCreatedById(Long userId);
    long countByCreatedByIdAndStatus(Long userId, String status);


    Page<Application> findByNameContainingIgnoreCaseAndCreatedById(String name, Long createdById, Pageable pageable);


    Page<Application> findByNameContainingIgnoreCaseAndStatusAndCreatedById(String name, String status, Long createdById, Pageable pageable);


    Page<Application> findApplicationsNeedingAttention(LocalDateTime createdBefore, Pageable pageable);
}
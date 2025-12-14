package com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resourceTypes;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

// ResourceTypeRepositoryPort.java
public interface ResourceTypeRepositoryPort {
    ResourceType save(ResourceType resourceType);
    Optional<ResourceType> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByNameIgnoreCase(String name);
    Page<ResourceType> findByCreatedById(Long createdById, Pageable pageable);


    Page<ResourceType> findByNameContainingIgnoreCaseAndCreatedById(String name, Long createdById, Pageable pageable);



    boolean existsByNameIgnoreCaseAndCreatedById(String name, Long createdById);
    Page<ResourceType> findAll(Pageable pageable);
    Page<ResourceType> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
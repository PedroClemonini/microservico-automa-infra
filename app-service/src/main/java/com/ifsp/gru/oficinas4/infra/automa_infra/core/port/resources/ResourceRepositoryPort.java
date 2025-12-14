package com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resources;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ResourceRepositoryPort {

    Resource save(Resource resource);
    Optional<Resource> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);

    boolean existsByResourceTypeId(Long typeId);

    Page<Resource> findByCreatedById(Long createdById, Pageable pageable);


    Page<Resource> findByNameContainingIgnoreCaseAndCreatedById(String name, Long createdById, Pageable pageable);

    Page<Resource> findByResourceTypeIdAndCreatedById(Long typeId, Long createdById, Pageable pageable);

    Page<Resource> findByActiveAndCreatedById(Boolean active, Long createdById, Pageable pageable);
}
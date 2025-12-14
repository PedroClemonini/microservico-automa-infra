package com.ifsp.gru.oficinas4.infra.automa_infra.core.port.applicationResources;


import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ApplicationResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ApplicationResourceRepositoryPort {

    ApplicationResource save(ApplicationResource applicationResource);

    Optional<ApplicationResource> findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByApplicationIdAndResourceId(Long applicationId, Long resourceId);

    List<List<String>> findCodeSnippetsByApplicationId(Long applicationId);

    Page<ApplicationResource> findAll(Pageable pageable);

    List<ApplicationResource> findByApplicationId(Long applicationId);

    long count();

    Page<ApplicationResource> findByApplicationCreatedById(Long createdById, Pageable pageable);

    boolean existsByResourceId(Long resourceId);
}
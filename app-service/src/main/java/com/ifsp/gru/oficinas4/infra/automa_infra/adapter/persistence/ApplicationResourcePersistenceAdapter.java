package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.entity.ApplicationResourceJpaEntity;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.mapper.ApplicationResourceMapper;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.repository.ApplicationResourceRepository;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.applicationResources.ApplicationResourceRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ApplicationResource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApplicationResourcePersistenceAdapter implements ApplicationResourceRepositoryPort {

    private final ApplicationResourceRepository repository;
    private final ApplicationResourceMapper mapper;

    @Override
    public ApplicationResource save(ApplicationResource applicationResource) {
        ApplicationResourceJpaEntity entity = mapper.toJpaEntity(applicationResource);
        ApplicationResourceJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<ApplicationResource> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByApplicationIdAndResourceId(Long applicationId, Long resourceId) {
        return repository.existsByApplicationIdAndResourcesId(applicationId, resourceId);
    }

    @Override
    public List<List<String>> findCodeSnippetsByApplicationId(Long applicationId) {
        List<ApplicationResourceJpaEntity> entities = repository.findAllByApplicationId(applicationId);

        return entities.stream()
                .map(mapper::toDomain)
                .map(ApplicationResource::getResources)
                .filter(Objects::nonNull)
                .map(resource -> resource.getCodeSnippet()) // Assume List<String> no domínio
                .filter(list -> list != null && !list.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public Page<ApplicationResource> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public Page<ApplicationResource> findByApplicationCreatedById(Long createdById, Pageable pageable) {
        return repository.findByApplicationCreatedById(createdById, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByResourceId(Long resourceId) {
        return repository.existsByResourcesId(resourceId);
    }

    @Override
    public List<ApplicationResource> findByApplicationId(Long applicationId) {
        return repository.findAllByApplicationId(applicationId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
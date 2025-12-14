package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.entity.ResourceTypeJpaEntity;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.mapper.ResourceTypeMapper;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.repository.ResourceTypeRepository;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resourceTypes.ResourceTypeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResourceTypePersistenceAdapter implements ResourceTypeRepositoryPort {

    private final ResourceTypeRepository repository;
    private final ResourceTypeMapper mapper;



    @Override
    public ResourceType save(ResourceType resourceType) {
        ResourceTypeJpaEntity entity = mapper.toJpaEntity(resourceType);
        ResourceTypeJpaEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        // Verifica se existe algum Tipo com esse nome no banco inteiro (Global)
        return repository.existsByNameIgnoreCase(name);
    }
    @Override
    public Page<ResourceType> findAll(Pageable pageable) {

        return repository.findAll(pageable).map(mapper::toDomain);
    }
    @Override
    public Page<ResourceType> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<ResourceType> findById(Long id) {
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
    public Page<ResourceType> findByCreatedById(Long createdById, Pageable pageable) {

        return repository.findByCreatedById(createdById, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<ResourceType> findByNameContainingIgnoreCaseAndCreatedById(String name, Long createdById, Pageable pageable) {

        return repository.findByNameContainingIgnoreCaseAndCreatedById(name, createdById, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByNameIgnoreCaseAndCreatedById(String name, Long createdById) {

        return repository.existsByNameIgnoreCaseAndCreatedById(name, createdById);
    }
}
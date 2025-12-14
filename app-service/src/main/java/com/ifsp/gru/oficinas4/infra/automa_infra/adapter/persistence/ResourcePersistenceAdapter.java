package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.entity.ResourceJpaEntity;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.mapper.ResourceMapper;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.repository.ResourceRepository;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resources.ResourceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ResourcePersistenceAdapter implements ResourceRepositoryPort {

    private final ResourceRepository repository;
    private final ResourceMapper mapper;

    @Override
    public Resource save(Resource resource) {
        ResourceJpaEntity entity = mapper.toJpaEntity(resource);
        ResourceJpaEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Resource> findById(Long id) {
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
    public boolean existsByResourceTypeId(Long typeId) {
        return repository.existsByResourceTypeId(typeId);
    }


    @Override
    public Page<Resource> findByCreatedById(Long createdById, Pageable pageable) {
        return repository.findByCreatedById(createdById, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Resource> findByNameContainingIgnoreCaseAndCreatedById(String name, Long createdById, Pageable pageable) {
        return repository.findByNameContainingIgnoreCaseAndCreatedById(name, createdById, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Resource> findByResourceTypeIdAndCreatedById(Long typeId, Long createdById, Pageable pageable) {
        return repository.findByResourceTypeIdAndCreatedById(typeId, createdById, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Resource> findByActiveAndCreatedById(Boolean active, Long createdById, Pageable pageable) {
        return repository.findByActiveAndCreatedById(active, createdById, pageable)
                .map(mapper::toDomain);
    }
}
package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.mapper;


import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.entity.ApplicationResourceJpaEntity;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ApplicationResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationResourceMapper {


    private final ApplicationMapper applicationMapper;
    private final ResourceMapper resourceMapper;


    public ApplicationResourceJpaEntity toJpaEntity(ApplicationResource domain) {
        if (domain == null) {
            return null;
        }

        ApplicationResourceJpaEntity entity = new ApplicationResourceJpaEntity();

        entity.setId(domain.getId());
        entity.setAddedAt(domain.getAddedAt());


        if (domain.getApplication() != null) {
            entity.setApplication(applicationMapper.toJpaEntity(domain.getApplication()));
        }


        if (domain.getResources() != null) {
            entity.setResources(resourceMapper.toJpaEntity(domain.getResources()));
        }

        return entity;
    }


    public ApplicationResource toDomain(ApplicationResourceJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        ApplicationResource domain = new ApplicationResource();

        domain.setId(entity.getId());
        domain.setAddedAt(entity.getAddedAt());


        if (entity.getApplication() != null) {
            domain.setApplication(applicationMapper.toDomain(entity.getApplication()));
        }


        if (entity.getResources() != null) {
            domain.setResources(resourceMapper.toDomain(entity.getResources()));
        }

        return domain;
    }
}
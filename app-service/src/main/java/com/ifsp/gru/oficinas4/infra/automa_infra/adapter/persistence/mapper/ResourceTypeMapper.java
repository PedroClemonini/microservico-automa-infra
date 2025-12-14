package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.mapper;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.entity.ResourceTypeJpaEntity;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import org.springframework.stereotype.Component;

@Component
public class ResourceTypeMapper {

    public ResourceTypeJpaEntity toJpaEntity(ResourceType domain) {
        if (domain == null) {
            return null;
        }

        ResourceTypeJpaEntity entity = new ResourceTypeJpaEntity();


        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());



        entity.setCreatedById(domain.getCreatedById());
        entity.setCreatedByName(domain.getCreatedByName());
        entity.setCreatedByEmail(domain.getCreatedByEmail());


        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    public ResourceType toDomain(ResourceTypeJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        ResourceType domain = new ResourceType();


        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());

        domain.setCreatedById(entity.getCreatedById());
        domain.setCreatedByName(entity.getCreatedByName());
        domain.setCreatedByEmail(entity.getCreatedByEmail());

        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());

        return domain;
    }
}
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

        // Dados Básicos
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());

        // --- NOVOS CAMPOS: Snapshot de Auditoria ---
        // Essenciais para saber quem criou o registro
        entity.setCreatedById(domain.getCreatedById());
        entity.setCreatedByName(domain.getCreatedByName());
        entity.setCreatedByEmail(domain.getCreatedByEmail());

        // --- NOVOS CAMPOS: Timestamps ---
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());

        return entity;
    }

    public ResourceType toDomain(ResourceTypeJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        ResourceType domain = new ResourceType();

        // Dados Básicos
        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());

        // --- NOVOS CAMPOS: Snapshot de Auditoria ---
        domain.setCreatedById(entity.getCreatedById());
        domain.setCreatedByName(entity.getCreatedByName());
        domain.setCreatedByEmail(entity.getCreatedByEmail());

        // --- NOVOS CAMPOS: Timestamps ---
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());

        return domain;
    }
}
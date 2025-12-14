package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.mapper;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.entity.ApplicationJpaEntity;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@Component
public class ApplicationMapper {

    public ApplicationJpaEntity toJpaEntity(Application domain) {
        if (domain == null) return null;

        ApplicationJpaEntity entity = new ApplicationJpaEntity();

        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setStatus(domain.getStatus());
        entity.setIpAddress(domain.getIpAddress());

        // Credenciais
        entity.setSshUser(domain.getSshUser());
        entity.setSshPassword(domain.getSshPassword());

        // Timestamps
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        entity.setLastDeployedAt(domain.getLastDeployedAt());

        // --- SNAPSHOT DE AUDITORIA (Mapeamento Plano) ---
        // Aqui substituímos o objeto User pelos campos individuais
        entity.setCreatedById(domain.getCreatedById());
        entity.setCreatedByName(domain.getCreatedByName());
        entity.setCreatedByEmail(domain.getCreatedByEmail());

        return entity;
    }

    public Application toDomain(ApplicationJpaEntity entity) {
        if (entity == null) return null;

        Application domain = new Application();

        domain.setId(entity.getId());
        domain.setName(entity.getName());
        domain.setDescription(entity.getDescription());
        domain.setStatus(entity.getStatus());
        domain.setIpAddress(entity.getIpAddress());

        // Credenciais
        domain.setSshUser(entity.getSshUser());
        domain.setSshPassword(entity.getSshPassword());

        // Timestamps
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        domain.setLastDeployedAt(entity.getLastDeployedAt());

        // --- SNAPSHOT DE AUDITORIA ---
        domain.setCreatedById(entity.getCreatedById());
        domain.setCreatedByName(entity.getCreatedByName());
        domain.setCreatedByEmail(entity.getCreatedByEmail());

        return domain;
    }
}
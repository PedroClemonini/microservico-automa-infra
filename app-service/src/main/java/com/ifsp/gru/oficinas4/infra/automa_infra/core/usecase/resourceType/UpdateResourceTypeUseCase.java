package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resourceType;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resourceTypes.ResourceTypeRepositoryPort;

import com.ifsp.gru.oficinas4.infra.automa_infra.exception.DuplicateResourceException;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UpdateResourceTypeUseCase {

    private final ResourceTypeRepositoryPort repository;

    @Transactional
    public ResourceType execute(Long id, ResourceType incomingData, Long requestingUserId) {

        ResourceType existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de Recurso não encontrado: " + id));


        if (!existing.getCreatedById().equals(requestingUserId)) {
            throw new RuntimeException("Permissão negada. Você não é o dono deste Tipo de Recurso.");
        }

        if (incomingData.getName() != null && !incomingData.getName().isBlank()) {

            if (!incomingData.getName().equalsIgnoreCase(existing.getName()) &&
                    repository.existsByNameIgnoreCaseAndCreatedById(incomingData.getName(), requestingUserId)) {
                throw new DuplicateResourceException("Já existe outro Tipo com este nome.");
            }
            existing.setName(incomingData.getName());
        }

        if (incomingData.getDescription() != null) {
            existing.setDescription(incomingData.getDescription());
        }

        existing.setUpdatedAt(LocalDateTime.now());

        return repository.save(existing);
    }
}
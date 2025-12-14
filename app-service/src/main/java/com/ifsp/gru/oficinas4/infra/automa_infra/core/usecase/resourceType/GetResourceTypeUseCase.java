package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resourceType;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resourceTypes.ResourceTypeRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// GET
@Service
@RequiredArgsConstructor
public class GetResourceTypeUseCase {
    private final ResourceTypeRepositoryPort repository;

    @Transactional(readOnly = true)
    public ResourceType execute(Long id, Long requestingUserId) {
        ResourceType rt = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo não encontrado: " + id));

        if (!rt.getCreatedById().equals(requestingUserId)) {
            throw new RuntimeException("Permissão negada.");
        }
        return rt;
    }
}


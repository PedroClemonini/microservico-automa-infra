package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resourceType;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resourceTypes.ResourceTypeRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListResourceTypeUseCase {
    private final ResourceTypeRepositoryPort repository;

    @Transactional(readOnly = true)
    public Page<ResourceType> execute(Pageable pageable, Long requestingUserId) {

        return repository.findByCreatedById(requestingUserId, pageable);
    }
}
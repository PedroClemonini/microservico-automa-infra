package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resourceType;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.external.auth.AuthServiceClient;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.external.auth.dto.UserPublicData;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resourceTypes.ResourceTypeRepositoryPort;

import com.ifsp.gru.oficinas4.infra.automa_infra.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateResourceTypeUseCase {

    private final ResourceTypeRepositoryPort repository;


    @Transactional

    public ResourceType execute(ResourceType incomingRequest, Long userId, String userName, String userEmail) {

        if (repository.existsByNameIgnoreCaseAndCreatedById(incomingRequest.getName(), userId)) {
            throw new DuplicateResourceException("Duplicado...");
        }

        ResourceType rt = new ResourceType();
        rt.setName(incomingRequest.getName());
        rt.setDescription(incomingRequest.getDescription());


        rt.setCreatedById(userId);
        rt.setCreatedByName(userName);
        rt.setCreatedByEmail(userEmail);

        rt.setCreatedAt(LocalDateTime.now());
        rt.setUpdatedAt(LocalDateTime.now());

        return repository.save(rt);
    }
}
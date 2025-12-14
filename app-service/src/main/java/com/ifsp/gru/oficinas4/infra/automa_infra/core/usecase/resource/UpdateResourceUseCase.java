package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resource;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resources.ResourceRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resourceTypes.ResourceTypeRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
@Service
@RequiredArgsConstructor
public class UpdateResourceUseCase {

    private final ResourceRepositoryPort resourceRepository;
    private final ResourceTypeRepositoryPort resourceTypeRepository;

    @Transactional

    public Resource execute(Long id, Resource incomingData, Long requestingUserId) {


        Resource existingResource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado com ID: " + id));


        if (!existingResource.getCreatedById().equals(requestingUserId)) {
            throw new UnauthorizedException("Você não tem permissão para editar este recurso.");
        }

        if (incomingData.getResourceType() != null && incomingData.getResourceType().getId() != null) {
            Long newTypeId = incomingData.getResourceType().getId();


            if (!newTypeId.equals(existingResource.getResourceType().getId())) {
                ResourceType newResourceType = resourceTypeRepository.findById(newTypeId)
                        .orElseThrow(() -> new ResourceNotFoundException("Tipo de recurso não encontrado com ID: " + newTypeId));
                existingResource.setResourceType(newResourceType);
            }
        }

        if (hasText(incomingData.getName())) {
            existingResource.setName(incomingData.getName());
        }

        if (hasText(incomingData.getDescription())) {
            existingResource.setDescription(incomingData.getDescription());
        }

        if (hasText(incomingData.getVersion())) {
            existingResource.setVersion(incomingData.getVersion());
        }

        if (incomingData.getCodeSnippet() != null) {
            existingResource.setCodeSnippet(incomingData.getCodeSnippet());
        }

        if (incomingData.getActive() != null) {
            existingResource.setActive(incomingData.getActive());
        }


        existingResource.setUpdatedAt(LocalDateTime.now());

        return resourceRepository.save(existingResource);
    }

    private boolean hasText(String text) {
        return text != null && !text.isBlank();
    }
}
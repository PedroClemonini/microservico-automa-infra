package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resource;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resources.ResourceRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resourceTypes.ResourceTypeRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateResourceUseCase {

    private final ResourceRepositoryPort resourceRepository;
    private final ResourceTypeRepositoryPort resourceTypeRepository;


    @Transactional
    public Resource execute(Resource dto, Long userId, String userName, String userEmail) {

        ResourceType resourceType = resourceTypeRepository.findById(dto.getResourceType().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tipo de recurso não encontrado com ID: " + dto.getResourceType().getId()
                ));


        Resource resource = new Resource();

        resource.setResourceType(resourceType);
        resource.setName(dto.getName());
        resource.setDescription(dto.getDescription());
        resource.setVersion(dto.getVersion());
        resource.setCodeSnippet(dto.getCodeSnippet());
        resource.setActive(dto.getActive() != null ? dto.getActive() : true);
        resource.setCreatedById(userId);
        resource.setCreatedByName(userName);
        resource.setCreatedByEmail(userEmail);
        LocalDateTime now = LocalDateTime.now();
        resource.setCreatedAt(now);
        resource.setUpdatedAt(now);
        return resourceRepository.save(resource);
    }
}
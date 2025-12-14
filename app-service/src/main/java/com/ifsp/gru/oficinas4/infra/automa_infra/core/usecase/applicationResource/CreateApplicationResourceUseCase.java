package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.applicationResource;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ApplicationResource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ApplicationRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.applicationResources.ApplicationResourceRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resources.ResourceRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateApplicationResourceUseCase {

    private final ApplicationResourceRepositoryPort repository;
    private final ApplicationRepositoryPort applicationRepository;
    private final ResourceRepositoryPort resourceRepository;

    @Transactional
    public ApplicationResource execute(Long applicationId, Long resourceId, Long userId) {


        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Aplicação não encontrada com ID: " + applicationId));

        if (!application.getCreatedById().equals(userId)) {
            throw new UnauthorizedException("Você não tem permissão para modificar esta aplicação.");
        }


        boolean alreadyLinked = repository.existsByApplicationIdAndResourceId(applicationId, resourceId);
        if (alreadyLinked) {
            throw new BusinessException("O Recurso com ID " + resourceId + " já está vinculado à Aplicação com ID " + applicationId + ".");
        }


        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado com ID: " + resourceId));

        if (Boolean.FALSE.equals(resource.getActive())) {
            throw new BusinessException("Não é possível vincular um recurso inativo.");
        }


        ApplicationResource newLink = new ApplicationResource();
        newLink.setApplication(application);
        newLink.setResources(resource);
        newLink.setAddedAt(LocalDateTime.now());
        return repository.save(newLink);
    }
}
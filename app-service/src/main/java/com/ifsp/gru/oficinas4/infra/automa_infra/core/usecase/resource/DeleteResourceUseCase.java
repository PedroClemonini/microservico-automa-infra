package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resource;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.applicationResources.ApplicationResourceRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resources.ResourceRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.BusinessException;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class DeleteResourceUseCase {

    private final ResourceRepositoryPort resourceRepository;

    private final ApplicationResourceRepositoryPort applicationResourceRepository;

    @Transactional

    public void execute(Long resourceId, Long requestingUserId) {


        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado para exclusão: " + resourceId));



        if (!resource.getCreatedById().equals(requestingUserId)) {
            throw new UnauthorizedException("Você não tem permissão para excluir este recurso.");
        }


        boolean isInUse = applicationResourceRepository.existsByResourceId(resourceId);

        if (isInUse) {
            throw new BusinessException("Não é possível excluir o recurso pois ele está vinculado a uma ou mais aplicações. Remova os vínculos primeiro.");
        }


        resourceRepository.deleteById(resourceId);
    }
}
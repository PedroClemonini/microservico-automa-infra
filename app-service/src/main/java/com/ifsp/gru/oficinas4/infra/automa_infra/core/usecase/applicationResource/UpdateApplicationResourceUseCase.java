package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.applicationResource;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ApplicationResource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ApplicationRepositoryPort;
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
public class UpdateApplicationResourceUseCase {

    private final ApplicationResourceRepositoryPort repository;
    private final ApplicationRepositoryPort applicationRepository;
    private final ResourceRepositoryPort resourceRepository;

    @Transactional
    public ApplicationResource execute(Long id, ApplicationResource incomingData, Long requestingUserId) {

        ApplicationResource existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado com ID: " + id));

        if (!existing.getApplication().getCreatedById().equals(requestingUserId)) {
            throw new UnauthorizedException("Você não tem permissão para alterar este vínculo.");
        }


        Long currentAppId = existing.getApplication().getId();
        Long currentResId = existing.getResources().getId();

        boolean changed = false;


        if (incomingData.getApplication() != null && incomingData.getApplication().getId() != null) {
            Long newAppId = incomingData.getApplication().getId();


            if (!newAppId.equals(currentAppId)) {
                Application newApplication = applicationRepository.findById(newAppId)
                        .orElseThrow(() -> new ResourceNotFoundException("Aplicação de destino não encontrada com ID: " + newAppId));


                if (!newApplication.getCreatedById().equals(requestingUserId)) {
                    throw new UnauthorizedException("Você não pode mover um recurso para uma aplicação que não é sua.");
                }

                existing.setApplication(newApplication);
                currentAppId = newAppId;
                changed = true;
            }
        }

        // 4. Atualização do Recurso (Trocar o recurso vinculado)
        if (incomingData.getResources() != null && incomingData.getResources().getId() != null) {
            Long newResId = incomingData.getResources().getId();

            if (!newResId.equals(currentResId)) {
                Resource newResource = resourceRepository.findById(newResId)
                        .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado com ID: " + newResId));

                existing.setResources(newResource);
                currentResId = newResId; // Atualiza variável de controle
                changed = true;
            }
        }


        if (changed) {
            boolean comboExists = repository.existsByApplicationIdAndResourceId(currentAppId, currentResId);

            if (comboExists) {
                throw new BusinessException("O Recurso ID " + currentResId + " já está vinculado à Aplicação ID " + currentAppId + ".");
            }
        }

        return repository.save(existing);
    }
}
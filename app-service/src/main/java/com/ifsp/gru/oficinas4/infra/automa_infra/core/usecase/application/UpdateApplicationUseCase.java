package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.application;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ApplicationRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class UpdateApplicationUseCase {

    private final ApplicationRepositoryPort applicationRepository;

    @Transactional

    public Application execute(Long id, Application incomingData, Long requestingUserId) {


        Application existingApp = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application não encontrada: " + id));


        if (!existingApp.getCreatedById().equals(requestingUserId)) {
            throw new UnauthorizedException("Você não tem permissão para editar os dados desta aplicação.");
        }


        if (hasText(incomingData.getName())) {
            existingApp.setName(incomingData.getName());
        }

        if (incomingData.getDescription() != null) {
            existingApp.setDescription(incomingData.getDescription());
        }

        if (hasText(incomingData.getStatus())) {
            existingApp.setStatus(incomingData.getStatus());
        }

        if (hasText(incomingData.getSshUser())) {
            existingApp.setSshUser(incomingData.getSshUser());
        }

        if (hasText(incomingData.getSshPassword())) {
            existingApp.setSshPassword(incomingData.getSshPassword());
        }


        existingApp.setUpdatedAt(LocalDateTime.now());


        return applicationRepository.save(existingApp);
    }

    private boolean hasText(String text) {
        return text != null && !text.isBlank();
    }
}
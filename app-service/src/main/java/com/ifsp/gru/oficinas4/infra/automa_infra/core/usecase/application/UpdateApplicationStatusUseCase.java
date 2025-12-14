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
public class UpdateApplicationStatusUseCase {

    private final ApplicationRepositoryPort applicationRepository;

    @Transactional

    public Application execute(Long applicationId, String newStatus, Long requestingUserId) {


        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application não encontrada: " + applicationId));

        if (!app.getCreatedById().equals(requestingUserId)) {
            throw new UnauthorizedException("Você não tem permissão para alterar o status desta aplicação.");
        }

        app.setStatus(newStatus);
        app.setUpdatedAt(LocalDateTime.now());

        return applicationRepository.save(app);
    }
}
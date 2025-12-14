package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.application;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ApplicationRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class GetApplicationUseCase {

    private final ApplicationRepositoryPort applicationRepository;

    @Transactional(readOnly = true)
    public Application execute(Long applicationId, Long requestingUserId) {

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application não encontrada: " + applicationId));

        if (!app.getCreatedById().equals(requestingUserId)) {
            throw new UnauthorizedException("Você não tem permissão para visualizar esta aplicação.");
        }

        return app;
    }
}
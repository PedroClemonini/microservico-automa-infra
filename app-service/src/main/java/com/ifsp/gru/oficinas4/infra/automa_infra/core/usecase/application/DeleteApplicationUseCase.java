package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.application;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ApplicationRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteApplicationUseCase {

    private final ApplicationRepositoryPort applicationRepository;

    @Transactional
    public void execute(Long applicationId, Long requestingUserId) {

        applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application não encontrada com ID: " + applicationId));

        applicationRepository.delete(applicationId);

    }
}
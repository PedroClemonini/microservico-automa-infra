package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.application;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ApplicationRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListApplicationsUseCase {

    private final ApplicationRepositoryPort applicationRepository;

    @Transactional(readOnly = true)

    public Page<Application> execute(String name, String status, Long requestingUserId, Pageable pageable) {

        if (name != null && status != null) {
            return applicationRepository.findByNameContainingIgnoreCaseAndStatusAndCreatedById(name, status, requestingUserId, pageable);
        }

        if (name != null) {
            return applicationRepository.findByNameContainingIgnoreCaseAndCreatedById(name, requestingUserId, pageable);
        }

        if (status != null) {
            return applicationRepository.findByCreatedByIdAndStatus(requestingUserId, status, pageable);
        }

        return applicationRepository.findByCreatedById(requestingUserId, pageable);
    }
}
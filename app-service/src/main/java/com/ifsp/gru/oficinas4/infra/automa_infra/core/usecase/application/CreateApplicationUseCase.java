package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.application;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ApplicationRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateApplicationUseCase {

    private final ApplicationRepositoryPort applicationRepositoryPort;




    @Transactional
    public Application execute(Application application, Long userId, String userName, String userEmail) {


        application.setCreatedById(userId);
        application.setCreatedByName(userName);
        application.setCreatedByEmail(userEmail);
        LocalDateTime now = LocalDateTime.now();
        application.setCreatedAt(now);
        application.setUpdatedAt(now);
        if (application.getStatus() == null || application.getStatus().isBlank()) {
            application.setStatus("DRAFT");
        }

        return applicationRepositoryPort.save(application);
    }
}
package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.infrastructure.configuration;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.persistence.repository.ApplicationResourceRepository;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.application.mapper.ApplicationWebMapper;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ApplicationRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ProvisioningPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.applicationResources.ApplicationResourceRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProvisioningAsyncService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ApplicationResourceRepositoryPort applicationResourceRepository;
    private final ApplicationRepositoryPort applicationRepository;
    private final ProvisioningPort provisioningPort;
    private final ApplicationWebMapper mapper;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void startProvisioning(Long applicationId, Long userId) {
        log.info("Iniciando provisionamento assíncrono para App ID: {} (User: {})", applicationId, userId);

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Application não encontrada durante processamento"));

        try {

            notifyProgress(userId, applicationId, 10, "DEPLOYING", "Carregando configurações...");

            List<List<String>> resourcesList = applicationResourceRepository.findCodeSnippetsByApplicationId(applicationId);


            notifyProgress(userId, applicationId, 30, "DEPLOYING", "Iniciando provisionamento de infraestrutura...");

            ProvisioningPort.ProvisioningResult result = provisioningPort.provision(app, resourcesList);


            notifyProgress(userId, applicationId, 90, "DEPLOYING", "Finalizando deploy...");

            if (result.publicIp() != null) {
                app.setIpAddress(result.publicIp());
                app.setStatus("DEPLOYED");
                log.info("Deploy concluído com sucesso. IP: {}", result.publicIp());


                notifyProgress(userId, applicationId, 100, "DEPLOYED",
                        "Deploy concluído! IP: " + result.publicIp());
            } else {
                app.setStatus("DEPLOYED_NO_IP");
                log.warn("Deploy concluído sem IP público.");

                notifyProgress(userId, applicationId, 100, "DEPLOYED_NO_IP",
                        "Deploy concluído (sem IP público)");
            }

        } catch (Exception e) {
            log.error("Erro no provisionamento da App ID {}: {}", applicationId, e.getMessage(), e);
            app.setStatus("DEPLOY_FAILED");

            notifyProgress(userId, applicationId, -1, "DEPLOY_FAILED",
                    "Falha no deploy: " + e.getMessage());
        }

        app.setLastDeployedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());

        Application savedApp = applicationRepository.save(app);

        notifyFinalState(userId, savedApp);

        log.info("Provisionamento finalizado para App ID: {} com status: {}", applicationId, savedApp.getStatus());
    }



    private void notifyProgress(Long userId, Long appId, int progress, String status, String message) {
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/deploy-progress",
                Map.of(
                        "applicationId", appId,
                        "progress", progress,
                        "status", status,
                        "message", message,
                        "timestamp", Instant.now()
                )
        );
        log.debug("Progresso enviado para user {}: {}% - {}", userId, progress, message);
    }


    private void notifyFinalState(Long userId, Application app) {
        messagingTemplate.convertAndSend(
                "/topic/deploy-status",
                mapper.toResponse(app)
        );
        log.info("Estado final enviado para user {}: {}", userId, app.getStatus());
    }
}
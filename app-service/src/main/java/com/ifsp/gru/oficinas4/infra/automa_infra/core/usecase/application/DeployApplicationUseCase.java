package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.application;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.infrastructure.configuration.ProvisioningAsyncService;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ApplicationRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ProvisioningPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.applicationResources.ApplicationResourceRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeployApplicationUseCase {

    private final ApplicationRepositoryPort applicationRepository;
    private final ProvisioningAsyncService provisioningAsyncService;
    private final SimpMessagingTemplate messagingTemplate;


    @Transactional
    public Application execute(Long applicationId, Long userId) { // Renomeado para 'userId' para padronizar
        log.info("Iniciando Use Case de Deploy para App ID: {} solicitado por User ID: {}", applicationId, userId);

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application não encontrada: " + applicationId));


        if (!app.getCreatedById().equals(userId)) {
            log.warn("Tentativa de deploy bloqueada. Usuário {} tentou deployar app {} (Dono: {})",
                    userId, applicationId, app.getCreatedById());
            throw new UnauthorizedException("Você não tem permissão para realizar o deploy desta aplicação.");
        }

        app.setStatus("DEPLOYING");
        Application saved = applicationRepository.save(app);
        notifyStatus(app.getId(), "DEPLOYING", "Iniciando deploy...");
        try {
            provisioningAsyncService.startProvisioning(applicationId, userId);

        }catch (Exception e) {
            log.error("Erro ao iniciar provisionamento: {}", e.getMessage());
            app.setStatus("FAILED");
            applicationRepository.save(app);
            notifyStatus(app.getId(), "FAILED", "Falha ao iniciar deploy: " + e.getMessage());
            throw e;
        }

        return saved;

    }

    private void notifyStatus(Long appId, String status, String message) {
        messagingTemplate.convertAndSend("/topic/deployments",
                Map.of(
                        "applicationId", appId,
                        "status", status,
                        "message", message,
                        "timestamp", Instant.now()
                )
        );
    }
}
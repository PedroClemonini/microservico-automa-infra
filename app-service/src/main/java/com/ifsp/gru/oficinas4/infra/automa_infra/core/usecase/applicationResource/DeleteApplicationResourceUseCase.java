package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.applicationResource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ApplicationResource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.applicationResources.ApplicationResourceRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteApplicationResourceUseCase {

    private final ApplicationResourceRepositoryPort repository;

    @Transactional
    public void execute(Long id, Long requestingUserId) {


        ApplicationResource appResource = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vínculo não encontrado para exclusão: " + id));

        Long appOwnerId = appResource.getApplication().getCreatedById();

        if (!appOwnerId.equals(requestingUserId)) {
            throw new UnauthorizedException("Você não tem permissão para remover recursos desta aplicação.");
        }

        repository.deleteById(id);
    }
}
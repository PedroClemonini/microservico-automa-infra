package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resources.ResourceRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetResourceUseCase {

    private final ResourceRepositoryPort resourceRepository;

    @Transactional(readOnly = true)

    public Resource execute(Long id, Long requestingUserId) {


        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado: " + id));

        if (!resource.getCreatedById().equals(requestingUserId)) {


            throw new UnauthorizedException("Você não tem permissão para visualizar este recurso.");
        }

        return resource;
    }
}
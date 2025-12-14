package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resourceType;


import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resourceTypes.ResourceTypeRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resources.ResourceRepositoryPort;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteResourceTypeUseCase {

    private final ResourceTypeRepositoryPort repository;

    private final ResourceRepositoryPort resourceRepository;

    @Transactional
    public void execute(Long id, Long requestingUserId) {

        ResourceType rt = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo não encontrado: " + id));


        if (!rt.getCreatedById().equals(requestingUserId)) {
            throw new RuntimeException("Permissão negada.");
        }


        if (resourceRepository.existsByResourceTypeId(id)) {
            throw new RuntimeException("Não é possível excluir este Tipo pois existem Recursos associados a ele.");
        }

        repository.deleteById(id);
    }
}
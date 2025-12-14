package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resource;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.resources.ResourceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListResourcesUseCase {

    private final ResourceRepositoryPort resourceRepository;

    @Transactional(readOnly = true)

    public Page<Resource> execute(String name, Long typeId, Boolean active, Pageable pageable, Long requestingUserId) {


        if (name != null && !name.isBlank()) {
            return resourceRepository.findByNameContainingIgnoreCaseAndCreatedById(name, requestingUserId, pageable);
        }


        if (typeId != null) {
            return resourceRepository.findByResourceTypeIdAndCreatedById(typeId, requestingUserId, pageable);
        }


        if (active != null) {
            return resourceRepository.findByActiveAndCreatedById(active, requestingUserId, pageable);
        }



        return resourceRepository.findByCreatedById(requestingUserId, pageable);
    }
}
package com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.applicationResource;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.application.ApplicationRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.port.applicationResources.ApplicationResourceRepositoryPort;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ApplicationResource;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ListApplicationResourcesUseCase {

    private final ApplicationResourceRepositoryPort resourceRepository;
    private final ApplicationRepositoryPort applicationRepository;

    @Transactional(readOnly = true)
    public Page<ApplicationResource> execute(Long applicationId, Long requestingUserId, Pageable pageable) {


        if (applicationId != null) {
            Application app = applicationRepository.findById(applicationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Aplicação não encontrada: " + applicationId));


            if (!app.getCreatedById().equals(requestingUserId)) {
                throw new UnauthorizedException("Você não tem permissão para visualizar recursos desta aplicação.");
            }



            List<ApplicationResource> list = resourceRepository.findByApplicationId(applicationId);


            return new PageImpl<>(list, pageable, list.size());
        }


        return resourceRepository.findByApplicationCreatedById(requestingUserId, pageable);
    }
}
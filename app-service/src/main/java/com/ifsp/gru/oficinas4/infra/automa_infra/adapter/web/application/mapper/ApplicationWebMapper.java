package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.application.mapper;


import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.application.dto.ApplicationRequest;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.application.dto.ApplicationResponse;

import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class  ApplicationWebMapper {


    public Application toDomain(ApplicationRequest request) {
        if (request == null) return null;

        Application domain = new Application();
        domain.setName(request.getName());
        domain.setDescription(request.getDescription());
        domain.setSshUser(request.getSshUser());
        domain.setSshPassword(request.getSshPassword());
        domain.setIpAddress(request.getIpAddress());
        domain.setStatus(request.getStatus());


        return domain;
    }

    public ApplicationResponse toResponse(Application domain) {
        if (domain == null) return null;

        ApplicationResponse response = new ApplicationResponse();
        response.setId(domain.getId());
        response.setName(domain.getName());
        response.setDescription(domain.getDescription());
        response.setStatus(domain.getStatus());
        response.setSshUser(domain.getSshUser());
        response.setIpAddress(domain.getIpAddress());

        response.setCreatedAt(domain.getCreatedAt());
        response.setUpdatedAt(domain.getUpdatedAt());
        response.setLastDeployedAt(domain.getLastDeployedAt());

        if (domain.getCreatedById() != null) {
            ApplicationResponse.UserSummary summary = new ApplicationResponse.UserSummary();
            summary.setId(domain.getCreatedById());
            summary.setName(domain.getCreatedByName());
            summary.setEmail(domain.getCreatedByEmail());

            response.setCreatedBy(summary);
        }

        return response;
    }
}
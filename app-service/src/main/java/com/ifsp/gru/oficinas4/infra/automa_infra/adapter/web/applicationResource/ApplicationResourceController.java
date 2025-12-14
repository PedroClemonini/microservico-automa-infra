package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.applicationResource;

import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.applicationResource.dto.ApplicationResourceRequest;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.applicationResource.dto.ApplicationResourceResponse;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.applicationResource.mapper.ApplicationResourceWebMapper;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ApplicationResource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.applicationResource.*;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.UnauthorizedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/application-resources")
@RequiredArgsConstructor
public class ApplicationResourceController {

    private final CreateApplicationResourceUseCase createUseCase;
    private final GetApplicationResourceUseCase getUseCase;
    private final ListApplicationResourcesUseCase listUseCase;
    private final UpdateApplicationResourceUseCase updateUseCase;
    private final DeleteApplicationResourceUseCase deleteUseCase;

    private final ApplicationResourceWebMapper mapper;


    @PostMapping
    public ResponseEntity<ApplicationResourceResponse> create(
            @RequestBody @Valid ApplicationResourceRequest request,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);

        ApplicationResource created = createUseCase.execute(
                request.getApplicationId(),
                request.getResourceId(),
                userId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResourceResponse> getById(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        ApplicationResource domain = getUseCase.execute(id, userId);

        return ResponseEntity.ok(mapper.toResponse(domain));
    }


    @GetMapping
    public ResponseEntity<Page<ApplicationResourceResponse>> list(
            @RequestParam(required = false) Long applicationId,
            Pageable pageable,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        Page<ApplicationResource> page = listUseCase.execute(applicationId, userId, pageable);

        return ResponseEntity.ok(page.map(mapper::toResponse));
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ApplicationResourceResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ApplicationResourceRequest request,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);


        ApplicationResource incomingData = new ApplicationResource();
        if (request.getApplicationId() != null) {
            Application app = new Application();
            app.setId(request.getApplicationId());
            incomingData.setApplication(app);
        }
        if (request.getResourceId() != null) {
            Resource res = new Resource();
            res.setId(request.getResourceId());
            incomingData.setResources(res);
        }

        ApplicationResource updated = updateUseCase.execute(id, incomingData, userId);

        return ResponseEntity.ok(mapper.toResponse(updated));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        deleteUseCase.execute(id, userId);

        return ResponseEntity.noContent().build();
    }

    private Long extractUserIdFromAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuário não autenticado.");
        }

        try {
            return (Long) authentication.getPrincipal();
        } catch (ClassCastException e) {
            throw new UnauthorizedException("Erro interno: ID do usuário no token não é válido.");
        }
    }
}
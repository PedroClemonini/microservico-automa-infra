package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.application;

import com.ifsp.gru.oficinas4.infra.automa_infra.Configuration.JwtService;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.application.dto.ApplicationRequest;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.application.dto.ApplicationResponse;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.application.mapper.ApplicationWebMapper;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Application;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.application.*;
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
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final CreateApplicationUseCase createUseCase;
    private final GetApplicationUseCase getUseCase;
    private final ListApplicationsUseCase listUseCase;
    private final UpdateApplicationUseCase updateUseCase;
    private final DeleteApplicationUseCase deleteUseCase;
    private final UpdateApplicationStatusUseCase updateStatusUseCase;
    private final DeployApplicationUseCase deployUseCase;

    private final ApplicationWebMapper mapper;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<ApplicationResponse> create(
            @RequestBody @Valid ApplicationRequest request,
            @RequestHeader("Authorization") String authHeader,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        String token = authHeader.substring(7);
        String userName = jwtService.extractUserName(token);
        String userEmail = jwtService.extractUsername(token);

        Application domain = mapper.toDomain(request);


        Application created = createUseCase.execute(domain, userId, userName, userEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponse> getById(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        Application domain = getUseCase.execute(id, userId);

        return ResponseEntity.ok(mapper.toResponse(domain));
    }

    @GetMapping
    public ResponseEntity<Page<ApplicationResponse>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status,
            Pageable pageable,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        Page<Application> page = listUseCase.execute(name, status, userId, pageable);

        return ResponseEntity.ok(page.map(mapper::toResponse));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApplicationResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ApplicationRequest request,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        Application domain = mapper.toDomain(request);

        Application updated = updateUseCase.execute(id, domain, userId);

        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicationResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String newStatus,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        Application updated = updateStatusUseCase.execute(id, newStatus, userId);

        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @PostMapping("/{id}/deploy")
    public ResponseEntity<ApplicationResponse> deploy(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        Application applicationInProcess = deployUseCase.execute(id, userId);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(mapper.toResponse(applicationInProcess));
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
            throw new UnauthorizedException("Erro interno: Token não contém ID numérico válido.");
        }
    }
}
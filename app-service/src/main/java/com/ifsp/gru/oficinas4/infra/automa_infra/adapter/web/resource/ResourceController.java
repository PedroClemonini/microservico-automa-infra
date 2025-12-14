package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.resource;

import com.ifsp.gru.oficinas4.infra.automa_infra.Configuration.JwtService;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.resource.dto.ResourceRequest;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.resource.dto.ResourceResponse;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.resource.mapper.ResourceWebMapper;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.Resource;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resource.*;
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
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final CreateResourceUseCase createUseCase;
    private final GetResourceUseCase getUseCase;
    private final ListResourcesUseCase listUseCase;
    private final UpdateResourceUseCase updateUseCase;
    private final ToggleResourceActiveUseCase toggleActiveUseCase;
    private final DeleteResourceUseCase deleteUseCase;

    private final ResourceWebMapper mapper;
    private final JwtService jwtService;


    @PostMapping
    public ResponseEntity<ResourceResponse> create(
            @RequestBody @Valid ResourceRequest request,
            @RequestHeader("Authorization") String authHeader,
            Authentication authentication) {


        Long userId = extractUserIdFromAuth(authentication);


        String token = authHeader.substring(7); // Remove "Bearer "
        String userName = jwtService.extractUserName(token);
        String userEmail = jwtService.extractUsername(token); // O Subject é o email

        Resource domain = mapper.toDomain(request);


        Resource created = createUseCase.execute(domain, userId, userName, userEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> getById(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        Resource resource = getUseCase.execute(id, userId);
        return ResponseEntity.ok(mapper.toResponse(resource));
    }


    @GetMapping
    public ResponseEntity<Page<ResourceResponse>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) Boolean active,
            Pageable pageable,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        Page<Resource> page = listUseCase.execute(name, typeId, active, pageable, userId);
        return ResponseEntity.ok(page.map(mapper::toResponse));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ResourceRequest request,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        Resource domain = mapper.toDomain(request);
        Resource updated = updateUseCase.execute(id, domain, userId);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }


    @PatchMapping("/{id}/active")
    public ResponseEntity<ResourceResponse> toggleActive(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        Resource updated = toggleActiveUseCase.execute(id, userId);
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
            throw new UnauthorizedException("Erro interno de autenticação: Tipo de Principal inválido.");
        }
    }
}
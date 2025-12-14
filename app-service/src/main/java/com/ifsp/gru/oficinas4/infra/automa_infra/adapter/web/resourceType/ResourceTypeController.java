package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.resourceType;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.resourceType.dto.ResourceTypeRequest;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.resourceType.dto.ResourceTypeResponse;
import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.resourceType.mapper.ResourceTypeWebMapper;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.domain.ResourceType;
import com.ifsp.gru.oficinas4.infra.automa_infra.core.usecase.resourceType.*;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.UnauthorizedException;
import com.ifsp.gru.oficinas4.infra.automa_infra.Configuration.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resource-types")
@RequiredArgsConstructor
public class ResourceTypeController {

    private final CreateResourceTypeUseCase createUseCase;
    private final UpdateResourceTypeUseCase updateUseCase;
    private final GetResourceTypeUseCase getUseCase;
    private final ListResourceTypeUseCase listUseCase;
    private final DeleteResourceTypeUseCase deleteUseCase;
    private final ResourceTypeWebMapper mapper;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<ResourceType> create(
            @RequestBody @Valid ResourceTypeRequest request,
            @RequestHeader("Authorization") String authHeader,
            Authentication authentication
    ) {

        Long userId = (Long) authentication.getPrincipal();

        String token = authHeader.substring(7);
        String userName = jwtService.extractUserName(token);
        String userEmail = jwtService.extractUsername(token);


        ResourceType domainObj = mapper.toDomain(request);


        ResourceType saved = createUseCase.execute(domainObj, userId, userName, userEmail);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResourceTypeResponse> getById(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);
        ResourceType domain = getUseCase.execute(id, userId);
        return ResponseEntity.ok(mapper.toResponse(domain));
    }


    @GetMapping
    public ResponseEntity<Page<ResourceTypeResponse>> list(
            @RequestParam(required = false) String name,
            Pageable pageable,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);

        Page<ResourceType> page = listUseCase.execute(pageable, userId);

        return ResponseEntity.ok(page.map(mapper::toResponse));
    }

    // --- UPDATE ---
    @PutMapping("/{id}")
    public ResponseEntity<ResourceTypeResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ResourceTypeRequest request,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);

        ResourceType domain = mapper.toDomain(request);
        // O ID vindo do PathVariable tem prioridade
        domain.setId(id);

        // 5. Valida propriedade antes de atualizar
        ResourceType updated = updateUseCase.execute(id, domain, userId);

        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    // --- DELETE ---
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication) {

        Long userId = extractUserIdFromAuth(authentication);

        // 6. Valida propriedade e integridade (se está em uso) antes de deletar
        deleteUseCase.execute(id, userId);

        return ResponseEntity.noContent().build();
    }

    // --- MÉTODOS AUXILIARES ---

    private Long extractUserIdFromAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("Usuário não autenticado.");
        }
        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("Token inválido.");
        }
    }
}
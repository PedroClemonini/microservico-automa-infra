package com.ifsp.gru.oficinas4.infra.microservices.auth_service.adapter.web.controller;

import com.ifsp.gru.oficinas4.infra.microservices.auth_service.adapter.web.dto.UserLoginRequest;
import com.ifsp.gru.oficinas4.infra.microservices.auth_service.adapter.web.dto.UserRegisterRequest;
import com.ifsp.gru.oficinas4.infra.microservices.auth_service.adapter.web.mapper.UserWebMapper;
import com.ifsp.gru.oficinas4.infra.microservices.auth_service.core.domain.User;
import com.ifsp.gru.oficinas4.infra.microservices.auth_service.core.usecases.FindUserByEmailUseCase;
import com.ifsp.gru.oficinas4.infra.microservices.auth_service.core.usecases.RegisterUserUseCase;
import com.ifsp.gru.oficinas4.infra.microservices.auth_service.infraestructure.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUseCase;
    private final UserWebMapper mapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final FindUserByEmailUseCase findUserByEmailUseCase;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid UserRegisterRequest request) {

        User newUser = mapper.toDomain(request);
        User registeredUser = registerUseCase.execute(newUser);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(registeredUser.getEmail());


        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", registeredUser.getId());

        // 2. Gera o token PASSANDO O MAPA
        final String jwt = jwtService.generateToken(extraClaims, userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                AuthResponse.builder()
                        .token(jwt)
                        .user(mapper.toResponse(registeredUser))
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid UserLoginRequest request) {


        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );


        User userDomain = findUserByEmailUseCase.execute(request.getEmail());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // 4. Monta o Mapa com o ID
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", userDomain.getId());
        extraClaims.put("name", userDomain.getName());

        final String jwt = jwtService.generateToken(extraClaims, userDetails);

        return ResponseEntity.ok(
                AuthResponse.builder()
                        .token(jwt)
                        .user(mapper.toResponse(userDomain))
                        .build()
        );
    }
}
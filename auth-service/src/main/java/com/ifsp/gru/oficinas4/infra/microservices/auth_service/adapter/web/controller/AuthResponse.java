package com.ifsp.gru.oficinas4.infra.microservices.auth_service.adapter.web.controller;


import com.ifsp.gru.oficinas4.infra.microservices.auth_service.adapter.web.dto.UserResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;
    private UserResponse user; // Contém ID, nome, email, role
}
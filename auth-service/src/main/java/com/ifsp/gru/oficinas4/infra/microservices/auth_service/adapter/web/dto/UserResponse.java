package com.ifsp.gru.oficinas4.infra.microservices.auth_service.adapter.web.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;


}
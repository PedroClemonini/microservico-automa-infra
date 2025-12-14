package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.external.auth.dto;

import lombok.Data;


@Data
public class TokenValidationResponse {
    private boolean isValid;
    private Long userId;
    private String username;
    private String role;
}
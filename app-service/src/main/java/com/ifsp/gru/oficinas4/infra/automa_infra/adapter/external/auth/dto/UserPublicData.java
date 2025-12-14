package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.external.auth.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPublicData {
    private Long id;
    private String name;
    private String email;
    private String role;
}
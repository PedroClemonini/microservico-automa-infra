package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.web.application.dto;



import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationResponse {

    private Long id;
    private String name;
    private String description;
    private String status;
    private String sshUser;
    private String ipAddress;
    private UserSummary createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastDeployedAt;

    @Data
    public static class UserSummary {
        private Long id;
        private String name;
        private String email;
    }
}
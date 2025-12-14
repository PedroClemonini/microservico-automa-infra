package com.ifsp.gru.oficinas4.infra.automa_infra.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Application {

    private Long id;
    private String name;
    private String description;
    private String status;
    private Long createdById;
    private String createdByName;
    private String createdByEmail;
    private String sshUser;
    private String sshPassword;
    private String ipAddress;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    private LocalDateTime lastDeployedAt;

}

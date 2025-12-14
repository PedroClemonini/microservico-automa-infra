package com.ifsp.gru.oficinas4.infra.automa_infra.core.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceType {

    private Long id;

    private String name;

    private String description;
    private Long createdById;
    private String createdByName;
    private String createdByEmail;

    // --- TIMESTAMPS ---
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

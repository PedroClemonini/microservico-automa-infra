package com.ifsp.gru.oficinas4.infra.automa_infra.core.domain;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResource {

    private Long id;

    private Application application;

    private Resource resources;

    private LocalDateTime addedAt = LocalDateTime.now();

}

package com.ifsp.gru.oficinas4.infra.automa_infra.core.domain;
import com.ifsp.gru.oficinas4.infra.automa_infra.Configuration.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    private Long id;

    private ResourceType resourceType;

    private String name;

    private String description;

    private String version;

    private List<String> codeSnippet;
    private Boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private Long createdById;
    private String createdByName;
    private String createdByEmail;



}

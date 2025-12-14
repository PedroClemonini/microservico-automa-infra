package com.ifsp.gru.oficinas4.infra.microservices.auth_service.core.port;

import com.ifsp.gru.oficinas4.infra.microservices.auth_service.core.domain.User;

import java.util.Optional;

public interface UserRepositoryPort {

    User save(User user);
    Optional<User> findById(Long id);
    void deleteById(Long id);
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
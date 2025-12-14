package com.ifsp.gru.oficinas4.infra.microservices.auth_service.core.usecases;

import com.ifsp.gru.oficinas4.infra.microservices.auth_service.core.domain.User;
import com.ifsp.gru.oficinas4.infra.microservices.auth_service.core.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class FindUserByEmailUseCase {

    private final UserRepositoryPort userRepository;

    public User execute(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado com o email: " + email));
    }
}
package com.ifsp.gru.oficinas4.infra.microservices.auth_service.core.usecases;
import com.ifsp.gru.oficinas4.infra.microservices.auth_service.exception.AuthenticationException;
import com.ifsp.gru.oficinas4.infra.microservices.auth_service.core.domain.User;
import com.ifsp.gru.oficinas4.infra.microservices.auth_service.core.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User execute(String email, String rawPassword) {
        // 1. Busca o usuário pelo email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Credenciais inválidas."));

        // 2. Verifica a senha
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new AuthenticationException("Credenciais inválidas.");
        }

        // 3. Regra de Segurança: Remove a senha do objeto de domínio antes de devolvê-lo
        // Isso garante que a senha criptografada não vaze para camadas superiores (Controller/API)
        user.setPassword(null);

        return user;
    }
}
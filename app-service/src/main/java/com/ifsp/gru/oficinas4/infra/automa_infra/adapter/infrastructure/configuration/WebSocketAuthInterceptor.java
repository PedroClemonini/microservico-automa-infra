package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.infrastructure.configuration;

import com.ifsp.gru.oficinas4.infra.automa_infra.Configuration.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;


@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("=== INTERCEPTOR CHAMADO ===");

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            log.info("Comando STOMP: {}", accessor.getCommand());
            log.info("Headers: {}", accessor.toNativeHeaderMap());
        }

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("🔐 Processando CONNECT...");

            String authHeader = accessor.getFirstNativeHeader("Authorization");
            log.info("Authorization header: {}", authHeader != null ? "presente" : "ausente");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                try {
                    String jwt = authHeader.substring(7);
                    log.info("Token JWT extraído (primeiros 20 chars): {}", jwt.substring(0, Math.min(20, jwt.length())));

                    // Valida o token
                    if (!jwtService.isTokenExpired(jwt)) {
                        String userEmail = jwtService.extractUsername(jwt);
                        Long userId = jwtService.extractUserId(jwt);

                        log.info("✅ Token válido! UserID: {}, Email: {}", userId, userEmail);

                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                userId,
                                null,
                                Collections.emptyList()
                        );

                        accessor.setUser(auth);
                        log.info("✅ Usuário setado no accessor: {}", userId);

                    } else {
                        log.error("❌ Token expirado!");
                        throw new IllegalArgumentException("Token expirado");
                    }
                } catch (Exception e) {
                    log.error("❌ Erro na autenticação WebSocket: {}", e.getMessage(), e);
                    throw new IllegalArgumentException("Token inválido: " + e.getMessage());
                }
            } else {
                log.warn("⚠️ Header Authorization não encontrado ou inválido");
                throw new IllegalArgumentException("Token não fornecido ou inválido");
            }
        }

        log.info("=== INTERCEPTOR FINALIZADO ===");
        return message;
    }
}
package com.ifsp.gru.oficinas4.infra.microservices.auth_service.infraestructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // --- CORREÇÃO DO 403 AQUI ---
        // Se a requisição NÃO tiver token (ex: Login/Register),
        // ou o header estiver errado, NÓS DEVEMOS IGNORAR E DEIXAR PASSAR.
        // O SecurityConfig lá na frente vai decidir se essa rota pública pode entrar sem token.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; // <--- IMPORTANTE: O return impede que o código continue tentando ler um token nulo
        }
        // -----------------------------

        try {
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt); // Extrai email do token

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Se o token for inválido, expirado ou malformado, não lance erro 403 aqui.
            // Apenas logue e deixe seguir. O Security vai barrar depois se a rota for privada.
            System.out.println("Erro na validação do Token JWT: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
package com.ifsp.gru.oficinas4.infra.automa_infra.adapter.external.auth;


import com.ifsp.gru.oficinas4.infra.automa_infra.adapter.external.auth.dto.UserPublicData;
import com.ifsp.gru.oficinas4.infra.automa_infra.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class AuthServiceClient {

    private final RestTemplate restTemplate;
    private final String authServiceUrl;

    public AuthServiceClient(RestTemplate restTemplate, @Value("${auth.service.url}") String authServiceUrl) {
        this.restTemplate = restTemplate;
        this.authServiceUrl = authServiceUrl;
    }

    public UserPublicData findUserById(Long userId) {
        String url = authServiceUrl + "/api/internal/users/" + userId;

        try {
            return restTemplate.getForObject(url, UserPublicData.class);
        } catch (HttpClientErrorException.NotFound ex) {
            log.error("Usuário ID {} não encontrado no Auth Service.", userId);
            throw new ResourceNotFoundException("Usuário não encontrado.");
        } catch (Exception ex) {
            log.error("Erro ao comunicar com Auth Service: {}", ex.getMessage());
            throw new RuntimeException("Falha ao buscar dados de usuário externo.");
        }
    }
}
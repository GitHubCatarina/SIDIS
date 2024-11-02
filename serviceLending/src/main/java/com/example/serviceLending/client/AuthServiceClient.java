package com.example.serviceLending.client;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class AuthServiceClient {

    private final RestTemplate restTemplate;

    public AuthServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getUserRoles(String token) {
        String url = "http://localhost:8081/api/auth/roles"; // Alterar para endpoint que retorna roles baseado no token

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Adiciona o token aqui
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String[]> response = restTemplate.exchange(url, HttpMethod.GET, entity, String[].class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return Arrays.asList(response.getBody());
        } else {
            throw new RuntimeException("Failed to retrieve roles: " + response.getStatusCode());
        }
    }

}

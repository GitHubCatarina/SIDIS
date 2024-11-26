package com.example.serviceTopQuery.topManagement.api;

import com.example.serviceTopQuery.topManagement.model.Lending;
import com.example.serviceTopQuery.topManagement.services.TopServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Tag(name = "Lendings", description = "Endpoints for managing Lendings")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/lendings")
public class TopController {
    @Autowired
    private JwtDecoder jwtDecoder;

    private final RestTemplate restTemplate;
    @Value("${server.port}")
    private String serverPort;

    private final TopServiceImpl topService;
    private final LendingViewMapper lendingViewMapper;

    private boolean hasPermission(List<String> roles, String... allowedRoles) {
        for (String role : allowedRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/top-books")
    public ResponseEntity<List<LentBookView>> getTopBooks() {

        List<LentBookView> topBooks = topService.getTopBooks();
        return ResponseEntity.ok(topBooks);
    }

    @GetMapping("/top-readers")
    public ResponseEntity<List<LendingReaderView>> getTopReaders() {

        List<LendingReaderView> topReaders = topService.getTopReaders();
        return ResponseEntity.ok(topReaders);
    }

    //Auth
    private List<String> getRolesFromToken(String token) {
        Jwt jwt = jwtDecoder.decode(token);

        // Obter o claim que contém as roles como uma string
        String rolesClaim = jwt.getClaimAsString("roles");

        if (rolesClaim == null || rolesClaim.isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.asList(rolesClaim.split(","));
    }


    @Retryable(
            value = { Exception.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000) // Intervalo de 2 segundos entre tentativas
    )
    public void sincronizarComOutraInstancia(String authorization, Lending lending) {
        if (lending.getId() == null) {
            throw new IllegalArgumentException("Lending ID não deve ser nulo");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorization);
        headers.set("Content-Type", "application/json");

        try {
            System.out.println("Sincronização (criação ou atualização) bem-sucedida com a outra instância.");

        } catch (Exception e) {
            System.out.println("Erro na sincronização. Tentando novamente... " + e.getMessage());
            throw e; // Relança a exceção para ativar nova tentativa
        }
    }
}



package com.example.serviceLendingCom.lendingManagementCom.api;

import com.example.serviceLendingCom.lendingManagementCom.dto.LendingDTO;
import com.example.serviceLendingCom.lendingManagementCom.services.CreateLendingRequest;
import com.example.serviceLendingCom.lendingManagementCom.services.LendingEventProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.serviceLendingCom.lendingManagementCom.model.Lending;
import com.example.serviceLendingCom.lendingManagementCom.services.EditLendingRequest;
import com.example.serviceLendingCom.lendingManagementCom.services.LendingServiceImpl;
import com.example.serviceLendingCom.exceptions.NotFoundException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Tag(name = "Lendings", description = "Endpoints for managing Lendings")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/lendings")
public class LendingController {
    @Autowired
    private JwtDecoder jwtDecoder;

    @Value("${server.port}")
    private String serverPort;

    private final LendingServiceImpl lendingService;
    private final LendingViewMapper lendingViewMapper;
    @Autowired
    private LendingEventProducer lendingEventProducer;

    private boolean hasPermission(List<String> roles, String... allowedRoles) {
        for (String role : allowedRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Operation(summary = "Creates a new Lending")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LendingView> createLending(@Valid @RequestBody final CreateLendingRequest resource) {
        Lending lending = lendingService.createLending(resource);

        final var newbarUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(lending.getId().toString())
                .build().toUri();


        System.out.println("User criado, enviando evento para sincronização...");

        // Converte User para UserDTO
        LendingDTO lendingDTO = new LendingDTO().toDTO(lending);

        // Manda para I2
        lendingEventProducer.sendLendingCreatedEvent(lendingDTO);


        return ResponseEntity.created(newbarUri).eTag(Long.toString(lending.getVersion()))
                .body(lendingViewMapper.toLendingView(lending));
    }



    @Operation(summary = "Return a Book")
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LendingView> returnBook(
            @Valid @RequestBody final EditLendingRequest resource,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader(value = "If-Match", required = false) String ifMatchHeader) {

        String token = authorization.replace("Bearer ", "");

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Extract version from If-Match header
        Long version = getVersionFromIfMatchHeader(ifMatchHeader);

        Lending lending = lendingService.returnBook(resource, version);

        if (lending.getId() == null) {
            throw new IllegalStateException("Lending ID must not be null for synchronization.");
        }

        // Produzir evento de devolução
        LendingDTO lendingDTO = new LendingDTO().toDTO(lending);
        lendingDTO.setReturned(true); // Indicar que o empréstimo foi devolvido
        lendingEventProducer.sendLendingReturnedEvent(lendingDTO);

        final var newbarUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(lending.getId().toString())
                .build()
                .toUri();

        return ResponseEntity.created(newbarUri).eTag(Long.toString(lending.getVersion()))
                .body(lendingViewMapper.toLendingView(lending));
    }



    @Operation(summary = "Deletes a Lending by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteLending(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization) {

        String token = authorization.replace("Bearer ", ""); // Extrai o token do header

        // Verificar permissões
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) { // Apenas LIBRARIAN e ADMIN podem eliminar
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Verifica se o Lending existe antes de tentar eliminar
        if (!lendingService.existsById(id)) {
            throw new NotFoundException("Lending com ID " + id + " não encontrado.");
        }

        // Eliminar o Lending
        lendingService.deleteById(id);

        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }


    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader == null || ifMatchHeader.isEmpty()) {
            // Retorna 0 quando o cabeçalho não for fornecido
            return 0L;
        }

        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
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


}



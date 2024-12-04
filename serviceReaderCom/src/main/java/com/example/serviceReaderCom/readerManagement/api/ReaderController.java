package com.example.serviceReaderCom.readerManagement.api;

import com.example.serviceReaderCom.readerManagement.dto.ReaderDTO;
import com.example.serviceReaderCom.readerManagement.services.ReaderEventProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.serviceReaderCom.client.LendingServiceClient;
import com.example.serviceReaderCom.exceptions.NotFoundException;
import com.example.serviceReaderCom.fileStorage.UploadFileResponse;
import com.example.serviceReaderCom.readerManagement.model.Reader;
import com.example.serviceReaderCom.readerManagement.model.ReaderPhoto;
import com.example.serviceReaderCom.readerManagement.services.EditReaderRequest;
import com.example.serviceReaderCom.readerManagement.services.ReaderServiceImpl;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Collections;
import java.util.Arrays;
import java.util.Optional;
import java.util.Base64;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Tag(name = "Readers", description = "Endpoints for managing Readers")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/readers")
public class ReaderController {
    @Autowired
    private JwtDecoder jwtDecoder;


    private static final String IF_MATCH = "If-Match";
    private final ReaderServiceImpl readerService;
    private final ReaderViewMapper readerViewMapper;
    private final ReaderProfileViewMapper readerProfileViewMapper ;
    private final ReaderLentsViewMapper readerLentsViewMapper;
    private final LendingServiceClient lendingServiceClient;
    private final RestTemplate restTemplate;
    @Autowired
    private ReaderEventProducer readerEventProducer;

    private boolean hasPermission(List<String> roles, String... allowedRoles) {
        for (String role : allowedRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Operation(summary = "Creates a new Reader")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReaderView> createReader(@Valid @RequestPart("reader") final EditReaderRequest resource,
                                                   @RequestPart(value = "photo", required = false) MultipartFile photo, @RequestHeader("Authorization") String authorization) {


        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // 1. Executa a criação do Reader na base de dados local
        Reader reader = readerService.createReader(resource, photo);

        String photoBase64 = null;
        try {
            photoBase64 = (photo != null) ? Base64.getEncoder().encodeToString(photo.getBytes()) : null;
        } catch (IOException e) {
            e.printStackTrace(); // Log do erro
        }

        // 2. Envia o evento para o RabbitMQ
        ReaderDTO readerDTO = new ReaderDTO(
                reader.getId(),
                reader.getReaderCode(),
                reader.getName(),
                reader.getEmail(),
                reader.getDateOfBirth(),
                reader.getAge(),
                reader.getPhoneNumber(),
                reader.getGDBRConsent(),
                reader.getInterests(),
                reader.getFunnyQuote()
        );


        // Envia a mensagem para o RabbitMQ
        readerEventProducer.sendReaderCreatedEvent(readerDTO);

        final var newbarUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(reader.getId().toString())
                .build().toUri();
        return ResponseEntity.created(newbarUri).eTag(Long.toString(reader.getVersion()))
                .body(readerViewMapper.toReaderView(reader));
    }

    @Operation(summary = "Uploads a cover of a Reader")
    @PostMapping("/{readerId}/photo")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UploadFileResponse> uploadFile(@PathVariable("readerId") final String readerId,
                                                         @RequestParam("file") final MultipartFile file, @RequestHeader("Authorization") String authorization) throws URISyntaxException {


        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        // 1. Executa a criação do Reader na base de dados local
        final UploadFileResponse up = readerService.doUploadFile(readerId, file);

        Long readerIdLong = Long.parseLong(readerId);

        // 2. Reader atualizado para sincronização
        Optional<Reader> optionalReader = readerService.getReaderByIdWithQuote(readerIdLong);
        if (optionalReader.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 se o leitor não for encontrado
        }
        Reader reader = optionalReader.get();

        String photoBase64 = null;
        try {
            photoBase64 = (file != null) ? Base64.getEncoder().encodeToString(file.getBytes()) : null;
        } catch (IOException e) {
            e.printStackTrace(); // Log do erro
        }


        final var newbarUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(reader.getId().toString())
                .build().toUri();

        return ResponseEntity.created(new URI(up.getFileDownloadUri())).body(up);

    }

    @Operation(summary = "Fully replaces an existing reader. If the specified id does not exist does nothing and returns 400.")
    @PutMapping(path = "{readerId}")
    public ResponseEntity<ReaderView> updateReader(final WebRequest request,
                                                   @PathVariable("readerId") Long id,
                                                   @Valid @RequestBody final EditReaderRequest resource,
                                                   @RequestHeader("Authorization") String authorization) {

        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 1. Executa a atualização do Reader na base de dados local
        Reader reader = readerService.updateReader(id, resource, getVersionFromIfMatchHeader(ifMatchValue));


        final var newbarUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(reader.getId().toString())
                .build().toUri();

        return ResponseEntity.ok().eTag(Long.toString(reader.getVersion())).body(readerViewMapper.toReaderView(reader));
    }


    @Operation(summary = "Partially updates an existing reader")
    @PatchMapping(path = "{readerId}")
    public ResponseEntity<ReaderView> partialUpdateReader(final WebRequest request,
                                                      @PathVariable("readerId") Long id,
                                                      @Valid @RequestBody final EditReaderRequest resource, @RequestHeader("Authorization") String authorization) {


        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }



        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Reader reader = readerService.partialUpdateReader(id, resource, getVersionFromIfMatchHeader(ifMatchValue));

        return ResponseEntity.ok().eTag(Long.toString(reader.getVersion())).body(readerViewMapper.toReaderView(reader));
    }

    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }

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
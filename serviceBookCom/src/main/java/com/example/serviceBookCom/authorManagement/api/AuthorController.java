package com.example.serviceBookCom.authorManagement.api;

import com.example.serviceBookCom.authorManagement.dto.AuthorDTO;
import com.example.serviceBookCom.authorManagement.model.Author;
import com.example.serviceBookCom.authorManagement.services.AuthorServiceImpl;
import com.example.serviceBookCom.authorManagement.services.EditAuthorRequest;
import com.example.serviceBookCom.bookManagement.api.BookViewMapper;
import com.example.serviceBookCom.fileStorage.UploadFileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Tag(name = "Authors", description = "Endpoints for managing Authors")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/authors")
public class AuthorController {
    @Autowired
    private JwtDecoder jwtDecoder;

    private static final String IF_MATCH = "If-Match";
    private final AuthorServiceImpl authorService;
    private final AuthorViewMapper authorViewMapper;
    private final BookViewMapper bookViewMapper;
    private final AuthorLentsViewMapper authorLentsViewMapper;

    private final RestTemplate restTemplate;

    private boolean hasPermission(List<String> roles, String... allowedRoles) {
        for (String role : allowedRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Operation(summary = "Creates a new Author")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorView> createAuthor(@Valid @RequestPart("author") final EditAuthorRequest resource,
                                                   @RequestPart(value = "authorPhoto", required = false) MultipartFile authorPhoto,
                                                   @RequestHeader("Authorization") String authorization) {


        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final var author = authorService.createAuthor(resource, authorPhoto);
        final var newbarUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(author.getId().toString())
                .build().toUri();

        return ResponseEntity.created(newbarUri).eTag(Long.toString(author.getVersion()))
                .body(authorViewMapper.toAuthorView(author));
    }

    @Operation(summary = "Uploads a author photo")
    @PostMapping("/{authorId}/photo")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UploadFileResponse> uploadFile(@PathVariable("authorId") final String authorId,
                                                         @RequestParam("file") final MultipartFile file,
                                                         @RequestHeader("Authorization") String authorization) throws URISyntaxException {

        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }



        final UploadFileResponse up = authorService.doUploadFile(authorId, file);
        return ResponseEntity.created(new URI(up.getFileDownloadUri())).body(up);
    }

    @Operation(summary = "Fully replaces an existing author.")
    @PutMapping(path = "{authorId}")
    public ResponseEntity<AuthorView> updateAuthor(final WebRequest request,
                                                   @PathVariable("authorId") Long id,
                                                   @Valid @RequestBody final EditAuthorRequest resource,
                                                   @RequestHeader("Authorization") String authorization) {


        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }



        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Author author = authorService.updateAuthor(id, resource, getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(author.getVersion())).body(authorViewMapper.toAuthorView(author));
    }

    @Operation(summary = "Partially updates an existing author")
    @PatchMapping(path = "{authorId}")
    public ResponseEntity<AuthorView> partialUpdateAuthor(final WebRequest request,
                                                          @PathVariable("authorId") Long id,
                                                          @Valid @RequestBody final EditAuthorRequest resource,
                                                          @RequestHeader("Authorization") String authorization) {


        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Author author = authorService.partialUpdateAuthor(id, resource, getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(author.getVersion())).body(authorViewMapper.toAuthorView(author));
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





    private AuthorDTO toAuthorDTO(Author author) {
        // Converte a foto do autor para um ID ou base64 (se necessário)
        Long authorPhotoId = author.getAuthorPhoto() != null ? author.getAuthorPhoto().getId() : null;

        return new AuthorDTO(
                author.getId(),
                author.getName(),
                author.getShortBio(),
                author.getLents(),
                authorPhotoId
        );
    }

}
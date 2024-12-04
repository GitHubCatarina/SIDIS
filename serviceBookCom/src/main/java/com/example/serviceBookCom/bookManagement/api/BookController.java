package com.example.serviceBookCom.bookManagement.api;

import com.example.serviceBookCom.bookManagement.dto.BookDTO;
import com.example.serviceBookCom.bookManagement.dto.SyncBookDTO;
import com.example.serviceBookCom.bookManagement.model.Book;
import com.example.serviceBookCom.bookManagement.model.BookCover;
import com.example.serviceBookCom.bookManagement.repositories.BookRepository;
import com.example.serviceBookCom.bookManagement.services.BookEventProducer;
import com.example.serviceBookCom.bookManagement.services.BookServiceImpl;
import com.example.serviceBookCom.bookManagement.services.CreateBookRequest;
import com.example.serviceBookCom.bookManagement.services.EditBookRequest;
import com.example.serviceBookCom.exceptions.NotFoundException;
import com.example.serviceBookCom.fileStorage.UploadFileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
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
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Books", description = "Endpoints for managing Books")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "api/books")
public class BookController {
    @Autowired
    private JwtDecoder jwtDecoder;
    private static final String IF_MATCH = "If-Match";
    private final BookServiceImpl bookService;
    //private final LendingServiceImpl lendingService;
    private final BookViewMapper bookViewMapper;
    private final GenreViewMapper genreViewMapper;
    //private final LentBookViewMapper lentBookViewMapper;
    private final BookRepository bookRepository;

    private final RestTemplate restTemplate;
    @Autowired
    private BookEventProducer bookEventProducer;


    private boolean hasPermission(List<String> roles, String... allowedRoles) {
        for (String role : allowedRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Operation(summary = "Uploads a cover of a Book")
    @PostMapping("/{bookId}/cover")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UploadFileResponse> uploadFile(@PathVariable("bookId") final String bookId, @RequestParam("file") final MultipartFile file, @RequestHeader("Authorization") String authorization) throws URISyntaxException {

        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        // Service I1
        final UploadFileResponse up = bookService.doUploadFile(bookId, file);


        // Manda para I2
        return ResponseEntity.created(new URI(up.getFileDownloadUri())).body(up);
    }

    @Operation(summary = "Creates a new Book")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookView> createBook(@Valid @RequestPart("book") final CreateBookRequest resource,
                                               @RequestPart(value = "cover", required = false) MultipartFile coverPhoto,
                                               @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Service I1
        final var book = bookService.createBook(resource, coverPhoto);
        final var newbarUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(book.getId().toString())
                .build().toUri();

        // Manda para I2
        System.out.println("Livro criado, enviando evento para sincronização...");

        // Envia o evento para o RabbitMQ
        try {
            bookEventProducer.sendBookCreatedEvent(resource, coverPhoto);  // Ajustando a chamada aqui
        } catch (Exception e) {
            // Tratar o erro ou logar a exceção
            System.out.println("Erro ao enviar evento para o RabbitMQ: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.created(newbarUri).eTag(Long.toString(book.getVersion()))
                .body(bookViewMapper.toCreateBookView(book));
    }


    @Operation(summary = "Fully replaces an existing book")
    @PutMapping(path = "{bookId}")
    public ResponseEntity<BookView> updateBook(final WebRequest request, @PathVariable("bookId") Long id, @Valid @RequestBody final EditBookRequest resource, @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        // Service I1
        Book book = bookService.updateBook(id, resource, getVersionFromIfMatchHeader(ifMatchValue));

        // Manda para I2

        return ResponseEntity.ok().eTag(Long.toString(book.getVersion())).body(bookViewMapper.toBookView(book));
    }

    @Operation(summary = "Partially updates an existing book")
    @PatchMapping(path = "{bookId}")
    public ResponseEntity<BookView> partialUpdateBook(final WebRequest request, @PathVariable("bookId") Long id, @Valid @RequestBody final EditBookRequest resource, @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Service I1
        Book book = bookService.partialUpdateBook(id, resource, getVersionFromIfMatchHeader(ifMatchValue));

        // Manda para I2

        return ResponseEntity.ok().eTag(Long.toString(book.getVersion())).body(bookViewMapper.toBookView(book));
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


    private BookDTO toBookDTO(Book book) {
        // Converte o objeto Genre para seu nome (ou outro identificador relevante)
        String genreName = book.getGenre() != null ? book.getGenre().getName() : null;

        // Converte a lista de BookAuthor para uma lista de Strings com os nomes dos autores
        List<String> authorNames = book.getBookAuthors() != null ?
                book.getBookAuthors().stream()
                        .filter(bookAuthor -> bookAuthor.getAuthor() != null)  // Verifica se o autor não é nulo
                        .map(bookAuthor -> bookAuthor.getAuthor().getName())
                        .collect(Collectors.toList())
                : Collections.emptyList();


        String coverUrl = book.getCover() != null && book.getCover().getImage() != null ?
                Base64.getEncoder().encodeToString(book.getCover().getImage()) : null;

        return new BookDTO(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getGenre(),
                book.getDescription(),
                authorNames,
                coverUrl
        );
    }


}
package com.example.serviceBook.bookManagement.api;

import com.example.serviceBook.bookManagement.dto.BookDTO;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.serviceBook.bookManagement.model.BookCover;
import com.example.serviceBook.bookManagement.repositories.BookRepository;
import com.example.serviceBook.bookManagement.services.BookServiceImpl;
import com.example.serviceBook.exceptions.NotFoundException;
import com.example.serviceBook.bookManagement.model.Book;
import com.example.serviceBook.bookManagement.services.CreateBookRequest;
import com.example.serviceBook.bookManagement.services.EditBookRequest;
import com.example.serviceBook.fileStorage.UploadFileResponse;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import com.example.serviceBook.bookManagement.sync.SyncRequest;
import org.springframework.web.client.RestTemplate;
import com.example.serviceBook.bookManagement.model.BookAuthor;
import java.util.Base64;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
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
    @Value("${webhook.url}")
    private String webhookUrl;


    private boolean hasPermission(List<String> roles, String... allowedRoles) {
        for (String role : allowedRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Operation(summary = "Gets book by isbn")
    @GetMapping("/{bookIsbn}")
    public ResponseEntity<BookView> getBook(@PathVariable("bookIsbn") String isbn, @RequestHeader("Authorization") String authorization) {

        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        final var book = bookService.getBook(isbn).orElseThrow(() -> new NotFoundException(Book.class, isbn));
        return ResponseEntity.ok().eTag(Long.toString(book.getVersion())).body(bookViewMapper.toBookView(book));
    }


    @Operation(summary = "Gets all Books")
    @GetMapping
    public List<BookView> getBooks(
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "100", required = false) int size,
            @RequestHeader("Authorization") String authorization) {

        String token = authorization.replace("Bearer ", ""); // Token from header

        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            throw new RuntimeException("Unauthorized access");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> booksPage;

        if (genre != null && title != null && author != null) {
            booksPage = bookService.getBooksByTitleAndGenreAndAuthor(genre, title, author, pageable);
        } else if (genre != null && title == null && author == null) {
            booksPage = bookService.getBooksByGenre(genre, pageable);
        } else if (genre == null && title != null && author == null) {
            booksPage = bookService.getBooksByTitle(title, pageable);
        } else if (genre == null && title == null && author != null) {
            booksPage = bookService.getBooksByAuthor(author, pageable);
        } else {
            booksPage = bookService.getBooks(pageable);
        }

        return booksPage.map(bookViewMapper::toBookView).getContent();
    }


    @Operation(summary = "Gets top 5 Genres by book number")
    @GetMapping("/top-genres")
    public Iterable<GenreView> getTopGenres(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            throw new RuntimeException("Unauthorized access");
        }


        return genreViewMapper.toGenreView(bookService.getTopGenres(), bookService.getAllBooks());
    }

    @Operation(summary = "Gets top 5 Books lent")
    @GetMapping("/top-books")
    @ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = LentBookView.class))) })
    public ResponseEntity<List<LentBookView>> getTopBooks() {
        List<LentBookView> topBooks = bookService.getTopBooks();
        return ResponseEntity.ok(topBooks);
    }

    @Operation(summary = "Downloads a cover of a book by id")
    @GetMapping("/{bookId}/cover")
    public ResponseEntity<Resource> getBookCover(@PathVariable("bookId") final String bookId, final HttpServletRequest request, @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            throw new RuntimeException("Unauthorized access");
        }

        BookCover bookCover = bookService.getBookCover(bookId);
        final Resource resource = new ByteArrayResource(bookCover.getImage());
        String contentType = bookCover.getContentType();
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
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



        final UploadFileResponse up = bookService.doUploadFile(bookId, file);
        return ResponseEntity.created(new URI(up.getFileDownloadUri())).body(up);
    }

    @Operation(summary = "Creates a new Book")
    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookView> createBook(@Valid @RequestPart("book") final CreateBookRequest resource, @RequestPart(value = "cover", required = false) MultipartFile coverPhoto, @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        final var book = bookService.createBook(resource, coverPhoto);
        final var newbarUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(book.getId().toString())
                .build().toUri();

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

        Book book = bookService.updateBook(id, resource, getVersionFromIfMatchHeader(ifMatchValue));
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
        sincronizarComOutraInstancia(book);

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

    @Retryable(
            value = { Exception.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 2000) // Intervalo de 2 segundos entre tentativas
    )
    public void sincronizarComOutraInstancia(Book book) {

        BookDTO bookDTO = toBookDTO(book);
        if (book.getId() == null) {
            throw new IllegalArgumentException("O bookname do book não deve ser nulo");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Criar o SyncRequest com os dados do Book
        SyncRequest syncRequest = new SyncRequest();
        syncRequest.setId(book.getId());
        syncRequest.setResource(bookDTO);
        syncRequest.setDesiredVersion(0);

        // Enviar o SyncRequest
        HttpEntity<SyncRequest> requestEntity = new HttpEntity<>(syncRequest, headers);

        try {
            restTemplate.exchange(
                    webhookUrl + "/webhook/sync",
                    HttpMethod.PUT,
                    requestEntity,
                    Void.class
            );
            System.out.println("Sincronização (criação ou atualização) bem-sucedida com a outra instância.");
        } catch (Exception e) {
            System.out.println("Erro na sincronização. Tentando novamente... " + e.getMessage());
            throw e; // Relança a exceção para ativar nova tentativa
        }
    }
    private BookDTO toBookDTO(Book book) {
        // Converte o objeto Genre para seu nome (ou outro identificador relevante)
        String genreName = book.getGenre() != null ? book.getGenre().getName() : null;

        // Converte a lista de BookAuthor para uma lista de Strings com os nomes dos autores
        List<String> authorNames = book.getBookAuthors() != null ?
                book.getBookAuthors().stream()
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
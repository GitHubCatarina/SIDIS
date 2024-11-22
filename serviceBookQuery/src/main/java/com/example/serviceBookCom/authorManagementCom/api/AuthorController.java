package com.example.serviceBookCom.authorManagementCom.api;

import com.example.serviceBookCom.authorManagementCom.dto.AuthorDTO;
import com.example.serviceBookCom.authorManagementCom.model.Author;
import com.example.serviceBookCom.authorManagementCom.model.AuthorPhoto;
import com.example.serviceBookCom.authorManagementCom.services.AuthorServiceImpl;
import com.example.serviceBookCom.bookManagementCom.api.BookView;
import com.example.serviceBookCom.bookManagementCom.api.BookViewMapper;
import com.example.serviceBookCom.bookManagementCom.model.Book;
import com.example.serviceBookCom.bookManagementCom.model.BookAuthor;
import com.example.serviceBookCom.bookManagementCom.services.BookServiceImpl;
import com.example.serviceBookCom.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
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

    private final AuthorServiceImpl authorService;
    private final BookServiceImpl bookService;
    private final AuthorViewMapper authorViewMapper;
    private final BookViewMapper bookViewMapper;

    private boolean hasPermission(List<String> roles, String... allowedRoles) {
        for (String role : allowedRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Operation(summary = "Gets the Top 5 Authors by Book Count")
    @GetMapping("/top-authors")
    public ResponseEntity<List<AuthorView>> getTopAuthors(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<AuthorView> topAuthors = authorService.getTopAuthors();
        return ResponseEntity.ok(topAuthors);
    }

    @Operation(summary = "Gets all Authors")
    @GetMapping
    public List<AuthorView> getAuthors(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "100", required = false) int size,
            @RequestHeader("Authorization") String authorization) {


        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }



        Pageable pageable = PageRequest.of(page, size);
        Page<Author> authorPage = authorService.getAuthors(pageable);
        return authorPage.map(authorViewMapper::toAuthorView).getContent();
    }

    @Operation(summary = "Gets a specific Author by id")
    @GetMapping("/{authorId}")
    public ResponseEntity<AuthorView> getAuthor(@PathVariable("authorId") Long id, @RequestHeader("Authorization") String authorization) {

        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        final var author = authorService.getAuthorsById(id).orElseThrow(() -> new NotFoundException(Author.class, id));
        return ResponseEntity.ok().eTag(Long.toString(author.getVersion())).body(authorViewMapper.toAuthorView(author));
    }

    @Operation(summary = "Gets a specific Author by name")
    @GetMapping("/name")
    public Iterable<AuthorView> getAuthors(@RequestParam String name, @RequestHeader("Authorization") String authorization) {


        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }



        return authorViewMapper.toAuthorView(authorService.getAuthorsByName(name));
    }

    @Operation(summary = "Gets the co-authors of an author and their respective books")
    @GetMapping("/{authorId}/co-authors")
    public List<BookView> getAuthorCoAuthors(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "100", required = false) int size,
            @PathVariable("authorId") Long authorId,
            @RequestHeader("Authorization") String authorization) {


        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }


        Pageable pageable = PageRequest.of(page, size);
        List<BookAuthor> bookAuthors = bookService.getBookAuthorsByAuthorId(authorId);
        List<Book> booksList = new ArrayList<>();

        for (BookAuthor bookAuthor : bookAuthors) {
            if (bookAuthor.getBook().getBookAuthors().size() > 1) {
                booksList.add(bookAuthor.getBook());
            }
        }

        int start = Math.min((int) pageable.getOffset(), booksList.size());
        int end = Math.min((start + pageable.getPageSize()), booksList.size());
        List<Book> paginatedBooks = booksList.subList(start, end);

        Page<Book> booksPage = new PageImpl<>(paginatedBooks, pageable, booksList.size());
        return booksPage.map(bookViewMapper::toBookView).getContent();
    }
    /*
    @Operation(summary = "Gets the top-5 authors")
    @GetMapping("/top-authors")
    @RolesAllowed({Role.LIBRARIAN, Role.ADMIN, Role.READER})
    @ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = AuthorView.class))) })
    public Iterable<AuthorLentsView> getTop5Authors() {
        List<Author> allAuthors = new ArrayList<>();
        int totalPages = authorService.getTotalPages();
        for (int i = 0; i < totalPages; i++) {
            Pageable pageable = PageRequest.of(i, 5);
            Page<Author> authorsPage = authorService.getAuthors(pageable);
            allAuthors.addAll(authorsPage.getContent());
        }
        for (Author author : allAuthors) {
            List<BookAuthor> bookAuthors = bookService.getBookAuthorsByAuthorId(author.getId());
            int totalLents = 0;
            for (BookAuthor bookAuthor : bookAuthors) {
                List<Lending> lending = lendingService.getLentBook(bookAuthor.getId());
                totalLents += lending.size();
            }
            author.setLents(totalLents);
        }
        allAuthors.sort((a1, a2) -> Integer.compare(a2.getLents(), a1.getLents()));
        return authorLentsViewMapper.toAuthorLentsView(allAuthors.subList(0, Math.min(5, allAuthors.size())));
    }

     */

    @Operation(summary = "Gets the books from a specific Author by its id")
    @GetMapping("/{authorId}/books")
    public List<BookView> getAuthorBooks(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "100", required = false) int size,
            @PathVariable("authorId") Long authorId,
            @RequestHeader("Authorization") String authorization) {


        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }



        Pageable pageable = PageRequest.of(page, size);
        List<BookAuthor> bookAuthors = bookService.getBookAuthorsByAuthorId(authorId);
        List<Book> booksList = new ArrayList<>();

        for (BookAuthor bookAuthor : bookAuthors) {
            booksList.add(bookAuthor.getBook());
        }

        int start = Math.min((int) pageable.getOffset(), booksList.size());
        int end = Math.min((start + pageable.getPageSize()), booksList.size());
        List<Book> paginatedBooks = booksList.subList(start, end);

        Page<Book> booksPage = new PageImpl<>(paginatedBooks, pageable, booksList.size());
        return booksPage.map(bookViewMapper::toBookView).getContent();
    }


    @Operation(summary = "Downloads a photo of an author by id")
    @GetMapping("/{authorId}/photo")
    public ResponseEntity<Resource> getBookCover(@PathVariable("authorId") final String authorId, @RequestHeader("Authorization") String authorization) {

        String token = authorization.replace("Bearer ", ""); // Token from header

        // Roles from AuthService
        List<String> roles = getRolesFromToken(token);

        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }



        AuthorPhoto authorPhoto = authorService.getAuthorPhoto(authorId);
        final Resource resource = new ByteArrayResource(authorPhoto.getImage());
        String contentType = authorPhoto.getContentType();
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
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
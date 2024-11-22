package com.example.serviceLendingCom.lendingManagementCom.api;

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

    private boolean hasPermission(List<String> roles, String... allowedRoles) {
        for (String role : allowedRoles) {
            if (roles.contains(role)) {
                return true;
            }
        }
        return false;
    }

    @Operation(summary = "Gets all Lendings")
    @GetMapping
    @ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LendingView.class)))})
    public List<LendingView> getLendingsPage(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "100", required = false) int size,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Lending> lendingsPage = lendingService.getLendings(pageable);
        return lendingsPage.map(lendingViewMapper::toLendingView).getContent();
    }

    @Operation(summary = "Gets a specific Lending")
    @GetMapping("/{lendingId}")
    public ResponseEntity<LendingView> getLending(@PathVariable("lendingId") Long id,
                                                  @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        final var lending = lendingService.getLending(id).orElseThrow(() -> new NotFoundException(Lending.class, id));

        return ResponseEntity.ok().eTag(Long.toString(lending.getVersion())).body(lendingViewMapper.toLendingView(lending));
    }

    @Operation(summary = "Gets a list of overdue lending sorted by their tardiness")
    @GetMapping("/overdue")
    public List<LendingView> getOverdue(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "100", required = false) int size,
            @RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<Lending> overdueLendingsPage = lendingService.getOverdueLendings(pageable);
        return overdueLendingsPage.map(lendingViewMapper::toLendingView).getContent();
    }

    @Operation(summary = "Gets average lending duration")
    @GetMapping("/average")
    public double getAverageLendingDuration(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return lendingService.getAverageLendingDuration();
    }

    /*
    @Operation(summary = "Gets the number of lendings per month for the last 12 months, broken down by genre")
    @GetMapping("/lending-genre/{genreId}")
    public Map<Integer, Long> getAveragePerGenreInMonth(@PathVariable("genreId") Long genreId) {
        final var genre = genreService.getGenreById(genreId).orElseThrow(() -> new NotFoundException(Author.class, genreId));
        return lendingService.numberOfLendingsPerMonthByGenre(genre);
    }

    @Operation(summary = "Gets the average number of lending per genre of a certain month\n")
    @GetMapping("/average-per-genre/{date}")
    public double getAveragePerGenreInMonth(@PathVariable("date") LocalDate date) {
        int numberOfGenres = genreService.getGenres().size();
        return lendingService.AveragePerGenreInMonth(date, numberOfGenres);
    }

     */

    @Operation(summary = "Gets average lending duration per book")
    @GetMapping("/average-per-book")
    public Iterable<LendingAvgPerBookView> getAverageLendingDurationPerBook(@RequestHeader("Authorization") String authorization) {
        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return lendingService.getAverageLendingDurationPerBook();
    }
 /*
    @Operation(summary = "Gets average lending duration per genre per month for a certain period")
    @GetMapping("/average-duration-per-genre")
    public Iterable<LendingAvgPerGenrePerMonthView> getAverageLendingDurationPerGenrePerMonth(
            @RequestParam("startDate") LocalDate startDate, @RequestParam("endDate") LocalDate endDate) {
        return lendingService.getAverageLendingDurationPerGenrePerMonth(startDate, endDate);
    }


    @Operation(summary = "Creates a new Lending")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LendingView> createLending(@Valid @RequestBody final CreateLendingRequest resource) {
        Lending lending = lendingService.createLending(resource);

        final var newbarUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(lending.getId().toString())
                .build().toUri();

        //syncAuthor
        SyncRequest syncRequest = new SyncRequest(lending.getId(), "return");
        sendSyncWebhook(syncRequest);
        //.

        return ResponseEntity.created(newbarUri).eTag(Long.toString(lending.getVersion()))
                .body(lendingViewMapper.toLendingView(lending));
    }

     */

    @Operation(summary = "Return a Book")
    @PostMapping("/return")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<LendingView> returnBook(
            @Valid @RequestBody final EditLendingRequest resource,
            @RequestHeader("Authorization") String authorization,
            @RequestHeader(value = "If-Match", required = false) String ifMatchHeader) {

        String token = authorization.replace("Bearer ", ""); // Token from header

        // Check permissions
        List<String> roles = getRolesFromToken(token);
        if (!hasPermission(roles, "LIBRARIAN", "ADMIN", "READER")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        // Extract version from If-Match header
        Long version = getVersionFromIfMatchHeader(ifMatchHeader);

        // Service I1
        Lending lending = lendingService.returnBook(resource, version);
        if (lending.getId() == null) {
            throw new IllegalStateException("Lending ID must not be null for synchronization.");
        }


        // Manda para I2

        // Prepare the URI for the response
        final var newbarUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .pathSegment(lending.getId().toString())
                .build()
                .toUri();


        return ResponseEntity.created(newbarUri).eTag(Long.toString(lending.getVersion()))
                .body(lendingViewMapper.toLendingView(lending));
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



    //Backend Endpoints
    @GetMapping("/top-books")
    public ResponseEntity<List<LentBookView>> getTopBooks() {

        List<LentBookView> topBooks = lendingService.getTopBooks();
        return ResponseEntity.ok(topBooks);
    }

    @GetMapping("/top-readers")
    public ResponseEntity<List<LendingReaderView>> getTopReaders() {

        List<LendingReaderView> topReaders = lendingService.getTopReaders();
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


}



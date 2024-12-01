package com.example.serviceBookCom.bookManagement.dto;

import com.example.serviceBookCom.bookManagement.model.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class SyncBookDTO {

    // Getters e Setters
    private Long id;
    private String isbn;
    private String title;
    private String description;
    private Long genreId;
    private List<String> bookAuthors;
    private BookCoverDTO cover;

    public SyncBookDTO() {
    }

    public SyncBookDTO(Long id, String isbn, String title, Long genreId, String description, List<String> bookAuthors, BookCoverDTO cover) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.genreId = genreId;
        this.description = description;
        this.bookAuthors = bookAuthors;
        this.cover = cover;
    }

    @Override
    public String toString() {
        return "SyncBookCoverDTO{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", genreId='" + genreId + '\'' +
                ", description='" + description + '\'' +
                ", bookAuthors=" + bookAuthors +
                ", cover=" + cover +
                '}';
    }

    // Atualização do método toDTO
    public static SyncBookDTO toDTO(Book book) {

        // Extrair o ID do género (genre)
        Long genreId = book.getGenre() != null ? book.getGenre().getId() : null;

        List<String> authorNames = book.getBookAuthors() != null
                ? book.getBookAuthors().stream()
                .filter(bookAuthor -> bookAuthor.getAuthor() != null)
                .map(bookAuthor -> bookAuthor.getAuthor().getName())
                .collect(Collectors.toList())
                : Collections.emptyList();

        // Converter a capa do livro para BookCoverDTO
        BookCoverDTO coverDTO = book.getCover() != null
                ? new BookCoverDTO(
                book.getCover().getCoverUrl(),
                Base64.getEncoder().encodeToString(book.getCover().getImage()),  // Codifica a imagem em base64
                book.getCover().getContentType())
                : null;

        return new SyncBookDTO(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                genreId,
                book.getDescription(),
                authorNames,
                coverDTO
        );
    }
}

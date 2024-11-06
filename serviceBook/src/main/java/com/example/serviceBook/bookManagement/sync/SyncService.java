package com.example.serviceBook.bookManagement.sync;

import com.example.serviceBook.bookManagement.dto.BookDTO;
import com.example.serviceBook.bookManagement.model.Book;
import com.example.serviceBook.bookManagement.model.BookCover;
import com.example.serviceBook.bookManagement.model.BookAuthor;
import com.example.serviceBook.authorManagement.model.Author;
import com.example.serviceBook.bookManagement.repositories.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.serviceBook.bookManagement.model.Genre;
import com.example.serviceBook.bookManagement.repositories.GenreRepository;
import java.util.stream.Collectors;

@Service
public class SyncService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    public SyncService(BookRepository bookRepository, GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public void syncData(SyncRequest syncRequest) {
        System.out.println("Sync Request: " + syncRequest);

        Long bookId = syncRequest.getId();
        if (bookId == null) {
            throw new IllegalArgumentException("BookID não pode ser nulo");
        }

        Book existingBook = bookRepository.findById(bookId).orElse(null);

        if (existingBook != null) {
            updateBook(existingBook, syncRequest.getResource());
            System.out.println("Recurso atualizado com sucesso.");
        } else {
            createBook(syncRequest.getResource());
            System.out.println("Recurso criado com sucesso.");
        }
    }

    private void updateBook(Book existingBook, BookDTO resourceDTO) {
        if (resourceDTO.getTitle() != null) {
            existingBook.setTitle(resourceDTO.getTitle());
        }
        if (resourceDTO.getIsbn() != null) {
            existingBook.setIsbn(resourceDTO.getIsbn());
        }
        if (resourceDTO.getGenreId() != null) {
            Genre genre = genreRepository.findById(resourceDTO.getGenreId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid genre ID"));
            existingBook.setGenre(genre);
        }
        if (resourceDTO.getDescription() != null) {
            existingBook.setDescription(resourceDTO.getDescription());
        }
        if (resourceDTO.getBookAuthors() != null && !resourceDTO.getBookAuthors().isEmpty()) {
            // Atualiza os autores do livro, se houverem
            existingBook.getBookAuthors().clear();
            existingBook.getBookAuthors().addAll(
                    resourceDTO.getBookAuthors().stream()
                            .map(authorName -> new BookAuthor(existingBook, new Author(authorName, "Bio do autor")))  // Criando autor com nome
                            .collect(Collectors.toList())
            );
        }
        if (resourceDTO.getCoverUrl() != null) {
            existingBook.setCover(new BookCover(resourceDTO.getCoverUrl(), null));
        }

        bookRepository.save(existingBook);
    }

    private void createBook(BookDTO resourceDTO) {
        Book newBook = new Book();
        newBook.setIsbn(resourceDTO.getIsbn());
        newBook.setTitle(resourceDTO.getTitle());
        if (resourceDTO.getGenreId() != null) {
            Genre genre = genreRepository.findById(resourceDTO.getGenreId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid genre ID"));
            newBook.setGenre(genre);
        }
        newBook.setDescription(resourceDTO.getDescription());

        if (resourceDTO.getBookAuthors() != null && !resourceDTO.getBookAuthors().isEmpty()) {
            newBook.getBookAuthors().addAll(
                    resourceDTO.getBookAuthors().stream()
                            .map(authorName -> new BookAuthor(newBook, new Author(authorName, "Bio do autor")))  // Criando autor com nome
                            .collect(Collectors.toList())
            );
        }

        if (resourceDTO.getCoverUrl() != null) {
            newBook.setCover(new BookCover(resourceDTO.getCoverUrl(), null));
        }

        bookRepository.save(newBook);
    }

}

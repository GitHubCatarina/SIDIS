package com.example.serviceBook.bookManagement.repositories;

import com.example.serviceBook.bookManagement.model.Book;
import com.example.serviceBook.bookManagement.model.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        // Adicionando livros de exemplo para testes
        Genre genre1 = new Genre();
        genre1.setName("Fiction");

        Genre genre2 = new Genre();
        genre2.setName("Science");

        Book book1 = new Book();
        book1.setIsbn("123-456-789");
        book1.setTitle("Test Book 1");
        book1.setGenre(genre1);
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setIsbn("987-654-321");
        book2.setTitle("Test Book 2");
        book2.setGenre(genre2);
        bookRepository.save(book2);
    }

    @Test
    void findBookByIsbn() {
        Optional<Book> foundBook = bookRepository.findBookByIsbn("123-456-789");
        assertTrue(foundBook.isPresent(), "O livro deve estar presente");
        assertEquals("123-456-789", foundBook.get().getIsbn(), "O ISBN deve corresponder");
    }

    @Test
    void findBookById() {
        Book book = bookRepository.findAll().get(0);
        Optional<Book> foundBook = bookRepository.findBookById(book.getId());
        assertTrue(foundBook.isPresent(), "O livro deve estar presente");
        assertEquals(book.getId(), foundBook.get().getId(), "O ID deve corresponder ao do livro salvo");
    }

    @Test
    void findTopGenres() {
        List<Genre> topGenres = bookRepository.findTopGenres();
        assertFalse(topGenres.isEmpty(), "A lista de gêneros mais populares não deve estar vazia");
        assertEquals("Fiction", topGenres.get(0).getName(), "O gênero mais popular deve ser 'Fiction'");
    }
}

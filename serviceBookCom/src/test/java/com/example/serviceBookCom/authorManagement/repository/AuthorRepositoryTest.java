package com.example.serviceBookCom.authorManagement.repository;

import com.example.serviceBookCom.authorManagement.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuthorRepositoryTest {

    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        // Inserindo dados de exemplo
        Author author1 = new Author();
        author1.setName("John Doe");
        authorRepository.save(author1);

        Author author2 = new Author();
        author2.setName("Jane Smith");
        authorRepository.save(author2);
    }

    @Test
    void findAuthorByName() {
        Optional<Author> foundAuthor = authorRepository.findAuthorByName("John Doe");
        assertTrue(foundAuthor.isPresent(), "O autor deve estar presente");
        assertEquals("John Doe", foundAuthor.get().getName(), "O nome do autor deve corresponder");
    }

    @Test
    void findAuthorById() {
        Author author = authorRepository.findAll().get(0); // Pegando um autor do banco de dados
        Optional<Author> foundAuthor = authorRepository.findAuthorById(author.getId());
        assertTrue(foundAuthor.isPresent(), "O autor deve estar presente");
        assertEquals(author.getId(), foundAuthor.get().getId(), "O ID do autor deve corresponder");
    }
}

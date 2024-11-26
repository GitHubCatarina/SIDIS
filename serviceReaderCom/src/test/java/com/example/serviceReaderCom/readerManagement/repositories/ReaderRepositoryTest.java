package com.example.serviceReaderCom.readerManagement.repositories;

import com.example.serviceReaderCom.readerManagement.model.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class ReaderRepositoryTest {

    @Autowired
    private ReaderRepository readerRepository;

    @BeforeEach
    void setUp() {
        // Adicione dados de exemplo para os testes
        Reader reader1 = new Reader();
        reader1.setName("John Doe");
        reader1.setEmail("john.doe@example.com");
        readerRepository.save(reader1);

        Reader reader2 = new Reader();
        reader2.setName("Jane Smith");
        reader2.setEmail("jane.smith@example.com");
        readerRepository.save(reader2);
    }

    @Test
    void findReaderByName() {
        Optional<Reader> foundReader = readerRepository.findReaderByName("John Doe");
        assertTrue(foundReader.isPresent(), "O leitor deve estar presente");
        assertEquals("John Doe", foundReader.get().getName(), "O nome do leitor deve ser John Doe");
    }

    @Test
    void findReaderById() {
        Reader reader = readerRepository.findAll().get(0); // Pega o primeiro leitor da lista
        Optional<Reader> foundReader = readerRepository.findReaderById(reader.getId());
        assertTrue(foundReader.isPresent(), "O leitor deve estar presente");
        assertEquals(reader.getId(), foundReader.get().getId(), "O ID deve corresponder ao do leitor salvo");
    }

    @Test
    void findByEmail() {
        Optional<Reader> foundReader = readerRepository.findByEmail("jane.smith@example.com");
        assertTrue(foundReader.isPresent(), "O leitor deve estar presente");
        assertEquals("jane.smith@example.com", foundReader.get().getEmail(), "O e-mail do leitor deve ser jane.smith@example.com");
    }

    @Test
    void findMaxReaderId() {
        int maxId = readerRepository.findMaxReaderId();
        assertTrue(maxId > 0, "O ID máximo deve ser maior que 0");
    }
}

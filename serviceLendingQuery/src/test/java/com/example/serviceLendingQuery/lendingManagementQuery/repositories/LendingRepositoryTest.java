package com.example.serviceLendingQuery.lendingManagementQuery.repositories;

import com.example.serviceLendingQuery.lendingManagementQuery.model.Lending;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LendingRepositoryTest {

    @Autowired
    private LendingRepository lendingRepository;

    @BeforeEach
    void setUp() {
        // Carregue dados de exemplo no banco de dados H2
        Lending lending = new Lending();
        lending.setReaderId(1L);
        lending.setBookId(101L);
        lending.setReturned(false);
        lending.setLendDate(LocalDate.now().minusDays(10));
        lending.setLimitDate(LocalDate.now().minusDays(5));
        lendingRepository.save(lending);
    }

    @Test
    void countLentBooksByReaderId() {
        int count = lendingRepository.countLentBooksByReaderId(1L);
        assertEquals(1, count, "Deveria haver 1 livro emprestado para o readerId 1.");
    }

    @Test
    void findOverdueBooksByReaderId() {
        List<Lending> overdueBooks = lendingRepository.findOverdueBooksByReaderId(1L);
        assertFalse(overdueBooks.isEmpty(), "Deveria haver pelo menos um livro vencido para o readerId 1.");
    }

    @Test
    void findByLendingCode() {
        Lending lending = lendingRepository.findByLendingCode("2024/1").orElse(null);
        assertNotNull(lending, "Deveria ter encontrado um empréstimo com o código especificado.");
    }

}

package com.example.serviceReaderCom.readerManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.serviceReaderCom.readerManagement.model.Reader;

import java.util.Optional;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {
/*
    @Query("SELECT r FROM Reader r " +
            "WHERE EXISTS (SELECT l FROM Lending l WHERE l.readerId = r.id AND l.lendDate >= :startDate AND l.lendDate <= :endDate) " +
            "ORDER BY (SELECT COUNT(l) FROM Lending l WHERE l.readerId = r.id AND l.lendDate >= :startDate AND l.lendDate <= :endDate) DESC")
    List<Reader> findTopReaders(Pageable pageable, LocalDate startDate, LocalDate endDate);

    @Query("SELECT r FROM Reader r " +
            "JOIN Lending l ON l.readerId = r.id JOIN Book b ON b.id = l.bookId " +
            "WHERE l.lendDate >= :startDate " +
            "AND l.lendDate <= :endDate " +
            "AND b.genre = :genre ORDER BY (SELECT COUNT(l) " +
            "FROM Lending l " +
            "WHERE l.readerId = r.id) DESC")
    List<Reader> findTopReadersPerGenre(Pageable pageable, Genre genre, LocalDate startDate, LocalDate endDate);

    @Query("SELECT COUNT(l.id) FROM Lending l " +
            "WHERE l.lendDate >= :startDate " +
            "AND l.lendDate <= :endDate AND l.readerId = :readerId")
    int getMonthlyLending(Long readerId, LocalDate startDate, LocalDate endDate);
 */

    @Query("SELECT r FROM Reader r WHERE r.name = :name")
    Optional<Reader> findReaderByName(String name);

    @Query("SELECT r FROM Reader r WHERE r.id = :id")
    Optional<Reader> findReaderById(Long id);

    @Query("SELECT r FROM Reader r WHERE r.email = :email")
    Optional<Reader> findByEmail(String email);

    @Query("SELECT MAX(r.id) FROM Reader r")
    int findMaxReaderId();
}


package com.example.serviceTopQuery.topManagement.repositories;

import com.example.serviceTopQuery.topManagement.model.Lending;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public interface LendingRepository extends JpaRepository<Lending, Long> {
    @Query("SELECT COUNT(l) FROM Lending l WHERE l.readerId = :readerId AND l.returned = FALSE ")
    int countLentBooksByReaderId(Long readerId);

    @Query("SELECT l FROM Lending l WHERE l.readerId = :readerId AND l.returned = false AND l.limitDate < CURRENT_DATE")
    List<Lending> findOverdueBooksByReaderId(Long readerId);

    @Query("SELECT l FROM Lending l WHERE l.lendingCode = :lendingCode")
    Optional<Lending> findByLendingCode(String lendingCode);

    @Query("SELECT MAX(l.id) FROM Lending l")
    int findMaxLendingId();


    @Query("SELECT l FROM Lending l WHERE l.readerId = :readerId AND l.bookId = :bookId AND l.returned = FALSE")
    List<Lending> findAlreadyLendedBook(Long readerId, Long bookId);

    @Query("SELECT l FROM Lending l WHERE l.bookId = :id")
    List<Lending> getLentBook(Long id);

    @Query("SELECT l.bookId, COUNT(l.bookId) as lendCount " +
            "FROM Lending l " +
            "GROUP BY l.bookId " +
            "ORDER BY lendCount DESC " +
            "LIMIT 5")
    List<Object[]> findTopBookIds();

    @Query("SELECT l.readerId, COUNT(l.readerId) AS lendingCount " +
            "FROM Lending l " +
            "GROUP BY l.readerId " +
            "ORDER BY lendingCount DESC")
    List<Object[]> findTopReaders(Pageable pageable);
}

package com.example.serviceLendingCom.lendingManagementCom.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.serviceLendingCom.lendingManagementCom.model.Lending;

import java.util.List;
import java.util.Optional;

@Repository
public interface LendingRepository extends JpaRepository<Lending, Long> {
    @Query("SELECT COUNT(l) FROM Lending l WHERE l.readerId = :readerId AND l.returned = FALSE ")
    int countLentBooksByReaderId(Long readerId);

    @Query("SELECT l FROM Lending l WHERE l.readerId = :readerId AND l.returned = false AND l.limitDate < CURRENT_DATE")
    List<Lending> findOverdueBooksByReaderId(Long readerId);

    @Query("SELECT l FROM Lending l WHERE l.lendingCode = :lendingCode")
    Optional<Lending> findByLendingCode(String lendingCode);

    @Query("SELECT MAX(l.id) FROM Lending l")
    int findMaxLendingId();

    @Query("SELECT l FROM Lending l WHERE l.returned = false AND l.limitDate < CURRENT_DATE()")
    Page<Lending> findOverdueLendings(Pageable pageable);

    @Query("SELECT l FROM Lending l WHERE l.readerId = :readerId AND l.bookId = :bookId AND l.returned = FALSE")
    List<Lending> findAlreadyLendedBook(Long readerId, Long bookId);

    @Query("SELECT l FROM Lending l WHERE l.bookId = :id")
    List<Lending> getLentBook(Long id);
    /*
    @Query("SELECT CASE " +
            "WHEN COUNT(l.id) = 0 THEN 0 " +
            "ELSE COALESCE(COUNT(l.id) * 1.0 / :numberOfGenres, 0) " +
            "END " +
            "FROM Lending l " +
            "JOIN Book b ON l.bookId = b.id " +
            "WHERE MONTH(l.lendDate) = MONTH(:date) AND YEAR(l.lendDate) = YEAR(:date)")
    double averagePerGenreInMonth(@Param("date") LocalDate date, int numberOfGenres);

    @Query("SELECT MONTH(l.lendDate) AS month, COUNT(l.id) AS count " +
            "FROM Lending l " +
            "JOIN Book b ON l.bookId = b.id " +
            "WHERE b.genre = :genre AND l.lendDate >= DATEADD(MONTH, -11, CURRENT_DATE()) " +
            "GROUP BY MONTH(l.lendDate) " +
            "ORDER BY MONTH(l.lendDate)")
    List<Object[]> numberOfLendingsPerMonthByGenre(@Param("genre") Genre genre);
     */

    @Query(value = "SELECT l.bookId, AVG(DATEDIFF(l.returnedDate, l.lendDate)) AS averageDuration FROM Lending l WHERE l.returned = TRUE GROUP BY l.bookId", nativeQuery = true)
    List<Object[]> findAverageLendingDurationPerBook();



    @Query("SELECT l.bookId, COUNT(l.bookId) as lendCount " +
            "FROM Lending l " +
            "GROUP BY l.bookId " +
            "ORDER BY lendCount DESC " +
            "LIMIT 5")
    List<Object[]> findTopBookIds();
/*
    @Query("SELECT b.genre.id, AVG(TIMESTAMPDIFF(DAY, l.lendDate, l.returnedDate)) AS averageDuration " +
            "FROM Lending l " +
            "JOIN Book b ON l.bookId = b.id " +
            "WHERE l.returned = TRUE " +
            "AND l.lendDate BETWEEN :startDate AND :endDate " +
            "GROUP BY b.genre.id, MONTH(l.lendDate), YEAR(l.lendDate)")
    List<Object[]> findAverageLendingDurationPerGenrePerMonth(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

 */

    @Query("SELECT l.readerId, COUNT(l.readerId) AS lendingCount " +
            "FROM Lending l " +
            "GROUP BY l.readerId " +
            "ORDER BY lendingCount DESC")
    List<Object[]> findTopReaders(Pageable pageable);
}

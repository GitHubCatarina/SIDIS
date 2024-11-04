package com.example.serviceBook.bookManagement.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.serviceBook.bookManagement.model.BookAuthor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM BookAuthor ba WHERE ba.book.id = ?1")
    void deleteByBookId(Long bookId);

    @Query("SELECT ba FROM BookAuthor ba JOIN ba.author a WHERE a.id = :authorId")
    List<BookAuthor> getAuthorBooks(@Param("authorId") Long authorId);

    @Query("SELECT ba.author, COUNT(ba.book) as bookCount " +
            "FROM BookAuthor ba " +
            "GROUP BY ba.author " +
            "ORDER BY bookCount DESC")
    List<Object[]> findTopAuthorsByBookCount(Pageable pageable);

}

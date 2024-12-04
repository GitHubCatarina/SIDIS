package com.example.serviceBookQuery.bookManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.serviceBookQuery.bookManagement.model.Book;
import com.example.serviceBookQuery.bookManagement.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.isbn = :isbn")
    Optional<Book> findBookByIsbn(String isbn);

    @Query("SELECT b FROM Book b WHERE b.id = :id")
    Optional<Book> findBookById(Long id);

    @Query(value = "SELECT g FROM Genre g JOIN Book b ON g.id = b.genre.id GROUP BY g.id, g.name ORDER BY COUNT(b.id) DESC LIMIT 5")
    List<Genre> findTopGenres();

    boolean existsByIsbn(String isbn);
}

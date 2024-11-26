package com.example.serviceBookCom.bookManagement.repositories;

import com.example.serviceBookCom.bookManagement.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT g FROM Genre g WHERE g.id = :id")
    Optional<Genre> findGenreById(Long id);
}

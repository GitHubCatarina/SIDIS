package com.example.serviceLendingCom.lendingManagementCom.repositories;

import com.example.serviceLendingCom.lendingManagementCom.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT g FROM Genre g WHERE g.id = :id")
    Optional<Genre> findGenreById(Long id);
}

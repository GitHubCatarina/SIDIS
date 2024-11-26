package com.example.serviceBookQuery.authorManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.serviceBookQuery.authorManagement.model.Author;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {



    @Query("SELECT a FROM Author a WHERE a.name = :name")
    Optional<Author> findAuthorByName(String name);

    @Query("SELECT a FROM Author a WHERE a.id = :id")
    Optional<Author> findAuthorById(Long id);
}

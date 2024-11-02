package com.example.serviceBook.bookManagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.serviceBook.bookManagement.model.BookCover;

@Repository
public interface BookCoverRepository extends JpaRepository<BookCover, Long> {
}

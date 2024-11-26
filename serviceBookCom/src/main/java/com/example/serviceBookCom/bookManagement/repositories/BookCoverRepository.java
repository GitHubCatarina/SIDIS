package com.example.serviceBookCom.bookManagement.repositories;

import com.example.serviceBookCom.bookManagement.model.BookCover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCoverRepository extends JpaRepository<BookCover, Long> {
}

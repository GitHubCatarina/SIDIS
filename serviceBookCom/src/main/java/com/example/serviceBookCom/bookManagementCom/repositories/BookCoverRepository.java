package com.example.serviceBookCom.bookManagementCom.repositories;

import com.example.serviceBookCom.bookManagementCom.model.BookCover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCoverRepository extends JpaRepository<BookCover, Long> {
}

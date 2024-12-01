package com.example.serviceLendingCom.lendingManagementCom.repositories;

import com.example.serviceLendingCom.lendingManagementCom.model.BookCover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCoverRepository extends JpaRepository<BookCover, Long> {
}

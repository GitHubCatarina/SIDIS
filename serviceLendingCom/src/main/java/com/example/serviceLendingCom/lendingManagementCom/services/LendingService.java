package com.example.serviceLendingCom.lendingManagementCom.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.serviceLendingCom.lendingManagementCom.api.LendingAvgPerBookView;
import com.example.serviceLendingCom.lendingManagementCom.api.LendingReaderView;
import com.example.serviceLendingCom.lendingManagementCom.api.LentBookView;
import com.example.serviceLendingCom.lendingManagementCom.model.Lending;
import java.util.List;

import java.util.Optional;

public interface LendingService {
    Optional<Lending> getLending(Long lendingId);
    Page<Lending> getLendings(Pageable pageable);
    Iterable<Lending> getAllLendings();
    List<Lending> getLentBook(Long bookId);
    Page<Lending> getOverdueLendings(Pageable pageable);
    double getAverageLendingDuration();
    Iterable<LendingAvgPerBookView> getAverageLendingDurationPerBook();
   Lending returnBook(EditLendingRequest resource, Long version);

}

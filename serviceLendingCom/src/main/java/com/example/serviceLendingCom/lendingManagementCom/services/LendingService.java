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
    //double AveragePerGenreInMonth(LocalDate date, int numberOfGenres);
    //Map<Integer, Long> numberOfLendingsPerMonthByGenre(Genre genre);
    Iterable<LendingAvgPerBookView> getAverageLendingDurationPerBook();
    //Iterable<LendingAvgPerGenrePerMonthView> getAverageLendingDurationPerGenrePerMonth(LocalDate startDate, LocalDate endDate);
    //Lending createLending(CreateLendingRequest resource);
    Lending returnBook(EditLendingRequest resource, Long version);

    List<LentBookView> getTopBooks();
    List<LendingReaderView> getTopReaders();
}

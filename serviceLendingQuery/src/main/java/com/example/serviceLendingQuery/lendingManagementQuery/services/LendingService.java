package com.example.serviceLendingQuery.lendingManagementQuery.services;

import com.example.serviceLendingQuery.lendingManagementQuery.api.LendingAvgPerBookView;
import com.example.serviceLendingQuery.lendingManagementQuery.api.LendingReaderView;
import com.example.serviceLendingQuery.lendingManagementQuery.api.LentBookView;
import com.example.serviceLendingQuery.lendingManagementQuery.model.Lending;
import com.example.serviceLendingQuery.lendingManagementQuery.services.EditLendingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

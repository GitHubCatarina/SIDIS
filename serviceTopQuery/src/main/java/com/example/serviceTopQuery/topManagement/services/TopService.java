package com.example.serviceTopQuery.topManagement.services;

import com.example.serviceTopQuery.topManagement.api.LendingReaderView;
import com.example.serviceTopQuery.topManagement.api.LentBookView;

import java.util.List;

public interface TopService {
    //Optional<Lending> getLending(Long lendingId);
    //Page<Lending> getLendings(Pageable pageable);
    //Iterable<Lending> getAllLendings();
    //List<Lending> getLentBook(Long bookId);
    //Page<Lending> getOverdueLendings(Pageable pageable);
    //double getAverageLendingDuration();
    //double AveragePerGenreInMonth(LocalDate date, int numberOfGenres);
    //Map<Integer, Long> numberOfLendingsPerMonthByGenre(Genre genre);
    //Iterable<LendingAvgPerBookView> getAverageLendingDurationPerBook();
    //Iterable<LendingAvgPerGenrePerMonthView> getAverageLendingDurationPerGenrePerMonth(LocalDate startDate, LocalDate endDate);
    //Lending createLending(CreateLendingRequest resource);
    //Lending returnBook(EditLendingRequest resource, Long version);

    List<LentBookView> getTopBooks();
    List<LendingReaderView> getTopReaders();
}

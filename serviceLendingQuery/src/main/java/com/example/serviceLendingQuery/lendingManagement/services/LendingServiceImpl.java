package com.example.serviceLendingQuery.lendingManagement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.serviceLendingQuery.lendingManagement.api.*;
import com.example.serviceLendingQuery.exceptions.NotFoundException;
import com.example.serviceLendingQuery.lendingManagement.model.Lending;
import com.example.serviceLendingQuery.lendingManagement.repositories.LendingRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LendingServiceImpl implements LendingService {

    private final LendingRepository lendingRepository;
    private final LendingAvgPerBookViewMapper lendingAvgPerBookViewMapper;
    private final LendingAvgPerGenrePerMonthViewMapper lendingAvgPerGenrePerMonthViewMapper;

    @Value("${lending.days}")
    private int daysOfLending ;
    @Value("${lending.lateFee}")
    private float lateFee;

    @Autowired
    public LendingServiceImpl(LendingRepository lendingRepository, LendingAvgPerBookViewMapper lendingAvgPerBookViewMapper, LendingAvgPerGenrePerMonthViewMapper lendingAvgPerGenrePerMonthViewMapper) {
        this.lendingRepository = lendingRepository;
        this.lendingAvgPerBookViewMapper = lendingAvgPerBookViewMapper;
        this.lendingAvgPerGenrePerMonthViewMapper = lendingAvgPerGenrePerMonthViewMapper;

    }

    public Optional<Lending> getLending(final Long lendingId) {
        return lendingRepository.findById(lendingId);
    }

    public Page<Lending> getLendings(Pageable pageable) {
        return lendingRepository.findAll(pageable);
    }

    public Iterable<Lending> getAllLendings() {
        return lendingRepository.findAll();
    }

    public List<Lending> getLentBook(Long bookId) {
        return lendingRepository.getLentBook(bookId);
    }

    public Page<Lending> getOverdueLendings(Pageable pageable) {
        return lendingRepository.findOverdueLendings(pageable);
    }

    public double getAverageLendingDuration() {
        List<Lending> allLendings = lendingRepository.findAll();
        long totalDuration = 0;
        int lendingCount = allLendings.size();

        for (Lending lending : allLendings) {
            long duration = ChronoUnit.DAYS.between(lending.getLendDate(), LocalDate.now());
            totalDuration += duration;
        }

        // Calcula a média diretamente como double sem formatação adicional
        double averageDuration = lendingCount > 0 ? (double) totalDuration / lendingCount : 0;

        return averageDuration;
    }
/*
    public double AveragePerGenreInMonth(LocalDate date, int numberOfGenres){
        return lendingRepository.averagePerGenreInMonth(date, numberOfGenres);
    }

    public Map<Integer, Long> numberOfLendingsPerMonthByGenre(Genre genre) {
        List<Object[]> results = lendingRepository.numberOfLendingsPerMonthByGenre(genre);
        Map<Integer, Long> lendingsPerMonth = new HashMap<>();
        for (Object[] result : results) {
            Integer month = (Integer) result[0];
            Long count = (Long) result[1];
            lendingsPerMonth.put(month, count);
        }
        return lendingsPerMonth;
    }

 */

    public Iterable<LendingAvgPerBookView> getAverageLendingDurationPerBook() {
        List<Object[]> results = lendingRepository.findAverageLendingDurationPerBook();
        return lendingAvgPerBookViewMapper.toLendingAvgPerBookViewList(results);
    }

    /*
    public Iterable<LendingAvgPerGenrePerMonthView> getAverageLendingDurationPerGenrePerMonth(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = lendingRepository.findAverageLendingDurationPerGenrePerMonth(startDate, endDate);
        return lendingAvgPerGenrePerMonthViewMapper.toLendingAvgPerGenrePerMonthViewList(results);
    }


    public Lending createLending(final CreateLendingRequest resource) {
        Optional<Book> book = bookRepository.findById(resource.getBookId());
        if (book.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] Book not found with ID: " + resource.getBookId());
        }

        Optional<Reader> reader = readerRepository.findById(resource.getReaderId());
        if (reader.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] Reader not found with ID: " + resource.getReaderId());
        }

        if (!lendingRepository.findOverdueBooksByReaderId(reader.get().getId()).isEmpty()) {
            throw new IllegalArgumentException("[ERROR] Cannot lend book. Reader has overdue books.");
        }

        if (!lendingRepository.findAlreadyLendedBook(reader.get().getId(), book.get().getId()).isEmpty()) {
            throw new IllegalArgumentException("[ERROR] Cannot lend book. Reader has already taken this book.");
        }

        if (lendingRepository.countLentBooksByReaderId(reader.get().getId()) >= 3) {
            throw new IllegalArgumentException("[ERROR] Cannot lend book. Reader has already lent 3 books.");
        }
        LocalDate limitDate = LocalDate.now().plusDays(daysOfLending);

        Lending lending = new Lending();
        String lendingCode = java.time.Year.now().getValue() + "/" + (lendingRepository.findMaxLendingId() + 1);
        lending.setLendingCode(lendingCode);
        lending.setReaderId(resource.getReaderId());
        lending.setBookId(resource.getBookId());
        lending.setLendDate(LocalDate.now());
        lending.setLimitDate(limitDate);
        lending.setReturned(false);
        lending.setFine(0.0f);
        lending.setComment("");
        lending.setDaysOverdue(0);
        lending.setDaysTillReturn((int) ChronoUnit.DAYS.between(LocalDate.now(), limitDate));
        lending.setBookTitle(book.get().getTitle());

        return lendingRepository.save(lending);
    }

 */

    public Lending returnBook(final EditLendingRequest resource, Long version) {
        Optional<Lending> lending = Optional.empty();
        Lending returnedLending;

        // Procurar pelo código do empréstimo ou pelo ID
        if (resource.getLendingCode() != null) {
            lending = lendingRepository.findByLendingCode(resource.getLendingCode());
        } else if (resource.getId() != null) {
            lending = lendingRepository.findById(resource.getId());
        }

        if (lending.isEmpty()) {
            throw new NotFoundException("[ERROR] Lending not found");
        }

        returnedLending = lending.get();

        // Verificação da versão para evitar conflitos de concorrência
        if (version != null && !Long.valueOf(returnedLending.getVersion()).equals(version)) {
            throw new RuntimeException("[ERROR] Version mismatch. Expected version: " + version + ", but found: " + returnedLending.getVersion());
        }

        // Verificar se o livro já foi devolvido
        if (returnedLending.isReturned()) {
            throw new IllegalArgumentException("[ERROR] Book with lending number: " + resource.getId() + " is already returned.");
        }

        // Atualizar as informações do empréstimo para refletir a devolução
        returnedLending.setReturnedDate(LocalDate.now());

        int daysOverdue = (int) ChronoUnit.DAYS.between(returnedLending.getLimitDate(), LocalDate.now());
        float fine;
        if (daysOverdue < 0) {
            daysOverdue = 0;
            fine = 0;
        } else {
            fine = calculateFine(daysOverdue);
        }

        returnedLending.setDaysOverdue(daysOverdue);
        returnedLending.setDaysTillReturn(0);
        returnedLending.setReturned(true);
        returnedLending.setFine(fine);
        returnedLending.setComment(resource.getComment());

        //Repo save I1
        // Salvar o empréstimo atualizado no repositório
        return lendingRepository.save(returnedLending);
    }



    @Override
    public List<LentBookView> getTopBooks() {
        List<Object[]> topBookIds = lendingRepository.findTopBookIds();

        return topBookIds.stream()
                .map(record -> new LentBookView((Long) record[0], (Long) record[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<LendingReaderView> getTopReaders() {
        Pageable pageable = PageRequest.of(0, 5); // Define que queremos apenas os 5 primeiros resultados
        List<Object[]> topReaders = lendingRepository.findTopReaders(pageable);

        return topReaders.stream()
                .map(record -> new LendingReaderView((Long) record[0], (Long) record[1]))
                .collect(Collectors.toList());
    }

    private float calculateFine(long daysOverdue) {
        return daysOverdue * lateFee;
    }

    public boolean lendingExists(String lendingCode) {
        // Verifica se já existe um empréstimo com o lendingCode
        return lendingRepository.findByLendingCode(lendingCode).isPresent();
    }
}

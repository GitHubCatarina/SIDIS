package com.example.serviceLendingQuery.lendingManagementQuery.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table
public class Lending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(length = 255)
    private String lendingCode;

    @Version
    private long version;

    @NotNull
    @Column(length = 255)
    private Long readerId;

    @NotNull
    @Column(length = 255)
    private Long bookId;

    @NotNull
    @Column(length = 255)
    private String bookTitle;

    @NotNull
    private LocalDate lendDate;

    @NotNull
    private LocalDate limitDate;

    private LocalDate returnedDate;

    private Integer daysTillReturn;

    private boolean returned;

    private Integer daysOverdue;

    private Float fine;

    private String comment;

    public Lending() {

    }

    public Lending(String lendingCode, Long readerId, Long bookId, String bookTitle, LocalDate lendDate, LocalDate limitDate, LocalDate returnedDate, boolean returned, Float fine, String comment) {
        this.lendingCode = lendingCode;
        this.readerId = readerId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.lendDate = lendDate;
        this.limitDate = limitDate;
        this.returnedDate = returnedDate;
        this.returned = returned;
        this.fine = fine;
        this.comment = comment;

        if (!returned) {
            this.daysTillReturn = (int) ChronoUnit.DAYS.between(LocalDate.now(), limitDate);
        } else {
            this.daysTillReturn = 0;
        }

        if (!returned && LocalDate.now().isAfter(limitDate)) {
            this.daysOverdue = (int) ChronoUnit.DAYS.between(limitDate, LocalDate.now());
        } else {
            this.daysOverdue = 0;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLendingCode() {
        return lendingCode;
    }

    public void setLendingCode(String lendingCode) {
        this.lendingCode = lendingCode;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Long getReaderId() {
        return readerId;
    }

    public void setReaderId(Long readerId) {
        this.readerId = readerId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public LocalDate getLendDate() {
        return lendDate;
    }

    public void setLendDate(LocalDate lendDate) {
        this.lendDate = lendDate;
    }

    public LocalDate getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(LocalDate limitDate) {
        this.limitDate = limitDate;
    }

    public LocalDate getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(LocalDate returnedDate) {
        this.returnedDate = returnedDate;
    }

    public Integer getDaysTillReturn() {
        return daysTillReturn;
    }

    public void setDaysTillReturn(Integer daysTillReturn) {
        this.daysTillReturn = daysTillReturn;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public Integer getDaysOverdue() {
        return daysOverdue;
    }

    public void setDaysOverdue(Integer daysOverdue) {
        this.daysOverdue = daysOverdue;
    }

    public Float getFine() {
        return fine;
    }

    public void setFine(Float fine) {
        this.fine = fine;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Lending{" +
                "id=" + id +
                ", lendingCode='" + lendingCode + '\'' +
                ", bookId=" + bookId +
                ", bookTitle='" + bookTitle + '\'' +
                ", lendDate=" + lendDate +
                ", limitDate=" + limitDate +
                ", returnedDate=" + returnedDate +
                ", daysTillReturn=" + daysTillReturn +
                ", returned=" + returned +
                ", daysOverdue=" + daysOverdue +
                ", fine=" + fine +
                ", comment='" + comment + '\'' +
                ", readerId=" + readerId +
                '}';
    }
}

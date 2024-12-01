package com.example.serviceTopQuery.topManagement.dto;

import com.example.serviceTopQuery.topManagement.model.Lending;

import java.time.LocalDate;

public class LendingDTO {

    private Long id;
    private String lendingCode;
    private Long readerId;
    private Long bookId;
    private String bookTitle;
    private LocalDate lendDate;
    private LocalDate limitDate;
    private LocalDate returnedDate;
    private Integer daysTillReturn;
    private boolean returned;
    private Integer daysOverdue;
    private Float fine;
    private String comment;

    public LendingDTO() {
    }

    // Construtor com parâmetros para facilitar a criação do DTO
    public LendingDTO(Long id, String lendingCode, Long readerId, Long bookId, String bookTitle,
                      LocalDate lendDate, LocalDate limitDate, LocalDate returnedDate,
                      Integer daysTillReturn, boolean returned, Integer daysOverdue,
                      Float fine, String comment) {
        this.id = id;
        this.lendingCode = lendingCode;
        this.readerId = readerId;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.lendDate = lendDate;
        this.limitDate = limitDate;
        this.returnedDate = returnedDate;
        this.daysTillReturn = daysTillReturn;
        this.returned = returned;
        this.daysOverdue = daysOverdue;
        this.fine = fine;
        this.comment = comment;
    }

    // Getters e Setters
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
        return "LendingDTO{" +
                "id=" + id +
                ", lendingCode='" + lendingCode + '\'' +
                ", readerId=" + readerId +
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
                '}';
    }

    public LendingDTO toDTO(Lending lending) {
        return new LendingDTO(
                lending.getId(),
                lending.getLendingCode(),
                lending.getReaderId(),
                lending.getBookId(),
                lending.getBookTitle(),
                lending.getLendDate(),
                lending.getLimitDate(),
                lending.getReturnedDate(),
                lending.getDaysTillReturn(),
                lending.isReturned(),
                lending.getDaysOverdue(),
                lending.getFine(),
                lending.getComment()
        );
    }

}

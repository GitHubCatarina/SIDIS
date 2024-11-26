package com.example.serviceTopQuery.topManagement.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditLendingRequest {
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
}



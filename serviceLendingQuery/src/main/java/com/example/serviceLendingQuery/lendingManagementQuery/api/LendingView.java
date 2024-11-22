package com.example.serviceLendingQuery.lendingManagementQuery.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Lending")
public class LendingView {
    private Long id;
    private String lendingCode;
    private Long readerId;
    private Long bookId;
    private String bookTitle;
    private LocalDate lendDate;
    private LocalDate limitDate;
    private LocalDate returnedDate;
    private Integer daysTillReturn;
    private Integer daysOverdue;
    private Float fine;
    private boolean returned;
    private String comment;
}

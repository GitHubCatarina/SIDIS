package com.example.serviceTopQuery.topManagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Average Lending Duration Per Book")
public class LendingAvgPerBookView {
    private Long bookId;
    private Double averageDuration;

    public LendingAvgPerBookView(Long bookId, Double averageDuration) {
        this.bookId = bookId;
        this.averageDuration = averageDuration;
    }
}

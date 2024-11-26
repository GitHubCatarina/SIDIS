package com.example.serviceTopQuery.topManagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Average Lending Duration Per Genre Per Month")
public class LendingAvgPerGenrePerMonthView {
    private Long genreId;
    private Double averageDuration;

    public LendingAvgPerGenrePerMonthView(Long genreId, Double averageDuration) {
        this.genreId = genreId;
        this.averageDuration = averageDuration;
    }
}

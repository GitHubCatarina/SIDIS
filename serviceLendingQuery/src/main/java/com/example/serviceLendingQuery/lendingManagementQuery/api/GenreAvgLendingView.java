package com.example.serviceLendingQuery.lendingManagementQuery.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Genre Average Lending Duration View")
public class GenreAvgLendingView {
    private String genreName;
    private int month;
    private double averageDuration;
}

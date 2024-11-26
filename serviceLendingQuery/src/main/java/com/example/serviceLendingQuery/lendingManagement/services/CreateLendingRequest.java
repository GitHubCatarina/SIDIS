package com.example.serviceLendingQuery.lendingManagement.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateLendingRequest {
    private Long readerId;
    private Long bookId;
}

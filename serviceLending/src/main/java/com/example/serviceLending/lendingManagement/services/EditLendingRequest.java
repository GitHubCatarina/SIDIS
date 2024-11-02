package com.example.serviceLending.lendingManagement.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditLendingRequest {
    private Long id;
    private String lendingCode;
    private String comment;
}

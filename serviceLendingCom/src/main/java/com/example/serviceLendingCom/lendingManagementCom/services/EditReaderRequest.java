package com.example.serviceLendingCom.lendingManagementCom.services;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditReaderRequest {

    @Size(min = 2, max = 150)
    private String name;

    @Size(min = 3, max = 512)
    private String email;

    private LocalDate dateOfBirth;

    private Integer phoneNumber;

    private Boolean GDBRConsent;

    private List<String> interests;
}


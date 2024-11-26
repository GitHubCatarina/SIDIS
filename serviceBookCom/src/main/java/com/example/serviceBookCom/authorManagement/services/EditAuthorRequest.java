package com.example.serviceBookCom.authorManagement.services;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditAuthorRequest {
    @Size(min = 1, max = 150)
    private String name;
    @Size(min = 1, max = 4096)
    private String shortBio;
}
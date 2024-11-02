package com.example.serviceBook.bookManagement.services;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateBookRequest extends EditBookRequest {
    @Size(min = 1, max = 256)
    private String isbn;
}

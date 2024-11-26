package com.example.serviceBookQuery.bookManagement.services;

import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.serviceBookQuery.bookManagement.model.BookAuthor;
import com.example.serviceBookQuery.bookManagement.model.Genre;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditBookRequest {
    @Size(min = 1, max = 256)
    private String title;

    private Genre genre;

    @Size(min = 0, max = 4096)
    private String description;

    private List<BookAuthor> bookAuthors;
}

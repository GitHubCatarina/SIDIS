package com.example.serviceBookCom.bookManagementCom.services;

import com.example.serviceBookCom.bookManagementCom.model.BookAuthor;
import com.example.serviceBookCom.bookManagementCom.model.Genre;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

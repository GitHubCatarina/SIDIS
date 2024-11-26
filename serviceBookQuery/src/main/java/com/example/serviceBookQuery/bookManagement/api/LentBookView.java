package com.example.serviceBookQuery.bookManagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.example.serviceBookQuery.authorManagement.api.AuthorView;

import java.util.List;

@Data
@Schema(description = "LentBookView")
public class LentBookView {
    private Long bookId;
    private String isbn;
    private String title;
    private BookGenreView genre;
    private String description;
    private List<BookAuthorView> bookAuthors;

    public void setBookAuthors(List<AuthorView> authorViews) {
    }
}


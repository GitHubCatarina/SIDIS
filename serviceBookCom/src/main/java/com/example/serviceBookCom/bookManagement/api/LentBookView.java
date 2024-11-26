package com.example.serviceBookCom.bookManagement.api;

import com.example.serviceBookCom.authorManagement.api.AuthorView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

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


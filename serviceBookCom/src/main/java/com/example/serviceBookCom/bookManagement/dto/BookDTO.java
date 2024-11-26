package com.example.serviceBookCom.bookManagement.dto;

import com.example.serviceBookCom.bookManagement.model.Genre;

import java.util.List;

public class BookDTO {

    private Long id;
    private String isbn;
    private String title;
    private Long genreId;
    private String description;
    private List<String> bookAuthors;
    private String coverUrl;

    public BookDTO() {
    }

    // Modify the constructor to accept Genre instead of String
    public BookDTO(Long id, String isbn, String title, Genre genre, String description, List<String> bookAuthors, String coverUrl) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.genreId = genre != null ? genre.getId() : 3;
        this.description = description;
        this.bookAuthors = bookAuthors;
        this.coverUrl = coverUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(List<String> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "BookDTO{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", genreId=" + genreId +
                ", description='" + description + '\'' +
                ", bookAuthors=" + bookAuthors +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}

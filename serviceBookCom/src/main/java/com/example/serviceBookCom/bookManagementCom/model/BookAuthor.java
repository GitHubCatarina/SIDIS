package com.example.serviceBookCom.bookManagementCom.model;

import com.example.serviceBookCom.authorManagementCom.model.Author;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table
public class BookAuthor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bookId")
    @NotNull
    private Book book;

    @ManyToOne
    @JoinColumn(name = "authorId")
    @NotNull
    private Author author;

    public BookAuthor() {
    }

    public BookAuthor(Book book, Author author) {
        this.book = book;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "BookAuthor{" +
                "id=" + id +
                ", book=" + book +
                ", author=" + author +
                '}';
    }
}

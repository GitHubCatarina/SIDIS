package com.example.serviceLendingCom.lendingManagementCom.model;

import com.example.serviceLendingCom.lendingManagementCom.util.BookUtil;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.StaleObjectStateException;

import java.util.List;

@Entity
@Table
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;

    @Column(length = 13, nullable = false, unique = true, updatable = false)
    @NotEmpty
    private String isbn;

    @Column(length = 255, nullable = false)
    @NotEmpty
    private String title;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "genreId")
    private Genre genre;

    @Column(length = 4096, nullable = true)
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "book", orphanRemoval = true)
    private List<BookAuthor> bookAuthors;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private BookCover cover;

    public Book() {
    }

    public Book(String isbn, String title, Genre genre, String description, List<BookAuthor> bookAuthors, BookCover cover) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.bookAuthors = bookAuthors;
        this.cover = cover;
    }

    public Book(String isbn, String title, Genre genre, List<BookAuthor> bookAuthors, String description) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.bookAuthors = bookAuthors;
        this.description = description;
    }

    public Book(String isbn, String title, Genre genre, String description) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        if (!BookUtil.isValidISBN(isbn)) {
            throw new IllegalArgumentException("[ERROR] Isbn is no valid.");
        }
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (title == null && title.isBlank() || title.length() > 255) {
            throw new IllegalArgumentException("[ERROR] Title is not valid.");
        }

        this.title = title;
    }

    public Genre getGenre() {
        if (genre == null) {
            throw new IllegalArgumentException("[ERROR] Genre is not valid.");
        }
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getDescription() {
        if (genre == null) {
            throw new IllegalArgumentException("[ERROR] Genre is not valid.");
        }
        return description;
    }

    public void setDescription(String description) {
        if (description != null && description.length() > 4096) {
            throw new IllegalArgumentException("[ERROR] Description is not valid.");
        }
        this.description = description;
    }

    public List<BookAuthor> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(List<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }

    public BookCover getCover() {
        return cover;
    }

    public void setCover(BookCover cover) {
        this.cover = cover;
    }

    public void updateData(final long desiredVersion, final String title, final Genre genre, final String description) {
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        setTitle(title);
        setGenre(genre);
        setDescription(description);
    }

    public void applyPatch(final long desiredVersion, final String title, final Genre genre, final String description) {
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        if (title != null && !title.isEmpty()) {
            setTitle(title);
        }
        if (genre != null) {
            setGenre(genre);
        }

        if (description != null) {
            if (description.isEmpty()) {
                setDescription(null);
            } else {
                setDescription(description);
            }
        } else {
            setDescription(null);
        }
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", description='" + description + '\'' +
                ", bookAuthors=" + bookAuthors +
                '}';
    }
}
package com.example.serviceLendingCom.lendingManagementCom.services;

import com.example.serviceLendingCom.lendingManagementCom.model.Book;
import com.example.serviceLendingCom.lendingManagementCom.model.BookAuthor;
import com.example.serviceLendingCom.lendingManagementCom.model.BookCover;
import com.example.serviceLendingCom.lendingManagementCom.model.Genre;
import com.example.serviceLendingCom.fileStorage.UploadFileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> getBook(String isbn);
    Page<Book> getBooks(Pageable pageable);
    Iterable<Book> getAllBooks();
    Iterable<Genre> getTopGenres();
    //Iterable<Book> getTopBooks();
    Page<Book> getBooksByGenre(String genre, Pageable pageable);
    Page<Book> getBooksByTitle(String title, Pageable pageable);
    Page<Book> getBooksByAuthor(String author, Pageable pageable);
    Page<Book> getBooksByTitleAndGenreAndAuthor(String genre, String title, String author, Pageable pageable);
    List<BookAuthor> getBookAuthorsByAuthorId(Long authorId);
    BookCover getBookCover(String bookId);
    Book createBook(CreateBookRequest resource, MultipartFile coverPhoto);
    Book updateBook(Long id, EditBookRequest resource, long desiredVersion);
    Book partialUpdateBook(Long id, EditBookRequest resource, long desiredVersion);
    UploadFileResponse doUploadFile(String id, MultipartFile file);
    boolean bookExists(Long bookId);
}

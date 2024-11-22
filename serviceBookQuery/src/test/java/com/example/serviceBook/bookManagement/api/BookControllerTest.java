package com.example.serviceBook.bookManagement.api;

import com.example.serviceBookCom.bookManagementCom.api.BookController;
import com.example.serviceBookCom.bookManagementCom.model.Book;
import com.example.serviceBookCom.bookManagementCom.model.BookCover;
import com.example.serviceBookCom.bookManagementCom.services.BookServiceImpl;
import com.example.serviceBookCom.bookManagementCom.services.CreateBookRequest;
import com.example.serviceBookCom.bookManagementCom.services.EditBookRequest;
import com.example.serviceBookCom.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getBook() {
        when(bookService.getBook("123")).thenReturn(java.util.Optional.of(new Book()));
        assertDoesNotThrow(() -> bookController.getBook("123", "Bearer token"));
        verify(bookService, times(1)).getBook("123");
    }

    @Test
    void getBooks() {
        assertDoesNotThrow(() -> bookController.getBooks(null, null, null, 0, 100, "Bearer token"));
        verify(bookService, times(1)).getBooks(any());
    }

    @Test
    void getTopGenres() {
        assertDoesNotThrow(() -> bookController.getTopGenres("Bearer token"));
        verify(bookService, times(1)).getTopGenres();
    }

    /*
    @Test
    void getTopBooks() {
        assertDoesNotThrow(() -> bookController.getTopBooks());
        verify(bookService, times(1)).getTopBooks();
    }

     */

    @Test
    void getBookCover() {
        when(bookService.getBookCover("123")).thenReturn(new BookCover());
        assertDoesNotThrow(() -> bookController.getBookCover("123", null, "Bearer token"));
        verify(bookService, times(1)).getBookCover("123");
    }

    @Test
    void uploadFile() {
        assertDoesNotThrow(() -> bookController.uploadFile("123", null, "Bearer token"));
        verify(bookService, times(1)).doUploadFile(eq("123"), any());
    }

    @Test
    void createBook() {
        assertDoesNotThrow(() -> bookController.createBook(new CreateBookRequest(), null, "Bearer token"));
        verify(bookService, times(1)).createBook(any(), any());
    }

    @Test
    void updateBook() {
        assertDoesNotThrow(() -> bookController.updateBook(null, 1L, new EditBookRequest(), "Bearer token"));
        verify(bookService, times(1)).updateBook(eq(1L), any(), anyLong());
    }

    @Test
    void partialUpdateBook() {
        assertDoesNotThrow(() -> bookController.partialUpdateBook(null, 1L, new EditBookRequest(), "Bearer token"));
        verify(bookService, times(1)).partialUpdateBook(eq(1L), any(), anyLong());
    }
}
